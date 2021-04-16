package com.marlonquenoran.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marlonquenoran.ecommerce.model.Producto;
import com.marlonquenoran.ecommerce.model.Usuario;
import com.marlonquenoran.ecommerce.service.ProductoService;

import org.slf4j.*;

@Controller
@RequestMapping("/productos")
public class productoController {

	private final Logger LOGGER= org.slf4j.LoggerFactory.getLogger(productoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	// Para enviar a base de datos
	
	@PostMapping("/save")
	public String save(Producto producto) {
		
		LOGGER.info("Este es el objeto producto {}",producto);
		
		Usuario u= new Usuario(1, "", "", "", "", 0, "", "");
		producto.setUsuario(u);
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
}
