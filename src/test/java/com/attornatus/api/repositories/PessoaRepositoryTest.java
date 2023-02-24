package com.attornatus.api.repositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import com.attornatus.api.entities.Empresa;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.enums.PerfilEnum;
import com.attornatus.api.services.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaRepositoryTest {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	private Long usuarioId;

	@Before
	public void setUp() throws Exception {

		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

		Usuario usuario = this.usuarioRepository.save(obterDadosUsuario(empresa));
		this.usuarioId = usuario.getId();
		
		this.pessoaRepository.save(obterDadosPessoas(usuario));
		this.pessoaRepository.save(obterDadosPessoas(usuario));


	}

	@After
	public void tearDown() throws Exception {
		this.empresaRepository.deleteAll();
	}


	private Pessoa obterDadosPessoas(Usuario usuario) {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Fulano de Tal");
		pessoa.setDataNascimento("2023-02-20");
		return pessoa;
	}

	private Usuario obterDadosUsuario(Empresa empresa) throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano de Tal");
		usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
		usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		usuario.setCpf("24291173474");
		usuario.setEmail("email@email.com");
		usuario.setEmpresa(empresa);
		return usuario;
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}



}
