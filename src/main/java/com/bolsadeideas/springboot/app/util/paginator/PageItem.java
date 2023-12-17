package com.bolsadeideas.springboot.app.util.paginator;

import lombok.Getter;

@Getter
public class PageItem {
	
	private int numero;
	private boolean isActual;
	
	public PageItem(int numero, boolean isActual) {
		this.numero = numero;
		this.isActual = isActual;
	}
	
	
}
