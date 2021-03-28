package br.com.estudospring.clientes.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	@GetMapping("{codigo}")
	public ServicoPrestadoDTO buscarPorId(@PathVariable("codigo") Integer id) {
		ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
 		return repository.findById(id).map(servicoPrestado -> {
 			dto.setId(servicoPrestado.getId());
			dto.setDescricao(servicoPrestado.getDescricao());
			dto.setData(servicoPrestado.getData().toString());
			dto.setIdCliente(servicoPrestado.getId());
			dto.setPreco(servicoPrestado.getPreco().toString());
			return dto;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço prestado não encontrado"));
	}

	@GetMapping
	public List<ServicoPrestado> pesquisar(
			@RequestParam (value = "nome", required = false, defaultValue = "") String nome) {
			//@RequestParam (value = "mes", required = false) Integer mes) {
		
//		return repository.findByNomeClienteAndMes("%" + nome + "%", mes);
		return repository.findByNomeClienteAndMes("%" + nome + "%");
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
		servicoPrestado.setPreco(converter.converter(dto.getPreco()));

		return repository.save(servicoPrestado);
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Class<Void> deletar(@PathVariable Integer id) {
		return repository.findById(id).map(servico -> {
			repository.delete(servico);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço prestado não encontrado"));
	}

	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Class<Void> altualizar(@PathVariable Integer id, @RequestBody @Valid ServicoPrestadoDTO dtoAtualizado) {
		LocalDate data = LocalDate.parse(dtoAtualizado.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		Cliente cliente = clienteRepository.
				findById(dtoAtualizado.getIdCliente())
				.orElseThrow(() -> 
					new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente inexistente"));

		return repository.findById(id).map(servicoPrestado -> {
			servicoPrestado.setDescricao(dtoAtualizado.getDescricao());
			servicoPrestado.setData(data);
			servicoPrestado.setCliente(cliente);
			servicoPrestado.setPreco(converter.converter(dtoAtualizado.getPreco()));
			repository.save(servicoPrestado);
			return Void.TYPE;
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço Prestado não encontrado"));
	}
}
