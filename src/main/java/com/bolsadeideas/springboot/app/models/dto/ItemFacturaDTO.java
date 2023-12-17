package com.bolsadeideas.springboot.app.models.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFacturaDTO implements Serializable{
	
	private Integer cantidad;
	private ProductoDTO producto;
	
	public Double calcularImporte() {
		return cantidad.doubleValue() * producto.getPrecio();
	}
	
	private static final long serialVersionUID = 1L;

}
