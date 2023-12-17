package com.bolsadeideas.springboot.app.models.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaDTO  implements Serializable{

	private Long id;
	@NotEmpty
	private String descripcion;
	private String observacion;
	private Date createAt;
	private ClienteDTO clienteDTO;
	private List<ItemFacturaDTO> items;
	
	public Double getTotal() {
		return items.stream().mapToDouble(ItemFacturaDTO :: calcularImporte).sum();
	}
	
	public FacturaDTO() {
		this.items = new ArrayList<ItemFacturaDTO>();
	}
	
	public void addItemFactura(ItemFacturaDTO item) {
		this.items.add(item);
	}
	private static final long serialVersionUID = 1L;

}
