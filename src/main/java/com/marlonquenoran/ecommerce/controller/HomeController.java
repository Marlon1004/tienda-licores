package com.marlonquenoran.ecommerce.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marlonquenoran.ecommerce.model.DetalleOrden;
import com.marlonquenoran.ecommerce.model.Orden;
import com.marlonquenoran.ecommerce.model.Producto;
import com.marlonquenoran.ecommerce.model.Usuario;
import com.marlonquenoran.ecommerce.service.IDetalleOrdenService;
import com.marlonquenoran.ecommerce.service.IOrdenService;
import com.marlonquenoran.ecommerce.service.IUsuarioService;
import com.marlonquenoran.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log=LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	
	// para almacenarr los detalles de la orden
	List<DetalleOrden> detalle= new ArrayList<DetalleOrden>();
	
	// datos de la orden
	Orden orden=new Orden();
	
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		log.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
		
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
		log.info("Id enviado como parametro {}", id);
		
		Producto producto= new Producto();
		Optional<Producto> productoOptional=productoService.get(id);
		producto=productoOptional.get();
		
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
	
	
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		
		DetalleOrden detalleOrden=new DetalleOrden();
		Producto producto=new Producto();
		double sumaTotal=0;
		
		Optional<Producto> optionalPrroducto=productoService.get(id);
		log.info("Producto añadido: {}", optionalPrroducto.get());
		log.info("Cantidad: {}", cantidad);
		producto=optionalPrroducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		//validar que el producto no se añada dos veces
		Integer idProducto=producto.getId();
		boolean ingresado=detalle.stream().anyMatch(p -> p.getProducto().getId()==idProducto);
		
		if (!ingresado) {
			detalle.add(detalleOrden);
		}
		
		
		
		sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	
	// metodo eliminar un producto del carrito de compras
	
	@GetMapping("/delete/cart/{id}")
	public String deleteCar(@PathVariable Integer id, Model model) {
		
		//lista nueva de productos
		List<DetalleOrden> ordenesNueva= new ArrayList<DetalleOrden>();
		
		for (DetalleOrden detalleOrden : detalle) {
			
			if (detalleOrden.getProducto().getId()!=id) {
				
				ordenesNueva.add(detalleOrden);
			}
			
		}
		
		//poner la nueva lista con los productos restantes
		detalle=ordenesNueva;
		
		double sumaTotal=0;
		
		sumaTotal=detalle.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);
		
		
		return "usuario/carrito";
	}
	
	
	
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);
		
		return "/usuario/carrito";
	}
	
	
	
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		model.addAttribute("cart", detalle);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", usuario);
		
		return "usuario/resumenorden";
	}
	
	
	
	//guardarr la orden
	
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//usuario
		Usuario usuario= usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		orden.setUsuario(usuario);
		ordenService.save(orden);
		
		//guardar detalles
		
		for(DetalleOrden dt:detalle)
		{
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		
		
		///limpieza de lista y orden
		orden=new Orden();
		detalle.clear();
		
		return "redirect:/";
	}
	
	
	
	@PostMapping("/search")
	public String serachProducto(@RequestParam String nombre, Model model) {
		
		log.info("Nombre del producto: {}", nombre);
		
		List<Producto> productos=productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		
		return "usuario/home";
	}
	
	

}
