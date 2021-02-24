package br.com.estudospring.clientes.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;

	@Column(nullable = false, length = 150)
	@Getter
	@Setter
	private String nome;

	@Column(nullable = false, length = 11)
	@Getter
	@Setter
	private String cpf;

	@Column(name = "data_cadastro")
	@Getter
	@Setter
	private LocalDate dataCadastro;
	
	@PrePersist
	public void  prePersiste() {
		setDataCadastro(LocalDate.now());
	}
}
