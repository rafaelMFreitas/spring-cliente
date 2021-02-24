package br.com.estudospring.clientes.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.estudospring.clientes.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
