package com.attornatus.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import com.attornatus.api.entities.Empresa;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.enums.PerfilEnum;
import com.attornatus.api.services.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	private static final String EMAIL = "email@email.com";
	private static final String CPF = "24291173474";

	@Before
	public void setUp() throws Exception {
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		this.usuarioRepository.save(obterDadosUsuario(empresa));
	}

	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testBuscarUsuarioPorEmail() {
		Usuario usuario = this.usuarioRepository.findByEmail(EMAIL);

		assertEquals(EMAIL, usuario.getEmail());
	}

	@Test
	public void testBuscarUsuarioPorCpf() {
		Usuario usuario = this.usuarioRepository.findByCpf(CPF);

		assertEquals(CPF, usuario.getCpf());
	}

	@Test
	public void testBuscarUsuarioPorEmailECpf() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail(CPF, EMAIL);

		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorEmailOuCpfParaEmailInvalido() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");

		assertNotNull(usuario);
	}

	@Test
	public void testBuscarUsuarioPorEmailECpfParaCpfInvalido() {
		Usuario usuario = this.usuarioRepository.findByCpfOrEmail("12345678901", EMAIL);

		assertNotNull(usuario);
	}

	private Usuario obterDadosUsuario(Empresa empresa) throws NoSuchAlgorithmException {
		Usuario usuario = new Usuario();
		usuario.setNome("Fulano de Tal");
		usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
		usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		usuario.setCpf(CPF);
		usuario.setEmail(EMAIL);
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
