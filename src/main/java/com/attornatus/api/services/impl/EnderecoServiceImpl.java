package com.attornatus.api.services.impl;

import com.attornatus.api.entities.Endereco;
import com.attornatus.api.repositories.EnderecoRepository;
import com.attornatus.api.services.EnderecoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoServiceImpl implements EnderecoService {
	
	private static final Logger log = LoggerFactory.getLogger(EnderecoServiceImpl.class);

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Endereco persistir(Endereco endereco) {
		log.info("Persistindo endereço: {}", endereco);
		return this.enderecoRepository.save(endereco);
	}

	public Optional<Endereco> buscarPorId(Long id) {
		log.info("Buscando endereço pelo ID {}", id);
		return this.enderecoRepository.findById(id);
	}

	@Override
	public List<Endereco> buscarPorEmpresaId(Long id) {
		log.info("Buscando endereço pela empresa ID {}", id);
		return enderecoRepository.findByEmpresaId(id);
	}

	public List<Endereco> buscarPorPessoaId(Long id) {
		log.info("Buscando endereço pela pessoa ID {}", id);
		return enderecoRepository.findByPessoaId(id);
	}

}