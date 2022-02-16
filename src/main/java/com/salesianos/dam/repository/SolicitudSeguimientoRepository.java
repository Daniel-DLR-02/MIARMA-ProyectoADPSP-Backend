package com.salesianos.dam.repository;

import com.salesianos.dam.model.SolicitudSeguimiento;
import com.salesianos.dam.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SolicitudSeguimientoRepository extends JpaRepository<SolicitudSeguimiento,Long> {

}
