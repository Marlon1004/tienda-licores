package com.marlonquenoran.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Orden> findAll() {
		
		return ordenRespository.findAll();
	}
	
	
	//0000010
	public String generarNumeroOrden() {
		int numero=0;
		String numeroConcatenado="";
		
		List<Orden> ordenes=findAll();
		List<Integer> numeros=new ArrayList<Integer>();
		
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if (ordenes.isEmpty()) {
			numero=1;
		}
		else {
			numero=numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		
		if (numero<10) { //0000000001  0000000010   000000100
			numeroConcatenado="000000000"+ String.valueOf(numero);
		}
		else if(numero<100){
			numeroConcatenado="00000000"+ String.valueOf(numero);
		}
		else if(numero<1000){
			numeroConcatenado="0000000"+ String.valueOf(numero);
		}
		else if(numero<10000){
			numeroConcatenado="000000"+ String.valueOf(numero);
		}
		
		return numeroConcatenado;
	}

}
