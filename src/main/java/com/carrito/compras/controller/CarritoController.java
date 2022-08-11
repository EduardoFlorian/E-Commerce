package com.carrito.compras.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carrito.compras.model.Producto;
import com.carrito.compras.model.ProductoCarrito;
import com.carrito.compras.repository.ICategoriaRepository;
import com.carrito.compras.repository.IProductoRepository;

@Controller
@RequestMapping("/compras")
public class CarritoController {

	@Autowired
	private IProductoRepository ProductoRepo;
	
	@Autowired
	private ICategoriaRepository CategoriaRepo;
	
	
	//METODOS
	//Metodo para obtener el carrito de compras
	private ArrayList<ProductoCarrito> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoCarrito> carrito = (ArrayList<ProductoCarrito>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }
	//Metodo para guardar productos en el carrito de compras
	private void guardarCarrito(ArrayList<ProductoCarrito> carrito, HttpServletRequest request) {
	    request.getSession().setAttribute("carrito", carrito);
	}
	
	
	@GetMapping("/productos/filtro")
	public String verCategoriasParaFiltrar(Model model) {
		
		//ComboCategoria
		model.addAttribute("listadoCategorias", CategoriaRepo.findAll());
		
		//ListadoDeProductosTotales
		//model.addAttribute("listadoProductosPorCategoria", ProductoRepo.findAll(Sort.by("nombre")));
		
		return "carrito/Seleccionar-Producto";
	}
	
	@PostMapping("/productos/disponibles")
	public String verProdcutosDisponiblesPorCategoria(Model model, @RequestParam (name="idCategoria") int cate) {
		
		//ComboCategoria
		model.addAttribute("listadoCategorias", CategoriaRepo.findAll());
		if(cate>0) {
		//ListadoPorFiltroCategoria
		model.addAttribute("listadoProductosPorCategoria", ProductoRepo.findByIdCategoria(cate));
		}
		else {
		model.addAttribute("msgSeleccionarCategoria",  "Debe seleccionar una categoria");
		}
		
		return "carrito/Seleccionar-Producto"; 
	}
	
	@GetMapping("/agregar/carrito/{idProducto}")
	public String agregarProductoAlCarrito(@PathVariable Integer idProducto, Model model) {
		
		//Obtner producto por id
		Producto productoSeleccionado = ProductoRepo.getById(idProducto);
		
		//Mandar objeto producto encontrado
		model.addAttribute("producto", productoSeleccionado);
		
		//ComboCategoria
		model.addAttribute("listadoCategorias", CategoriaRepo.findAll());
		
		return "carrito/Agregar-Carrito";
	}
	
	@PostMapping("/agregar/carrito")
	public String agregarProductoEnElCarrito(@RequestParam(name="cantidad") Integer cantidad, @ModelAttribute Producto producto, Model model, HttpServletRequest request, RedirectAttributes redirect) {
		
		//Obtenemos carrito
		ArrayList<ProductoCarrito> auxiliar = this.obtenerCarrito(request);
		
		//Obtenemos producto por id del producto del formulario
		Producto productoEncontrado = ProductoRepo.getById(producto.getIdProducto());
		
		
		//Recorremos el carrito para verificar que no agreguemos un producto que ya se encuentra
		//en el carrito
		for(ProductoCarrito aux : auxiliar) {
			if(aux.getIdProducto() == productoEncontrado.getIdProducto()) {
				redirect.addFlashAttribute("msgCarritoDuplicado", "Este Producto ya se encuentra en tu carrito");

				return "redirect:/compras/productos/filtro";
			}
		}
		
		//Si la cantidad que ingresamos del producto supera nuestro stock, enviamos un mensaje
		//y no agregamos el producto al carrito
		if(cantidad > productoEncontrado.getStock()) {
			
			//Redireccionamos un mensaje diciendo que la cantidad ingresada supera el stock del producto a agregar
			redirect.addFlashAttribute("msgExcesoCantidad", "La cantidad ingresada supera nuestro stock");
			
			//Creamos una variable que contenga el id del producto del producto a agregar (Es decir el producto encontrado por id)
			int idProd = productoEncontrado.getIdProducto();
			
			//Redireccionamos a la vista donde se encuentra el Producto a agregar y le concatenamo el id del producto para que no se pierdan los
			//datos de la vista
			return "redirect:/compras/agregar/carrito/"+idProd;
		}
		
		//Si el producto no se repite en el carrito y la cantidad ingresada es correcta, añadimos
		//el producto al carrito
		auxiliar.add( new ProductoCarrito(
				productoEncontrado.getIdProducto(), 
				productoEncontrado.getNombre(),
				productoEncontrado.getPrecio(),
				productoEncontrado.getIdCategoria(),
				productoEncontrado.getStock(), cantidad));
		
		
		this.guardarCarrito(auxiliar, request);
		
		redirect.addFlashAttribute("msgCarritoAgregado", "El Producto se ha agregado de manera correcta en tu carrito");
				
		return "redirect:/compras/productos/filtro"; 
	}
	
	@GetMapping("/ver/Carrito")
	public String verProductosEnCarrito(HttpServletRequest request, RedirectAttributes redirect, Model model) {
		//Obtenemos carrito y lo guardamos en una variable tipo ArrayList
		ArrayList<ProductoCarrito> auxiliar = this.obtenerCarrito(request);
		
		//Si el carrito es vacio que nos retorne a Seleccionar Producto
		if(auxiliar.size()==0) {
			redirect.addFlashAttribute("msgCarritoVacio", "Tu carrito se encuentra vacio");
			
			return "redirect:/compras/productos/filtro";
		}
		
		//Declaramos una variable local que va contener el pago total del carrito
		double pagoTotal = 0;
		
		for(ProductoCarrito productoCarrito : auxiliar) {
			//la variable declarada obtendra los subtotales de cada elemento del carrito
			//y los sumara
			pagoTotal += productoCarrito.getTotalPagar();
		}
		
		//Enviamos un atributo con el valor total a pagar por el carrito
		model.addAttribute("PagoTotal", pagoTotal);
		
		//Enviamos los productos del carrito
		model.addAttribute("ListaCarrito", auxiliar);
		
		return "carrito/Carrito";
	}
	
	@PostMapping("/eliminar/ProductoCarrito/{indice}")
	public String quitarProductoDelCarrito(@PathVariable int indice, HttpServletRequest request) {
		
		//Obtenemos carrito y lo guardamos en una variable tipo ArrayList
		ArrayList<ProductoCarrito> auxiliar = this.obtenerCarrito(request);
		
		//Removemos un elemento de un arraylist mediante su indice
		//(Cada elemento de un array cuenta con un indice )
		
		auxiliar.remove(indice);
		
		this.guardarCarrito(auxiliar, request);
		
		return "redirect:/compras/ver/Carrito";
	}
	
}
