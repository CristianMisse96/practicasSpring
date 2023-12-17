package com.bolsadeideas.springboot.app.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bolsadeideas.springboot.app.models.dto.ClienteDTO;
import com.bolsadeideas.springboot.app.models.dto.FacturaDTO;
import com.bolsadeideas.springboot.app.models.dto.ProductoDTO;

public interface IClienteService {

	public List<ClienteDTO> findAll();
	
	public Page<ClienteDTO> findAll(Pageable pageable);
	
	public void save(ClienteDTO clienteDTO);
	
	public ClienteDTO findOne(Long id);
	
	public void delete(Long id);
	
	public List<ProductoDTO> findByNombre(String term);
	
	public void saveFactura(FacturaDTO facturaDTO);
	
	public ProductoDTO findProductoPorId(Long id);
	
	public FacturaDTO findFacturaById(Long id);
	
	public void deleteFactura(Long id);
}
