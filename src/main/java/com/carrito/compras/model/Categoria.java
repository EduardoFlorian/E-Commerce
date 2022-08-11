package com.carrito.compras.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name="tb_categoria")
public class Categoria {
	@Id
	@Column(name="id_categoria")
	private Integer idCategoria;
	
	@Column(name="nombre_categoria")
	private String nombreCategoria;
}
