package com.attornatus.api.controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import com.attornatus.api.dtos.UsuarioDto;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.response.Response;
import com.attornatus.api.services.UsuarioService;
import com.attornatus.api.services.utils.PasswordUtils;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;

	public UsuarioController() {
	}

	/**
	 * Atualiza os dados de um usuario.
	 * 
	 * @param id
	 * @param usuarioDto
	 * @param result
	 * @return ResponseEntity<Response<UsuarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<UsuarioDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody UsuarioDto usuarioDto, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Atualizando usuario: {}", usuarioDto.toString());
		Response<UsuarioDto> response = new Response<UsuarioDto>();

		Optional<Usuario> usuario = this.usuarioService.buscarPorId(id);
		if (!usuario.isPresent()) {
			result.addError(new ObjectError("usuario", "Usuario não encontrado."));
		}

		this.atualizarDadosUsuario(usuario.get(), usuarioDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando usuario: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.usuarioService.persistir(usuario.get());
		response.setData(this.converterUsuarioDto(usuario.get()));

		return ResponseEntity.ok(response);
	}

	@GetMapping(value = "/empresa/{id}")
	public ResponseEntity<Response<List<UsuarioDto>>> atualizar(@PathVariable("id") Long id) {
		log.info("Buscando usuario por id de empresa: {}", id);
		Response<List<UsuarioDto>> response = new Response<List<UsuarioDto>>();

		List<Usuario> usuarios = usuarioService.buscarPorEmpresaId(id);

		response.setData(usuarios.stream()
				.map(func -> converterUsuarioDto(func))
				.collect(Collectors.toList()));

		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados do usuario com base nos dados encontrados no DTO.
	 * 
	 * @param usuario
	 * @param usuarioDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosUsuario(Usuario usuario, UsuarioDto usuarioDto, BindingResult result)
			throws NoSuchAlgorithmException {
		usuario.setNome(usuarioDto.getNome());

		if (!usuario.getEmail().equals(usuarioDto.getEmail())) {
			this.usuarioService.buscarPorEmail(usuarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			usuario.setEmail(usuarioDto.getEmail());
		}

		if (usuarioDto.getSenha().isPresent()) {
			usuario.setSenha(PasswordUtils.gerarBCrypt(usuarioDto.getSenha().get()));
		}
	}

	/**
	 * Retorna um DTO com os dados de um usuario.
	 * 
	 * @param usuario
	 * @return UsuarioDto
	 */
	private UsuarioDto converterUsuarioDto(Usuario usuario) {
		UsuarioDto usuarioDto = new UsuarioDto();
		usuarioDto.setId(usuario.getId());
		usuarioDto.setEmail(usuario.getEmail());
		usuarioDto.setNome(usuario.getNome());

		return usuarioDto;
	}

}
