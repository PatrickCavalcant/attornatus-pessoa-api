package com.attornatus.api.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.attornatus.api.entities.Endereco;
import com.attornatus.api.repositories.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.services.PessoaService;

@Service
public class PessoaServiceImpl implements PessoaService {

	private static final Logger log = LoggerFactory.getLogger(PessoaServiceImpl.class);

	@Autowired
	private PessoaRepository pessoaRepository;

	@Cacheable("pessoaPorId")
	public Optional<Pessoa> buscarPorId(Long id) {
		log.info("Buscando uma pessoa pelo ID {}", id);
		return this.pessoaRepository.findById(id);
	}
	
	@CachePut("pessoaPorId")
	public Pessoa persistir(Pessoa pessoa) {
		log.info("Persistindo a pessoa: {}", pessoa);
		return this.pessoaRepository.save(pessoa);
	}
	
	public void remover(Long id) {
		log.info("Removendo a pessoa ID {}", id);
		this.pessoaRepository.deleteById(id);
	}

}
