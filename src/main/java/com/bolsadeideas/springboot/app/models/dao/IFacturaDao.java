package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bolsadeideas.springboot.app.models.entity.Factura;

@Repository
public interface IFacturaDao extends JpaRepository<Factura, Long> {
	
}
