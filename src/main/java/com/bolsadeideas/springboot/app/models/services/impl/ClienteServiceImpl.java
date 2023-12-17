package com.bolsadeideas.springboot.app.models.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.dto.ClienteDTO;
import com.bolsadeideas.springboot.app.models.dto.FacturaDTO;
import com.bolsadeideas.springboot.app.models.dto.ProductoDTO;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;
import com.bolsadeideas.springboot.app.models.services.IClienteService;


@Service
public class ClienteServiceImpl implements IClienteService {
	
	private IClienteDao clienteDao;
	private ModelMapper modelMapper;
	private IProductoDao productoDao;
	private IFacturaDao facturaDao;
	
	public ClienteServiceImpl (IClienteDao clienteDao, ModelMapper modelMapper, IProductoDao productoDao,IFacturaDao facturaDao) {
		this.clienteDao=clienteDao;
		this.modelMapper=modelMapper;
		this.productoDao=productoDao;
		this.facturaDao=facturaDao;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ClienteDTO> findAll() {
		List<Cliente> result = (List<Cliente>) clienteDao.findAll();
		//List<ClienteDTO> resultDTO= result.stream().map(c-> modelMapper.map(c, ClienteDTO.class)).toList();
		
		return result.stream().map(cliente-> {
					ClienteDTO clienteDTO= modelMapper.map(cliente, ClienteDTO.class);
					return clienteDTO;
				}).toList();
	}

	@Override
	@Transactional
	public void save(ClienteDTO clienteDTO) {
		clienteDao.save(modelMapper.map(clienteDTO, Cliente.class));
	}

	@Override
	@Transactional(readOnly = true)
	public ClienteDTO findOne(Long id) {
		Cliente cliente = clienteDao.findById(id).orElse(null);
		return cliente !=null ? modelMapper.map(cliente, ClienteDTO.class): null ;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ClienteDTO> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Cliente> result=clienteDao.findAll(pageable);
		
		return result.map(cliente-> {
			ClienteDTO clienteDTO= modelMapper.map(cliente, ClienteDTO.class);
			return clienteDTO;
		});
	}

	@Override
	public List<ProductoDTO> findByNombre(String term) {
		
		List<Producto> productos = productoDao.buscarPorNombre(term);

        // Utiliza ModelMapper para convertir la lista de Producto a ProductoDTO
        List<ProductoDTO> productoDTOs = productos.stream()
                .map(producto -> modelMapper.map(producto, ProductoDTO.class)).toList();
        
		return productoDTOs;
	}

	@Override
	@Transactional
	public void saveFactura(FacturaDTO facturaDTO) {
		// TODO Auto-generated method stub
		facturaDao.save(modelMapper.map(facturaDTO, Factura.class));
	}

	@Override
	@Transactional(readOnly = true)
	public ProductoDTO findProductoPorId(Long id) {
		
		Producto producto = productoDao.findById(id).orElse(null);
		ProductoDTO result= null;
		if(producto!=null) {
			result= modelMapper.map(producto, ProductoDTO.class);
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public FacturaDTO findFacturaById(Long id) {
		Factura factura= facturaDao.findById(id).orElse(null);
		
		FacturaDTO result= null;
		if(factura!=null) {
			result= modelMapper.map(factura, FacturaDTO.class);
			result.setClienteDTO(modelMapper.map(factura.getCliente(), ClienteDTO.class));
		}
		return result;
		
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		
		facturaDao.deleteById(id);
		facturaDao.flush();
		
	}
	
}
