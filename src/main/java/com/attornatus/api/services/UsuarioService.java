package com.attornatus.api.services;

import java.util.List;
import java.util.Optional;

import com.attornatus.api.entities.Usuario;

public interface UsuarioService {
	
	/**
	 * Persiste um usuário na base de dados.
	 * 
	 * @param usuario
	 * @return Usuario
	 */
	Usuario persistir(Usuario usuario);
	
	/**
	 * Busca e retorna um usuário dado um CPF.
	 * 
	 * @param cpf
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorCpf(String cpf);
	
	/**
	 * Busca e retorna um usuário dado um email.
	 * 
	 * @param email
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorEmail(String email);
	
	/**
	 * Busca e retorna um usuário por ID.
	 * 
	 * @param id
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorId(Long id);

    List<Usuario> buscarPorEmpresaId(Long id);
}
