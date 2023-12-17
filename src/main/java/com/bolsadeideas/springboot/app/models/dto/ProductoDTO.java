package com.bolsadeideas.springboot.app.models.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDTO implements Serializable{

	private Long id;
	
	private String nombre;
	
	private Double precio;
	
	private Date createAt;
	
	private static final long serialVersionUID = 1L;

}
