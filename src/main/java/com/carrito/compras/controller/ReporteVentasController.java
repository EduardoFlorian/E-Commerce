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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.carrito.compras.model.Venta;
import com.carrito.compras.repository.IVentaRepository;

@Controller
@RequestMapping("/reporte")
public class ReporteVentasController {
	
	@Autowired
	private IVentaRepository ventaRepo;
	
	@GetMapping("/VentasRealizadas")
	public String verReporteDeVentas(@RequestParam Map<String, Object> params, Model model) {
		
		int page = params.get("page") != null ? (Integer.valueOf(params.get("page").toString()) - 1) : 0;
		
		PageRequest pageRequest = PageRequest.of(page, 4, Sort.by("idVenta").descending());
		
		Page<Venta> pageVenta = ventaRepo.findAll(pageRequest);
		
		int totalPage = pageVenta.getTotalPages();
		if(totalPage > 0) {
			List<Integer> pages = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
			model.addAttribute("pages", pages);
		}
		
		model.addAttribute("listadoVentas", pageVenta.getContent());
		model.addAttribute("current", page + 1);
		model.addAttribute("next", page + 2);
		model.addAttribute("prev", page);
		model.addAttribute("last", totalPage);
		
		
		return "venta/Reporte-Ventas";
	}
	
}
