package com.attornatus.api.services;

import java.util.Optional;

import com.attornatus.api.entities.Pessoa;

public interface PessoaService {

	/**
	 * Retorna uma pessoa por ID.
	 *
	 * @param id
	 * @return Optional<Pessoa>
	 */
	Optional<Pessoa> buscarPorId(Long id);
	
	/**
	 * Persiste uma pessoas na base de dados.
	 * 
	 * @param pessoa
	 * @return Pessoa
	 */
	Pessoa persistir(Pessoa pessoa);
	
	/**
	 * Remove uma pessoa da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);




}
