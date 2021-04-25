package com.marlonquenoran.ecommerce.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.marlonquenoran.ecommerce.model.Usuario;

@Service
public interface IUsuarioService {

	Optional<Usuario> findById(Integer id);
	
	Usuario save(Usuario usuario);
	
	Optional<Usuario> finByEmail(String email);
	
}
