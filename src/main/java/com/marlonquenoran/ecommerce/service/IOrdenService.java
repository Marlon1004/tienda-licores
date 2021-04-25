package com.marlonquenoran.ecommerce.service;

import java.util.List;

import com.marlonquenoran.ecommerce.model.Orden;
import com.marlonquenoran.ecommerce.model.Usuario;

public interface IOrdenService {
	
	List<Orden> findAll();
	
	Orden save(Orden orden);
	String generarNumeroOrden();
	
	List<Orden> findByUsuario(Usuario usuario);

}
