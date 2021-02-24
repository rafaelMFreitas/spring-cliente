package br.com.estudospring.clientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.estudospring.clientes.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Integer>{

}
