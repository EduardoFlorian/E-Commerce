package com.carrito.compras.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.carrito.compras.model.Producto;
import com.carrito.compras.repository.ICategoriaRepository;
import com.carrito.compras.repository.IProductoRepository;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	//Repositorios
	@Autowired
	private IProductoRepository ProductoRepo;
	@Autowired
	private ICategoriaRepository CategoriaRepo;
	
	@GetMapping({"/listado"})
	public String verProductos(@RequestParam Map<String, Object> params, Model model) {

		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		
		PageRequest pageRequest = PageRequest.of(page, 10);
		
		Page<Producto> pageProducto = ProductoRepo.findAll(pageRequest);
		
		int totalPage = pageProducto.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		
		model.addAttribute("listadoProductos", pageProducto.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		model.addAttribute("last", totalPage);
		
		
		return "producto/Productos";
	}
	
	@GetMapping("/agregar")
	public String verFormularioParaAgregar(Model model) {
		
		//Objeto Producto
		model.addAttribute("producto", new Producto());
		
		//Combo de categorias
		model.addAttribute("listadoCategorias", CategoriaRepo.findAll(Sort.by("nombreCategoria")));
		
		
		return "producto/Agregar-Producto";
	}
	
	@PostMapping("/agregar")
	public String registrarProducto(@ModelAttribute @Validated Producto producto,BindingResult result, RedirectAttributes redirect, Model model) {
		
		if(result.hasErrors()) {
			//Combo de categorias
			model.addAttribute("listadoCategorias", CategoriaRepo.findAll(Sort.by("nombreCategoria")));
			
			return "producto/Agregar-Producto";
		}
		
		ProductoRepo.save(producto);
		
		redirect.addFlashAttribute("msgExito", "Producto registrado de manera correcta");
		
		return "redirect:/productos/listado";
	}
	
	@GetMapping("/editar/{id}")
	public String mostrarProducto(@PathVariable Integer id, Model model) {
		
		//Obtenemos el producto por id
		Producto productoEncontrado = ProductoRepo.getById(id);
		
		//Al objeto "producto" que debemos enviar al formulario, lo enviamos con los valores del
		//producto encontrado
		model.addAttribute("producto", productoEncontrado);
		
		//Combo de categorias
		model.addAttribute("listadoCategorias", CategoriaRepo.findAll(Sort.by("nombreCategoria")));		
		
		return "producto/Editar-Producto";
	}
	
	@PostMapping("/editar/{id}")
	public String editarProducto(@PathVariable Integer id, @ModelAttribute @Validated Producto producto, BindingResult result, RedirectAttributes redirect, Model model) {
		
		if(result.hasErrors()) {
			//Combo de categorias
			model.addAttribute("listadoCategorias", CategoriaRepo.findAll(Sort.by("nombreCategoria")));
			
			return "producto/Agregar-Producto";
		}
		
		//Obtenemos el producto por id
		Producto productoEncontrado = ProductoRepo.getById(id);
		
		productoEncontrado.setNombre(producto.getNombre());
		productoEncontrado.setIdCategoria(producto.getIdCategoria());
		productoEncontrado.setPrecio(producto.getPrecio());
		productoEncontrado.setStock(producto.getStock());
		
		
		ProductoRepo.save(productoEncontrado);
		
		redirect.addFlashAttribute("msgExito", "Producto actualizado de manera correcta");
		
		return "redirect:/productos/listado";
	}
	
	@PostMapping("/eliminar/{id}")
	public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirect) {
		
		
		try {
			//Eliminamos producto por id
			ProductoRepo.deleteById(id);
			
			redirect.addFlashAttribute("msgExito", "Producto eliminado de manera correcta");
			
		} catch (Exception e) {

			redirect.addFlashAttribute("msgFallo", "No pudo eliminarse, porque hay ventas relacionadas a este producto");
		}
		
		return "redirect:/productos/listado";
	}
}
