package com.bolsadeideas.springboot.app.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="clientes")
@Getter
@Setter
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String apellido;
	private String email;
	
	@Column(name ="create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	private String foto;
	
	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Factura> facturas;
	
	public void addFactura(Factura factura) {
		this.facturas.add(factura);
	}

	public Cliente() {
		facturas = new ArrayList<Factura>();
	}
	
	
	
}
