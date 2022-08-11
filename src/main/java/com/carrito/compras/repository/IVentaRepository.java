package com.carrito.compras.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrito.compras.model.Venta;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Integer> {

}
