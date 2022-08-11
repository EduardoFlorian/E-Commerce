package com.carrito.compras.model;

public class ProductoCarrito extends Producto{
	
	
	private Integer cantidad;
	
	
	public ProductoCarrito(Integer idProducto, String nombre, Double precio, Integer idCategoria, Integer stock,Integer cantidad) {
		super(idProducto,nombre,precio,idCategoria,stock);
		this.cantidad = cantidad;
	}
	

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	//Para obtener el total a Pagar
	public Double getTotalPagar() {
		return this.cantidad * this.getPrecio();
	}
	
}
