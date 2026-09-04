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
    const svgMap = {
        success: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>',
        danger: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>',
        warning: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>',
        info: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="16" x2="12" y2="12"/><line x1="12" y1="8" x2="12.01" y2="8"/></svg>'
    };
    container.innerHTML = `
        <div class="ag-alert ag-alert-${type}">
            ${svgMap[type] || svgMap.info}
            <span>${message}</span>
            <button class="ag-alert-close" onclick="this.parentElement.remove()">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
            </button>
        </div>`;
    setTimeout(() => { const a = container.querySelector('.ag-alert'); if (a) a.remove(); }, 5000);
}

function showSuccess(id, msg) { showAlert(id, msg, 'success'); }
function showError(id, msg) { showAlert(id, msg, 'danger'); }

function renderNavbar(activePage) {
    const user = getUser();
    if (!user) return '';

    const roleLabel = user.rol === 'ADMIN' ? 'Administrador' : user.rol === 'MECANICO' ? 'Mecanico' : 'Almacenero';
    const dotClass = user.rol === 'ADMIN' ? 'dot-admin' : user.rol === 'MECANICO' ? 'dot-mecanico' : 'dot-almacenero';

    const navLinks = {
        ADMIN: [
            { page: 'dashboard', label: 'Dashboard', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="9"/><rect x="14" y="3" width="7" height="5"/><rect x="14" y="12" width="7" height="9"/><rect x="3" y="16" width="7" height="5"/></svg>' },
            { page: 'recepcion', label: 'Recepcion', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 16H9m10 0h3v-3.15a1 1 0 0 0-.84-.99L16 11l-2.7-3.6a1 1 0 0 0-.8-.4H5.24a2 2 0 0 0-1.8 1.1l-.8 1.63A6 6 0 0 0 2 12.42V16h2"/><circle cx="6.5" cy="16.5" r="2.5"/><circle cx="16.5" cy="16.5" r="2.5"/></svg>' },
            { page: 'cotizacion', label: 'Cotizacion', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>' },
            { page: 'orden_trabajo', label: 'Ordenes', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/></svg>' },
            { page: 'inventario', label: 'Inventario', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/></svg>' },
            { page: 'pago_entrega', label: 'Pago / Entrega', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="1" x2="12" y2="23"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>' },
        ],
        MECANICO: [
            { page: 'dashboard', label: 'Dashboard', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="9"/><rect x="14" y="3" width="7" height="5"/><rect x="14" y="12" width="7" height="9"/><rect x="3" y="16" width="7" height="5"/></svg>' },
            { page: 'recepcion', label: 'Recepcion', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 16H9m10 0h3v-3.15a1 1 0 0 0-.84-.99L16 11l-2.7-3.6a1 1 0 0 0-.8-.4H5.24a2 2 0 0 0-1.8 1.1l-.8 1.63A6 6 0 0 0 2 12.42V16h2"/><circle cx="6.5" cy="16.5" r="2.5"/><circle cx="16.5" cy="16.5" r="2.5"/></svg>' },
            { page: 'cotizacion', label: 'Cotizacion', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/></svg>' },
            { page: 'orden_trabajo', label: 'Mis Ordenes', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/></svg>' },
        ],
        ALMACENERO: [
            { page: 'dashboard', label: 'Dashboard', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="7" height="9"/><rect x="14" y="3" width="7" height="5"/><rect x="14" y="12" width="7" height="9"/><rect x="3" y="16" width="7" height="5"/></svg>' },
            { page: 'inventario', label: 'Inventario', icon: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/></svg>' },
        ],
    };

    const links = (navLinks[user.rol] || []).map(l =>
        `<a href="${l.page}.html" class="${activePage === l.page ? 'active' : ''}">${l.icon} ${l.label}</a>`
    ).join('');

    return `
    <nav class="ag-navbar">
        <a class="ag-navbar-brand" href="dashboard.html">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>
            AutoGestion
        </a>
        <div class="ag-navbar-nav">${links}</div>
        <div class="ag-navbar-right">
            <div class="ag-user-pill">
                <span class="ag-user-pill-dot ${dotClass}"></span>
                <span class="ag-user-pill-name">${user.nombre}</span>
                <span class="ag-user-pill-role">${roleLabel}</span>
            </div>
            <button class="ag-btn-ghost" onclick="logout()">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                Salir
            </button>
        </div>
    </nav>`;
}

function renderThemeToggle() {
    const stored = localStorage.getItem('ag-theme') || 'system';
    const icons = {
        system: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>',
        light: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/></svg>',
        dark: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>'
    };
    return `<button class="ag-theme-toggle" id="themeToggle" title="Cambiar tema" aria-label="Cambiar tema">${icons[stored]}</button>`;
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

function initTheme() {
    const stored = localStorage.getItem('ag-theme') || 'system';
    applyTheme(stored);

    const btn = document.getElementById('themeToggle');
    if (!btn) return;

    const order = ['system', 'light', 'dark'];
    const iconMap = {
        system: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"/><line x1="8" y1="21" x2="16" y2="21"/><line x1="12" y1="17" x2="12" y2="21"/></svg>',
        light: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/></svg>',
        dark: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>'
    };

    const mq = window.matchMedia('(prefers-color-scheme: dark)');
    mq.addEventListener('change', () => {
        if (localStorage.getItem('ag-theme') === 'system') applyTheme('system');
    });

    btn.addEventListener('click', () => {
        const current = localStorage.getItem('ag-theme') || 'system';
        const next = order[(order.indexOf(current) + 1) % order.length];
        localStorage.setItem('ag-theme', next);
        applyTheme(next);
        btn.innerHTML = iconMap[next];
        btn.title = `Tema: ${next}`;
    });
}

function applyTheme(mode) {
    const mq = window.matchMedia('(prefers-color-scheme: dark)');
    const resolved = mode === 'system' ? (mq.matches ? 'dark' : 'light') : mode;
    document.documentElement.setAttribute('data-bs-theme', resolved);
}
