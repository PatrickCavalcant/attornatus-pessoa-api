package com.attornatus.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.attornatus.api.dtos.EnderecoDto;
import com.attornatus.api.repositories.PessoaRepository;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attornatus.api.dtos.PessoaDto;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.response.Response;
import com.attornatus.api.services.UsuarioService;
import com.attornatus.api.services.PessoaService;

@RestController
@RequestMapping("/api/pessoas")
@CrossOrigin(origins = "*")
public class PessoaController {

	@Autowired
	private EnderecoController enderecoController;

	private static final Logger log = LoggerFactory.getLogger(PessoaController.class);

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private UsuarioService usuarioService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;


	public PessoaController() {
	}


	/**
	 * Retorna uma pessoa por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<PessoaDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaDto>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando pessoa por ID: {}", id);
		Response<PessoaDto> response = new Response<PessoaDto>();
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(id);

		if (!pessoa.isPresent()) {
			log.info("Pessoa não encontrado para o ID: {}", id);
			response.getErrors().add("Pessoa não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterPessoaDto(pessoa.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna todos pessoas.
	 * O método findAll() do JPA, busca tudo que estiver na tabela da nossa entidade na base.
	 *
	 * @param
	 * @return ResponseEntity<Response<PessoaDto>>
	 */

	@GetMapping(value = "/todos")
	public ResponseEntity<Response<List<PessoaDto>>> listarTodos() {

		log.info("Buscando todos pessoas {}");
		Response<List<PessoaDto>> response = new Response<List<PessoaDto>>();

		List<Pessoa> pessoas = pessoaRepository.findAll();

		response.setData(pessoas.stream()
				.map(pessoa -> converterPessoaDto(pessoa))
				.collect(Collectors.toList()));

		return ResponseEntity.ok(response);
	}


	/**
	 * Adiciona uma nova pessoa.
	 * 
	 * @param pessoaDto
	 * @param result
	 * @return ResponseEntity<Response<PessoaDto>>
	 * @throws ParseException 
	 */
	@PostMapping
	public ResponseEntity<Response<PessoaDto>> adicionar(@Valid @RequestBody PessoaDto pessoaDto,
														 BindingResult result) throws ParseException {
		log.info("Adicionando pessoa: {}", pessoaDto.toString());
		Response<PessoaDto> response = new Response<PessoaDto>();
		Pessoa pessoa = this.converterDtoParaPessoa(pessoaDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando pessoa: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		pessoa = this.pessoaService.persistir(pessoa);
		response.setData(this.converterPessoaDto(pessoa));
		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados de uma pessoa.
	 * 
	 * @param id
	 * @param pessoaDto
	 * @return ResponseEntity<Response<Pessoa>>
	 * @throws ParseException 
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<PessoaDto>> atualizar(@PathVariable("id") Long id,
														 @Valid @RequestBody PessoaDto pessoaDto, BindingResult result) throws ParseException {
		log.info("Atualizando pessoa: {}", pessoaDto.toString());
		Response<PessoaDto> response = new Response<PessoaDto>();
		pessoaDto.setId(Optional.of(id));
		Pessoa pessoa = this.converterDtoParaPessoa(pessoaDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando pessoa: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		pessoa = this.pessoaService.persistir(pessoa);
		response.setData(this.converterPessoaDto(pessoa));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um pessoa por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Pessoa>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo pessoa: {}", id);
		Response<String> response = new Response<String>();
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(id);

		if (!pessoa.isPresent()) {
			log.info("Erro ao remover devido ao pessoa ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover pessoa. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.pessoaService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}


	/**
	 * Converte uma entidade pessoa para seu respectivo DTO.
	 * 
	 * @param pessoa
	 * @return PessoaDto
	 */
	private PessoaDto converterPessoaDto(Pessoa pessoa) {
		PessoaDto pessoaDto = new PessoaDto();
		pessoaDto.setId(Optional.of(pessoa.getId()));
		pessoaDto.setDataNascimento(pessoa.getDataNascimento());
		pessoaDto.setNome(pessoa.getNome());
		pessoaDto.setEnderecos(enderecoController.buscarPessoaId(pessoa.getId()).getBody().getData());

		return pessoaDto;
	}

	/**
	 * Converte um PessoaDto para uma entidade Pessoa.
	 * 
	 * @param pessoaDto
	 * @param result
	 * @return Pessoa
	 * @throws ParseException 
	 */
	private Pessoa converterDtoParaPessoa(PessoaDto pessoaDto, BindingResult result) throws ParseException {
		Pessoa pessoa = new Pessoa();

		if (pessoaDto.getId().isPresent()) {
			Optional<Pessoa> pess = this.pessoaService.buscarPorId(pessoaDto.getId().get());
			if (pess.isPresent()) {
				pessoa = pess.get();
			} else {
				result.addError(new ObjectError("pessoa", "Pessoa não encontrado."));
			}
		} else {

		}
		pessoa.setNome(pessoaDto.getNome());
		pessoa.setDataNascimento(pessoaDto.getDataNascimento());

		return pessoa;
	}

}
