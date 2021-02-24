package br.com.estudospring.clientes.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
public class Servico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	
	@Column(nullable = false, length = 150)
	@Getter
	@Setter
	private String descricao;
	
	@Column
	@Getter
	@Setter
	private BigDecimal valor;
	
	@ManyToOne
	@JoinColumn(name = "id_cliente")
	@Getter
	@Setter
	private Cliente cliente;
}
