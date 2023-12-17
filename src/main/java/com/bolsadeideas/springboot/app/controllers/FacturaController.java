package com.bolsadeideas.springboot.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dto.ClienteDTO;
import com.bolsadeideas.springboot.app.models.dto.FacturaDTO;
import com.bolsadeideas.springboot.app.models.dto.ItemFacturaDTO;
import com.bolsadeideas.springboot.app.models.dto.ProductoDTO;
import com.bolsadeideas.springboot.app.models.services.IClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/factura")
@SessionAttributes("facturaDTO")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable Long clienteId, Map<String,Object> model, RedirectAttributes flash) {
		
		ClienteDTO cliente= clienteService.findOne(clienteId);
		
		if(cliente ==null) {
			flash.addFlashAttribute("error", "El cliente no existe");
			return "redirect:/listar";
		}
		
		FacturaDTO facturaDTO = new FacturaDTO();
		facturaDTO.setClienteDTO(cliente);
		
		model.put("facturaDTO", facturaDTO);
		model.put("titulo", "Crear factura");
		
		return "factura/form";
	}
	
	@GetMapping(value =  "/cargar-productos/{term}", produces = "application/json")
	public @ResponseBody List<ProductoDTO> cargarProductos(@PathVariable String term){
		return clienteService.findByNombre(term);
	}
	
	@PostMapping("/form")
	public String guardar(@Valid FacturaDTO facturaDTO, BindingResult result, Model model,
			@RequestParam(name = "item_id[]" , required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]" , required = false) Integer[] cantidadId,
			RedirectAttributes flash,
			SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Crear factura");
			return "factura/form";
		}
		
		if(itemId==null || itemId.length == 0 ) {
			model.addAttribute("titulo", "Crear factura");
			model.addAttribute("error", "LA factura debe tener lineas");
			return "factura/form";
		}
		
		for(int i = 0 ; i<itemId.length ;i++) {
			ProductoDTO productoDTO = clienteService.findProductoPorId(itemId[i]);
			ItemFacturaDTO linea = new ItemFacturaDTO();
			linea.setCantidad(cantidadId[i]);
			linea.setProducto(productoDTO);
			
			facturaDTO.addItemFactura(linea);
		}
		
		clienteService.saveFactura(facturaDTO);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura creada con exito!");
		
		return "redirect:/ver/" + facturaDTO.getClienteDTO().getId();
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model model, RedirectAttributes flash) {
		FacturaDTO facturaDTO = clienteService.findFacturaById(id);
		
		if(facturaDTO == null) {
			flash.addFlashAttribute("error", "La factura no existe");
			return "redirect:/listar";
		}
		
		model.addAttribute("facturaDTO", facturaDTO);
		model.addAttribute("titulo", "Factura: ".concat(facturaDTO.getDescripcion()));
		
		return "factura/ver";
	}
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		
		FacturaDTO facturaDTO = clienteService.findFacturaById(id);
		
		if(facturaDTO!=null) {
			clienteService.deleteFactura(id);
			flash.addFlashAttribute("success", "factura eliminada con éxito");
			return "redirect:/ver/" + facturaDTO.getClienteDTO().getId();
		}
		
		flash.addFlashAttribute("error", "La factura no existe, no se pudo eliminar");
		return "redirect:/listar";
	}

}
