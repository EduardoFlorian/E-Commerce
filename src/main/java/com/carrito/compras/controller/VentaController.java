package com.carrito.compras.controller;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carrito.compras.model.DetalleVenta;
import com.carrito.compras.model.Producto;
import com.carrito.compras.model.ProductoCarrito;
import com.carrito.compras.model.Venta;
import com.carrito.compras.repository.IDetalleVentaRepository;
import com.carrito.compras.repository.IProductoRepository;
import com.carrito.compras.repository.IVentaRepository;

@Controller
@RequestMapping("/ventas")
public class VentaController {
	
	//METODOS
	private ArrayList<ProductoCarrito> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoCarrito> carrito = (ArrayList<ProductoCarrito>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }
	
	private void guardarCarrito(ArrayList<ProductoCarrito> carrito, HttpServletRequest request) {
	    request.getSession().setAttribute("carrito", carrito);
	}
	
	private void limpiarCarrito(HttpServletRequest request) {
	    this.guardarCarrito(new ArrayList<>(), request);
	}
	
	@Autowired
	private IProductoRepository productoRepo;
	
	@Autowired
	private IVentaRepository ventaRepo;
	
	@Autowired
	private IDetalleVentaRepository detalleVentaRepo;
	
	
	@GetMapping("/ver/ordenCompra")
	public String verOrdenDeCompra(HttpServletRequest request, Model model) {
		
		//Obtener los productos del carrito
		ArrayList<ProductoCarrito> auxiliar = obtenerCarrito(request);
		
		//Obtener fecha actual
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
		String fechaActual = formatoFecha.format(new Date());
		
		//Mandar la lista de productos del carrito en un atributo
		model.addAttribute("ListaProductosCarrito", auxiliar);
		
		//Mandar fecha en atributo
		model.addAttribute("fechaActual", fechaActual);
		
		return "venta/Orden-Compra";
	}
	
	@GetMapping("/cancelar/ordenCompra")
	public String cancelarOrdenCompra(HttpServletRequest request, Model model, RedirectAttributes redirect) {
		
		//Limpiamos el carrito
		limpiarCarrito(request);
		
		//Enviamos un mensaje diciendo que se cancelo nuestra compra
		redirect.addFlashAttribute("compraCancelada", "Se canceló tu compra y se vació tu carrito");
		
		return "redirect:/compras/productos/filtro";
	}
	
	@PostMapping("/registrar/ordenCompra")
	public String registrarVenta(HttpServletRequest request, RedirectAttributes redirect, Model model) {
		
		//Obtener los productos del carrito
		ArrayList<ProductoCarrito> auxiliar = obtenerCarrito(request);
		
		//Obtener fecha actual
		LocalDate fechaActual = LocalDate.now();
		
		//Total pago en la venta
		Double totalPagar = 0.0;
		
		Venta venta = new Venta();
		
		venta.setFechaVenta(fechaActual);
		
		for(ProductoCarrito carrito : auxiliar) {
			totalPagar += carrito.getTotalPagar();
		}
		
		venta.setPagoTotalVenta(totalPagar);
		
		//Grabar VENTA
		ventaRepo.save(venta);
		
		
		//Recorremos el carrito paara pdoer grabar los detalles de la venta
		for(ProductoCarrito carrito : auxiliar) {
			Producto productoEncontrado = productoRepo.getById(carrito.getIdProducto());
			
			//Sub total de cada detalle
			double subtotal =0.0;
			subtotal+= carrito.getTotalPagar();
			
			//Restamos el stock del producto que se compro
			if(productoEncontrado!=null) {
				productoEncontrado.restarExistencia(carrito.getCantidad());
			}
			
			//"Actualizamos" el producto comprado ya que tendra un nuevo valor de stock 
			productoRepo.save(productoEncontrado);
			
			//Creamos un nuevo detalle venta con los elementos del carrito
			DetalleVenta dt = new DetalleVenta(
					carrito.getNombre(), carrito.getCantidad(), 
					carrito.getPrecio(), subtotal ,
					venta, productoEncontrado);
			//Grabamos detalle-venta
			detalleVentaRepo.save(dt);
			
		}
		
		this.limpiarCarrito(request);
		
		redirect.addFlashAttribute("msgVentaExitosa", "Venta realizada correctamente");
		
		return "redirect:/reporte/VentasRealizadas";
	}
	
}
