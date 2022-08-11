package com.carrito.compras.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name="tb_producto")
public class Producto {
	@Id
	@Column(name="id_prod")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProducto;
	
	
	@Column(name="nombre_prod")
	@NotEmpty
	private String nombre;
	
	@Column(name="precio_prod")
	@NotNull
	@Min(value=1)
	private Double precio;
	
	
	@Column(name="id_categoria") //Mismo name que la columna de la tabla Categoria
	@NotNull
	@Min(value=1)
	private Integer idCategoria;
	
	
	@Column(name="stock_pro")
	@NotNull
	@Min(value=1)
	private Integer stock;
	
	@ManyToOne
	@JoinColumn(name="id_categoria", insertable=false, updatable=false)
	private Categoria categoria;

	public Producto(Integer idProducto, @NotEmpty String nombre, @NotNull Double precio,
			@NotNull @Min(1) Integer idCategoria, @NotNull Integer stock) {
		super();
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.idCategoria = idCategoria;
		this.stock = stock;
	}

	public Producto() {
		super();
	}
	
	public void restarExistencia(int unidadesCompradas) {
	   this.stock -= unidadesCompradas;
	}

	
}
