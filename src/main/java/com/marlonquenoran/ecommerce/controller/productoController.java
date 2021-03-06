package com.marlonquenoran.ecommerce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.marlonquenoran.ecommerce.model.Producto;
import com.marlonquenoran.ecommerce.model.Usuario;
import com.marlonquenoran.ecommerce.service.IUsuarioService;
import com.marlonquenoran.ecommerce.service.ProductoService;
import com.marlonquenoran.ecommerce.service.UploadFileService;
import com.marlonquenoran.ecommerce.service.UsuarioServiceImpl;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.*;

@Controller
@RequestMapping("/productos")
public class productoController {

	private final Logger LOGGER= org.slf4j.LoggerFactory.getLogger(productoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private UploadFileService upload;
	
	
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
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		
		LOGGER.info("Este es el objeto producto {}",producto);
		
		Usuario u= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(u);
		
		
		//imagen
		if (producto.getId()==null) { //cuando se crea un producto
			
			String nombreImagen=upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		else {    
			
		}
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id,Model model) {
		
		Producto producto= new Producto();
		Optional<Producto> optionalProducto=productoService.get(id);
		producto=optionalProducto.get();
		
		LOGGER.info("Producto Buscado: {}",producto);
		model.addAttribute("producto", producto);
		
		return "productos/edit";
		
	}
	
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p=new Producto();
		p=productoService.get(producto.getId()).get();
		
		
		if (file.isEmpty()) {  // editamos el producto pero no cambiamos la imagen
			
			producto.setImagen(p.getImagen());
		}
		else {   // cuando se edita tambien la imagen
			
			
			if (!p.getImagen().equals("default.jpg")) {
				upload.deleteimage(p.getImagen());
			}
			
			String nombreImagen=upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
		
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		
		// Eliminar cuando la imagen no sea por defecto
		
		Producto p= new Producto();
		p=productoService.get(id).get();
		
		if (!p.getImagen().equals("default.jpg")) {
			upload.deleteimage(p.getImagen());
		}
		
		productoService.delete(id);
		return "redirect:/productos";
	}
	
	
}
