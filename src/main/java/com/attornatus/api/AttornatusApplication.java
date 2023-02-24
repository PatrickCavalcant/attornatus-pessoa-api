package com.attornatus.api;

import com.attornatus.api.entities.Endereco;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.entities.Empresa;
import com.attornatus.api.enums.PerfilEnum;
import com.attornatus.api.repositories.EmpresaRepository;
import com.attornatus.api.repositories.UsuarioRepository;
import com.attornatus.api.repositories.PessoaRepository;
import com.attornatus.api.repositories.EnderecoRepository;
import com.attornatus.api.services.utils.PasswordUtils;

import com.sun.xml.fastinfoset.stax.events.EndDocumentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.Date;

@SpringBootApplication
@EnableCaching
public class AttornatusApplication {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public static void main(String[] args) {
		SpringApplication.run(AttornatusApplication.class, args);
	}

	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String...args) throws Exception {
			Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Empresa Teste");
			empresa.setCnpj("31965365000184");
			empresaRepository.save(empresa);

			Usuario usuarioAdmin = new Usuario();
			usuarioAdmin.setCpf("25164061422");
			usuarioAdmin.setEmail("admin@empresa.com");
			usuarioAdmin.setNome("Administrador");
			usuarioAdmin.setPerfil(PerfilEnum.ROLE_ADMIN);
			usuarioAdmin.setSenha(PasswordUtils.gerarBCrypt("123456"));
			usuarioAdmin.setEmpresa(empresa);
			usuarioRepository.save(usuarioAdmin);

			Usuario usuario = new Usuario();
			usuario.setCpf("09943636211");
			usuario.setEmail("teste2@empresa.com");
			usuario.setNome("Joao Freitas Oliveira");
			usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
			usuario.setSenha(PasswordUtils.gerarBCrypt("123456"));
			usuario.setEmpresa(empresa);
			usuarioRepository.save(usuario);

			Pessoa pessoa = new Pessoa();
			pessoa.setDataNascimento("1998-08-24");
			pessoa.setNome("Patrick Cavalcante Moraes");
			pessoaRepository.save(pessoa);

			Endereco endereco = new Endereco();
			endereco.setPessoaId(1L);
			endereco.setLogradouro("Rua Manuel Reis Quadra 25 Lote 07");
			endereco.setCidade("Luziânia-GO");
			endereco.setNumero("55");
			endereco.setCep("72800-970");
			endereco.setEnderecoPrincipal("Sim");
			endereco.setEmpresa(empresa);
			enderecoRepository.save(endereco);

			Endereco endereco2 = new Endereco();
			endereco2.setPessoaId(1L);
			endereco2.setLogradouro("Avenida Ismar Quadra 36 Lote 27");
			endereco2.setCidade("Luziânia-GO");
			endereco2.setNumero("33");
			endereco2.setCep("72800-970");
			endereco2.setEnderecoPrincipal("Não");
			endereco2.setEmpresa(empresa);
			enderecoRepository.save(endereco2);


			empresaRepository.findAll().forEach(System.out::println);
			usuarioRepository.findByEmpresaId(empresa.getId()).forEach(System.out::println);
			pessoaRepository.findAll().forEach(System.out::println);
			enderecoRepository.findByEmpresaId(empresa.getId()).forEach(System.out::println);

		}
	}
}
