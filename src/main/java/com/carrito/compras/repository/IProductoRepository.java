package com.carrito.compras.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carrito.compras.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto,Integer> {
	List<Producto> findByIdCategoria(Integer idcategoria);
}
