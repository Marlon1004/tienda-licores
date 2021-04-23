package com.marlonquenoran.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marlonquenoran.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService usuarioService;
	
	
	// /usuario/registro
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}

}
