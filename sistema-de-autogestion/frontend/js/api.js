const API_BASE = 'http://localhost:8080/api';
function getToken() { return localStorage.getItem('token'); }
function setToken(token) { localStorage.setItem('token', token); }
function clearToken() { localStorage.removeItem('token'); localStorage.removeItem('user'); }
function getUser() { const u = localStorage.getItem('user'); return u ? JSON.parse(u) : null; }
function setUser(user) { localStorage.setItem('user', JSON.stringify(user)); }
function isLoggedIn() { return !!getToken(); }
function logout() { clearToken(); window.location.href = '../index.html'; }
function checkAuth() { if (!isLoggedIn()) { window.location.href = '../index.html'; return false; } return true; }
async function apiFetch(endpoint, options = {}) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json', ...options.headers };
    if (token) headers['Authorization'] = `Bearer ${token}`;
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
        if (response.status === 401 || response.status === 403) {
            clearToken();
            
            const currentPath = window.location.pathname;
            if (!currentPath.endsWith('index.html') && currentPath !== '/' && currentPath !== '') {
                window.location.href = window.location.pathname.includes('/pages/') ? '../index.html' : 'index.html';
            }
            throw new Error('Sesion expirada o no autorizada');
        }
        if (!response.ok) { const err = await response.text(); throw new Error(err || `Error ${response.status}`); }
        const ct = response.headers.get('content-type');
        if (ct && ct.includes('application/json')) return await response.json();
        return null;
    } catch (error) { console.error('API Error:', error); throw error; }
}
function showAlert(containerId, message, type = 'danger') {
    const container = document.getElementById(containerId);
    if (!container) return;
    const icons = { success: 'checkCircle', danger: 'alertTriangle', warning: 'alertTriangle', info: 'info' };
    container.innerHTML = `
        <div class="ag-alert ag-alert-${type}">
            ${icon(icons[type] || 'info', 18)}
            <span>${message}</span>
            <button class="ag-alert-close" onclick="this.parentElement.remove()">
                ${icon('x', 14)}
            </button>
        </div>`;
    setTimeout(() => { const a = container.querySelector('.ag-alert'); if (a) a.remove(); }, 5000);
}
function showSuccess(id, msg) { showAlert(id, msg, 'success'); }
function showError(id, msg) { showAlert(id, msg, 'danger'); }
function renderNavbar(activePage) {
    const user = getUser();
    if (!user) return '';

    const roleClass = user.rol === 'ADMIN' ? 'role-admin' : user.rol === 'MECANICO' ? 'role-mecanico' : 'role-almacenero';
    const roleLabel = user.rol === 'ADMIN' ? 'Administrador' : user.rol === 'MECANICO' ? 'Mecanico' : 'Almacenero';

    const navLinks = {
        ADMIN: [
            { page: 'dashboard', label: 'Dashboard', icon: 'dashboard' },
            { page: 'recepcion', label: 'Recepcion', icon: 'clipboard' },
            { page: 'cotizacion', label: 'Cotizacion', icon: 'fileText' },
            { page: 'orden_trabajo', label: 'Ordenes', icon: 'clipboardList' },
            { page: 'inventario', label: 'Inventario', icon: 'box' },
            { page: 'pago_entrega', label: 'Pago/Entrega', icon: 'dollarSign' },
        ],
        MECANICO: [
            { page: 'dashboard', label: 'Dashboard', icon: 'dashboard' },
            { page: 'recepcion', label: 'Recepcion', icon: 'clipboard' },
            { page: 'cotizacion', label: 'Cotizacion', icon: 'fileText' },
            { page: 'orden_trabajo', label: 'Mis Ordenes', icon: 'clipboardList' },
        ],
        ALMACENERO: [
            { page: 'dashboard', label: 'Dashboard', icon: 'dashboard' },
            { page: 'inventario', label: 'Inventario', icon: 'box' },
        ],
    };

    const links = (navLinks[user.rol] || []).map(l =>
        `<a href="${l.page}.html" class="${activePage === l.page ? 'active' : ''}">${icon(l.icon, 16)} ${l.label}</a>`
    ).join('');

    return `
    <nav class="ag-navbar">
        <a class="ag-navbar-brand" href="pages/dashboard.html">
            ${icon('wrench', 22)} AutoGestion
        </a>
        <div class="ag-navbar-nav">${links}</div>
        <div class="ag-navbar-user">
            <span class="ag-user-badge ${roleClass}">${roleLabel}</span>
            <span class="ag-user-name">${user.nombre}</span>
            <button class="ag-btn-logout" onclick="logout()">${icon('logOut', 14)} Salir</button>
        </div>
    </nav>`;
}
function formatCurrency(amount) {
    return new Intl.NumberFormat('es-PE', { style: 'currency', currency: 'PEN' }).format(amount);
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    return d.toLocaleDateString('es-PE') + ' ' + d.toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' });
}

function estadoBadge(estado) {
    const map = {
        'PENDIENTE': 'secondary', 'EN_DIAGNOSTICO': 'info', 'COTIZADA': 'warning',
        'EN_TRABAJO': 'primary', 'EN_PROCESO': 'warning', 'EN_PRUEBA': 'info',
        'FINALIZADA': 'success', 'CANCELADA': 'danger', 'APROBADA': 'success',
        'RECHAZADA': 'danger', 'ENTREGADA': 'success',
    };
    return `<span class="ag-badge ag-badge-${map[estado] || 'secondary'}">${estado.replace(/_/g, ' ')}</span>`;
}

function tipoBadge(tipo) {
    return `<span class="ag-badge ag-badge-${tipo === 'REPUESTO' ? 'repuesto' : 'insumo'}">${tipo}</span>`;
}

function stockClass(actual, minimo) {
    if (actual === 0) return 'row-danger';
    if (actual < minimo) return 'row-danger';
    return '';
}
