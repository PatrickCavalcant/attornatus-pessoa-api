package com.attornatus.api.controllers;

import com.attornatus.api.dtos.EnderecoDto;
import com.attornatus.api.entities.Endereco;
import com.attornatus.api.entities.Empresa;
import com.attornatus.api.response.Response;
import com.attornatus.api.services.EnderecoService;

import com.attornatus.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/enderecos")
@CrossOrigin(origins = "*")
public class EnderecoController {

	private static final Logger log = LoggerFactory.getLogger(EnderecoController.class);

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private EnderecoService enderecoService;

	public EnderecoController() {
	}

	/**
	 * Atualiza os dados de um endereço.
	 *
	 * @param id
	 * @param enderecoDto
	 * @param result
	 * @return ResponseEntity<Response<DisciplinaDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<EnderecoDto>> atualizar(@PathVariable("id") Long id,
														   @Valid @RequestBody EnderecoDto enderecoDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando endereço: {}", enderecoDto.toString());
		Response<EnderecoDto> response = new Response<EnderecoDto>();


		Optional<Endereco> endereco = this.enderecoService.buscarPorId(id);
		if (!endereco.isPresent()) {
			result.addError(new ObjectError("endereco", "Endereço não encontrado."));
		}

		this.atualizarDadosEndereco(endereco.get(), enderecoDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando endereço: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}


		this.enderecoService.persistir(endereco.get());
		response.setData(this.converterEnderecoDto(endereco.get()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Adiciona um novo endereço.
	 *
	 * @param enderecoDto
	 * @param result
	 * @return ResponseEntity<Response<EnderecoDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<EnderecoDto>> adicionar(@Valid @RequestBody EnderecoDto enderecoDto,
														   BindingResult result) throws NoSuchAlgorithmException {
		log.info("Adicionando lançamento: {}", enderecoDto.toString());
		Response<EnderecoDto> response = new Response<EnderecoDto>();

		validarDadosExistentes(enderecoDto, result);
		Endereco endereco = this.converterDtoParaEndereco(enderecoDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(enderecoDto.getCnpj());
		empresa.ifPresent(emp -> endereco.setEmpresa(emp));
		this.enderecoService.persistir(endereco);

		response.setData(this.converterEnderecoDto(endereco));
		return ResponseEntity.ok(response);
	}
	/**
	 * Retorna um endereço por ID da empresa.
	 *
	 * @param id
	 * @return ResponseEntity<Response<EnderecoDto>>
	 */
	@GetMapping(value = "/empresa/{id}")
	public ResponseEntity<Response<List<EnderecoDto>>> buscar(@PathVariable("id") Long id) {
		log.info("Buscando endereco por id de empresa: {}", id);
		Response<List<EnderecoDto>> response = new Response<List<EnderecoDto>>();

		List<Endereco> enderecos = enderecoService.buscarPorEmpresaId(id);

		response.setData(enderecos.stream()
				.map(func -> converterEnderecoDto(func))
				.collect(Collectors.toList()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna os endereços por ID pessoa.
	 *
	 * @param id
	 * @return ResponseEntity<Response<EnderecoDto>>
	 */
	@GetMapping(value = "/pessoa/{id}")
	public ResponseEntity<Response<List<EnderecoDto>>> buscarPessoaId(@PathVariable("id") Long id) {
		log.info("Buscando endereco por id de pessoa: {}", id);
		Response<List<EnderecoDto>> response = new Response<List<EnderecoDto>>();

		List<Endereco> enderecos = enderecoService.buscarPorPessoaId(id);

		response.setData(enderecos.stream()
				.map(func -> converterEnderecoDto(func))
				.collect(Collectors.toList()));

		return ResponseEntity.ok(response);
	}


	/**
	 * Verifica se a empresa está cadastrada e se o usuário não existe na base de dados.
	 *
	 * @param enderecoDto
	 * @param result
	 */
	private void validarDadosExistentes(EnderecoDto enderecoDto, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(enderecoDto.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}
	}

	/**
	 * Atualiza os dados do endereço com base nos dados encontrados no DTO.
	 *
	 * @param endereco
	 * @param enderecoDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosEndereco(Endereco endereco, EnderecoDto enderecoDto, BindingResult result)
			throws NoSuchAlgorithmException {
		endereco.setPessoaId(enderecoDto.getPessoaId());
		endereco.setLogradouro(enderecoDto.getLogradouro());
		endereco.setCidade(enderecoDto.getCidade());
		endereco.setNumero(enderecoDto.getNumero());
		endereco.setCep(enderecoDto.getCep());


	}

	/**
	 * Retorna um DTO com os dados de um endereço.
	 *
	 * @param endereco
	 * @return EnderecoDto
	 */
	private EnderecoDto converterEnderecoDto(Endereco endereco) {
		EnderecoDto enderecoDto = new EnderecoDto();
		enderecoDto.setId(Optional.of(endereco.getId()));
		enderecoDto.setPessoaId(endereco.getPessoaId());
		enderecoDto.setLogradouro(endereco.getLogradouro());
		enderecoDto.setCidade(endereco.getCidade());
		enderecoDto.setNumero(endereco.getNumero());
		enderecoDto.setCep(endereco.getCep());
		enderecoDto.setCnpj(endereco.getEmpresa().getCnpj());
		enderecoDto.setEnderecoPrincipal(endereco.getEnderecoPrincipal());

		return enderecoDto;
	}



	/**
	 * Converte um EnderecoDto para uma entidade Endereço.
	 *
	 * @param enderecoDto
	 * @param result
	 * @return Endereco
	 * @throws NoSuchAlgorithmException
	 */
	private Endereco converterDtoParaEndereco(EnderecoDto enderecoDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Endereco endereco = new Endereco();

		if (enderecoDto.getId().isPresent()) {
			Optional<Endereco> lanc = this.enderecoService.buscarPorId(enderecoDto.getId().get());
			if (lanc.isPresent()) {
				endereco = lanc.get();
			} else {
				result.addError(new ObjectError("endereco", "Endereço não encontrado."));
			}
		}
		endereco.setPessoaId(enderecoDto.getPessoaId());
		endereco.setLogradouro(enderecoDto.getLogradouro());
		endereco.setCidade(enderecoDto.getCidade());
		endereco.setNumero(enderecoDto.getNumero());
		endereco.setCep(enderecoDto.getCep());
		endereco.setEnderecoPrincipal(enderecoDto.getEnderecoPrincipal());

		return endereco;
	}


}

