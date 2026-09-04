package com.autogestion.config;

import com.autogestion.entity.*;
import com.autogestion.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Base de datos ya tiene datos, omitiendo seed...");
            return;
        }

        log.info("Insertando datos semilla...");

        
        String hash = passwordEncoder.encode("admin123");
        Usuario admin = usuarioRepository.save(Usuario.builder()
                .nombre("Admin Taller").email("admin@sanmartin.pe")
                .passwordHash(hash).rol("ADMIN").activo(true).build());
        Usuario mecanico = usuarioRepository.save(Usuario.builder()
                .nombre("Mecánico Uno").email("mecanico1@sanmartin.pe")
                .passwordHash(hash).rol("MECANICO").activo(true).build());
        usuarioRepository.save(Usuario.builder()
                .nombre("Almacenero").email("almacen@sanmartin.pe")
                .passwordHash(hash).rol("ALMACENERO").activo(true).build());

        
        Cliente c1 = clienteRepository.save(Cliente.builder()
                .nombre("Juan Pérez").telefono("951234567")
                .email("juan.perez@gmail.com").documento("45123698").build());
        Cliente c2 = clienteRepository.save(Cliente.builder()
                .nombre("María López").telefono("962345678")
                .email("maria.lopez@hotmail.com").documento("40258741").build());
        Cliente c3 = clienteRepository.save(Cliente.builder()
                .nombre("Carlos García").telefono("973456789")
                .email("carlos.garcia@yahoo.com").documento("41369852").build());

        
        vehiculoRepository.save(Vehiculo.builder()
                .cliente(c1).placa("ABC-123").marca("Toyota")
                .modelo("Corolla").anio(2020).build());
        vehiculoRepository.save(Vehiculo.builder()
                .cliente(c2).placa("DEF-456").marca("Hyundai")
                .modelo("Accent").anio(2019).build());
        vehiculoRepository.save(Vehiculo.builder()
                .cliente(c3).placa("GHI-789").marca("Nissan")
                .modelo("Sentra").anio(2021).build());

        
        servicioRepository.save(Servicio.builder().nombre("Cambio de aceite").precioBase(new BigDecimal("80.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Alineación y balanceo").precioBase(new BigDecimal("120.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Cambio de frenos").precioBase(new BigDecimal("200.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Diagnóstico computarizado").precioBase(new BigDecimal("150.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Reparación de motor").precioBase(new BigDecimal("800.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Cambio de correa de distribución").precioBase(new BigDecimal("350.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Lavado técnico").precioBase(new BigDecimal("50.00")).build());
        servicioRepository.save(Servicio.builder().nombre("Revisión de suspensión").precioBase(new BigDecimal("100.00")).build());

        
        productoRepository.save(Producto.builder().nombre("Aceite 5W-30 4L").tipo("INSUMO").precioUnitario(new BigDecimal("65.00")).stockActual(50).stockMinimo(10).build());
        productoRepository.save(Producto.builder().nombre("Filtro de aceite").tipo("REPUESTO").precioUnitario(new BigDecimal("25.00")).stockActual(40).stockMinimo(8).build());
        productoRepository.save(Producto.builder().nombre("Filtro de aire").tipo("REPUESTO").precioUnitario(new BigDecimal("35.00")).stockActual(30).stockMinimo(8).build());
        productoRepository.save(Producto.builder().nombre("Pastillas de freno delanteras").tipo("REPUESTO").precioUnitario(new BigDecimal("120.00")).stockActual(20).stockMinimo(5).build());
        productoRepository.save(Producto.builder().nombre("Pastillas de freno traseras").tipo("REPUESTO").precioUnitario(new BigDecimal("100.00")).stockActual(15).stockMinimo(5).build());
        productoRepository.save(Producto.builder().nombre("Disco de freno").tipo("REPUESTO").precioUnitario(new BigDecimal("180.00")).stockActual(10).stockMinimo(3).build());
        productoRepository.save(Producto.builder().nombre("Correa de distribución").tipo("REPUESTO").precioUnitario(new BigDecimal("90.00")).stockActual(8).stockMinimo(3).build());
        productoRepository.save(Producto.builder().nombre("Líquido de frenos 500ml").tipo("INSUMO").precioUnitario(new BigDecimal("30.00")).stockActual(25).stockMinimo(5).build());
        productoRepository.save(Producto.builder().nombre("Refrigerante 1L").tipo("INSUMO").precioUnitario(new BigDecimal("20.00")).stockActual(30).stockMinimo(8).build());
        productoRepository.save(Producto.builder().nombre("Bujias (juego 4)").tipo("REPUESTO").precioUnitario(new BigDecimal("45.00")).stockActual(20).stockMinimo(5).build());
        productoRepository.save(Producto.builder().nombre("Amortiguador delantero").tipo("REPUESTO").precioUnitario(new BigDecimal("250.00")).stockActual(6).stockMinimo(2).build());
        productoRepository.save(Producto.builder().nombre("Aceite de transmisión ATF").tipo("INSUMO").precioUnitario(new BigDecimal("55.00")).stockActual(15).stockMinimo(5).build());

        log.info("✅ Datos semilla insertados correctamente");
        log.info("   Usuarios: 3 (admin@sanmartin.pe / admin123)");
        log.info("   Clientes: 3 | Vehículos: 3");
        log.info("   Servicios: 8 | Productos: 12");
    }
}
