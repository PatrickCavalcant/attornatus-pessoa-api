package com.attornatus.api.services;

import com.attornatus.api.entities.Endereco;

import java.util.List;
import java.util.Optional;

public interface EnderecoService {
	
	/**
	 * Persiste um endereço na base de dados.
	 * 
	 * @param endereco
	 * @return Endereco
	 */
	Endereco persistir(Endereco endereco);

	/**
	 * Busca e retorna um endereço por ID.
	 * 
	 * @param id
	 * @return Optional<Endereco>
	 */
	Optional<Endereco> buscarPorId(Long id);

    List<Endereco> buscarPorEmpresaId(Long id);

	List<Endereco> buscarPorPessoaId(Long id);

}
