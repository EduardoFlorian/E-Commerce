package com.carrito.compras.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping({"/", ""})
	public String verInicioPagina() {
		return "Inicio-Principal";
	}
	
}
