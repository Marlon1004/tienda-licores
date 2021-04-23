package com.marlonquenoran.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marlonquenoran.ecommerce.model.Orden;
import com.marlonquenoran.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {
	
	@Autowired
	private IOrdenRepository ordenRespository;

	@Override
	public Orden save(Orden orden) {
		
		return ordenRespository.save(orden);
	}

}
