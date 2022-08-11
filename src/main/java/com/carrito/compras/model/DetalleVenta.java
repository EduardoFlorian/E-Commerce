package com.carrito.compras.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tb_detalle_venta")
public class DetalleVenta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_detalle;
	
	@Column(name="nombre_producto")
	private String nombre;
	
	@Column(name="cantidad_detalle")
	private Integer cantidad;
	
	@Column(name="precio_producto")
	private double precio;
	
	@Column(name="subtotal_producto")
	private double subtotal;
	
	@ManyToOne
	private Venta venta;
	
	@ManyToOne
	private Producto producto;

	public Integer getId_detalle() {
		return id_detalle;
	}

	public void setId_detalle(Integer id_detalle) {
		this.id_detalle = id_detalle;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public Venta getVenta() {
		return venta;
	}

	public void setVenta(Venta venta) {
		this.venta = venta;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	
	public DetalleVenta(Integer id_detalle, String nombre, Integer cantidad, double precio, double subtotal,
			Venta venta, Producto producto) {
		super();
		this.id_detalle = id_detalle;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.subtotal = subtotal;
		this.venta = venta;
		this.producto = producto;
	}
	

	public DetalleVenta(String nombre, Integer cantidad, double precio, double subtotal, Venta venta,
			Producto producto) {
		super();
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.precio = precio;
		this.subtotal = subtotal;
		this.venta = venta;
		this.producto = producto;
	}

	public DetalleVenta() {
		super();
	}

	@Override
	public String toString() {
		return "DetalleVenta [id_detalle=" + id_detalle + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio="
				+ precio + ", subtotal=" + subtotal + ", venta=" + venta + ", producto=" + producto + "]";
	}
	
	
	
	
}
