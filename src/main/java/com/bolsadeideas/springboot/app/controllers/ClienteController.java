package com.bolsadeideas.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.dto.ClienteDTO;
import com.bolsadeideas.springboot.app.models.services.IClienteService;
import com.bolsadeideas.springboot.app.models.services.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("clienteDTO")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	@Autowired
	private IUploadFileService fileService;
	
	@GetMapping("/listar")
	public String Listar(@RequestParam(name="page", defaultValue = "0") int page, Model model) {
		
		Pageable pageRequest = PageRequest.of(page,5);
		Page<ClienteDTO> clientes= clienteService.findAll(pageRequest);
		PageRender<ClienteDTO> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
		
	}
	
	@RequestMapping(value="/form")
	public String crear(Map<String, Object> model) {
		
		ClienteDTO clienteDTO = new ClienteDTO();
		
		model.put("titulo", "Formulario de clientes");
		model.put("clienteDTO", clienteDTO);
		return "form";
		
	}
	
	@PostMapping("/form")
	public String guardar(@Valid ClienteDTO clienteDTO, BindingResult result, Model model, 
			@RequestParam("file") MultipartFile foto,
			RedirectAttributes flash,SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de clientes");
			return "form";
		}
		
		if(!foto.isEmpty()) {
			
			if(clienteDTO.getId() !=null && clienteDTO.getId() > 0 
					&& clienteDTO.getFoto()!=null && clienteDTO.getFoto().length() >0 ) {
				
				fileService.delete(clienteDTO.getFoto());
			}
			
			String uniqueStringFile = null;
			try {
				uniqueStringFile = fileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Has subido correctamente la imagen ' " + uniqueStringFile + " '");
			clienteDTO.setFoto(uniqueStringFile);
		}
		
		String mensajeFlash = clienteDTO.getId() != null ?  "Cliente editado con éxito!" : "Cliente creado con éxito!";
		clienteService.save(clienteDTO);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		ClienteDTO clienteDTO = null;
		
		if(id>0) {
			clienteDTO= clienteService.findOne(id);
			if(clienteDTO==null) {
				flash.addFlashAttribute("error", "El ID de cliente no existe en la BD!");
				return "redirect:/listar";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		
		model.put("clienteDTO", clienteDTO);
		model.put("titulo", "Editar cliente");
		return "form";
	}
	
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		
		if(id>0) {
			ClienteDTO clienteDTO = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
			
				if(fileService.delete(clienteDTO.getFoto())) {
					flash.addFlashAttribute("info", "Foto" + clienteDTO.getFoto() + "eliminada con éxito!");
				}
			
		}
		return "redirect:/listar";
	}
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		ClienteDTO clienteDTO = clienteService.findOne(id);
		
		if(clienteDTO == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la BD!");
			return "redirect:/listar";
		}
		
		model.put("clienteDTO", clienteDTO);
		model.put("titulo", "Detalle del cliente: " + clienteDTO.getNombre());
		return "ver";
	}
	
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto( @PathVariable String filename){
		
		Resource recurso= null;
		try {
			recurso = fileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ recurso.getFilename()+"\" ")
				.body(recurso);
	}
}
