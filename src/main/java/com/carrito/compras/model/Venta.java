package com.carrito.compras.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="tb_Venta")
public class Venta {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_venta")
	private Integer idVenta;
	
	@Column(name="fec_venta")
	private LocalDate fechaVenta;
	
	@Column(name="total_venta")
	private double pagoTotalVenta;
	
	@OneToMany(mappedBy = "venta")
	List<DetalleVenta> detalleVenta;

	public Integer getIdVenta() {
		return idVenta;
	}

	public void setIdVenta(Integer idVenta) {
		this.idVenta = idVenta;
	}

	public LocalDate getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(LocalDate fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public double getPagoTotalVenta() {
		return pagoTotalVenta;
	}

	public void setPagoTotalVenta(double pagoTotalVenta) {
		this.pagoTotalVenta = pagoTotalVenta;
	}

	public List<DetalleVenta> getDetalleVenta() {
		return detalleVenta;
	}

	public void setDetalleVenta(List<DetalleVenta> detalleVenta) {
		this.detalleVenta = detalleVenta;
	}

	public Venta(Integer idVenta, LocalDate fechaVenta, double pagoTotalVenta, List<DetalleVenta> detalleVenta) {
		super();
		this.idVenta = idVenta;
		this.fechaVenta = fechaVenta;
		this.pagoTotalVenta = pagoTotalVenta;
		this.detalleVenta = detalleVenta;
	}

	public Venta() {
		super();
	}

	@Override
	public String toString() {
		return "Venta [idVenta=" + idVenta + ", fechaVenta=" + fechaVenta + ", pagoTotalVenta=" + pagoTotalVenta
				+ ", detalleVenta=" + detalleVenta + "]";
	}
	
	
}
