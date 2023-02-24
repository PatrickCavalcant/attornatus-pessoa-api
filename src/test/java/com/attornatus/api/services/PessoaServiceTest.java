package com.attornatus.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.repositories.PessoaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PessoaServiceTest {

	@MockBean
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaService pessoaService;


	@Test
	public void testBuscarPessoaPorId() {
		Optional<Pessoa> pessoa = this.pessoaService.buscarPorId(1L);

		assertTrue(pessoa.isPresent());
	}

	@Test
	public void testPersistirPessoa() {
		Pessoa pessoa = this.pessoaService.persistir(new Pessoa());

		assertNotNull(pessoa);
	}

}
