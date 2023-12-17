package com.bolsadeideas.springboot.app.models.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteDTO  implements Serializable{

	private Long id;
	@NotEmpty
	private String nombre;
	@NotEmpty
	private String apellido;
	@NotEmpty
	@Email
	private String email;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private Date createAt;
	private String foto;
	
	private List<FacturaDTO> facturas;
	
	@Override
	public String toString() {
		return  nombre + " " + apellido ;
	}

	private static final long serialVersionUID = 1L;

}
