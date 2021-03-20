package br.com.estudospring.clientes.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.estudospring.clientes.model.Cliente;
import br.com.estudospring.clientes.model.ServicoPrestado;
import br.com.estudospring.clientes.model.repository.ClienteRepository;
import br.com.estudospring.clientes.model.repository.ServicoPrestadoRepository;
import br.com.estudospring.clientes.rest.dto.ServicoPrestadoDTO;
import br.com.estudospring.clientes.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servicos-prestados")
//@RequiredArgsConstructor - cria um construtor com os atributos obrigatorios
public class ServicoPrestadoController {

	private final ServicoPrestadoRepository repository;
	private final ClienteRepository clienteRepository;
	private final BigDecimalConverter converter;
	
	@Autowired
	public ServicoPrestadoController(ServicoPrestadoRepository repository, 
			ClienteRepository ClienteRepository, BigDecimalConverter converter) {
		this.repository = repository;
		this.clienteRepository = ClienteRepository;
		this.converter = converter;
	}

	@GetMapping
	public List<ServicoPrestado> pesquisar(
			@RequestParam (value = "nome", required = false, defaultValue = "") String nome,
			@RequestParam (value = "mes", required = false) Integer mes) {
		
		return repository.findByNomeClienteAndMes("%" + nome + "%", mes);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServicoPrestado salvar(@RequestBody @Valid ServicoPrestadoDTO dto) {
		LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		Cliente cliente = clienteRepository.
				findById(dto.getIdCliente())
				.orElseThrow(() -> 
					new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inexistente"));

		ServicoPrestado servicoPrestado = new ServicoPrestado();
		servicoPrestado.setDescricao(dto.getDescricao());
		servicoPrestado.setData(data);
		servicoPrestado.setCliente(cliente);
		servicoPrestado.setValor(converter.converter(dto.getPreco()));

		return repository.save(servicoPrestado);
	}

//	@GetMapping("{codigo}")
//	public Cliente buscarPorId(@PathVariable("codigo") Integer id) {
//		return repository.findById(id)
//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
//	}
//
//	@DeleteMapping("{id}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public Class<Void> deletar(@PathVariable Integer id) {
//		return repository.findById(id).map(cliente -> {
//			repository.delete(cliente);
//			return Void.TYPE;
//		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
//	}
//
//	@PutMapping("{id}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public Class<Void> altualizar(@PathVariable Integer id, @RequestBody Cliente clienteAtualizado) {
//		return repository.findById(id).map(cliente -> {
//			cliente.setId(clienteAtualizado.getId());
//			cliente.setCpf(clienteAtualizado.getCpf());
//			repository.save(clienteAtualizado);
//			return Void.TYPE;
//		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
//	}
}
