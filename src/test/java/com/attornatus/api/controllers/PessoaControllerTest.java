package com.attornatus.api.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.attornatus.api.dtos.PessoaDto;
import com.attornatus.api.entities.Usuario;
import com.attornatus.api.entities.Pessoa;
import com.attornatus.api.services.UsuarioService;
import com.attornatus.api.services.PessoaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PessoaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PessoaService pessoaService;
	
	@MockBean
	private UsuarioService usuarioService;
	
	private static final String URL_BASE = "/api/pessoas/";
	private static final Long ID_USUARIO = 1L;
	private static final String NOME = "Fulano de Tal";
	private static final String DATA = "2023-02-20";
	

	@Test
	@WithMockUser
	public void testCadastrarPessoa() throws Exception {
		Pessoa pessoa = obterDadosPessoa();
		BDDMockito.given(this.usuarioService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Usuario()));
		BDDMockito.given(this.pessoaService.persistir(Mockito.any(Pessoa.class))).willReturn(pessoa);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.obterJsonRequisicaoPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_USUARIO))
				.andExpect(jsonPath("$.data.nome", equalTo(NOME)))
				.andExpect(jsonPath("$.data.dataNascimento").value(DATA))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	
	@Test
	@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testRemoverPessoa() throws Exception {
		BDDMockito.given(this.pessoaService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Pessoa()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_USUARIO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testRemoverPessoaAcessoNegado() throws Exception {
		BDDMockito.given(this.pessoaService.buscarPorId(Mockito.anyLong())).willReturn(Optional.of(new Pessoa()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_USUARIO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private String obterJsonRequisicaoPost() throws JsonProcessingException {
		PessoaDto pessoaDto = new PessoaDto();
		pessoaDto.setId(null);
		pessoaDto.setNome(NOME);
		pessoaDto.setDataNascimento(DATA);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(pessoaDto);
	}

	private Pessoa obterDadosPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setId(ID_USUARIO);
		pessoa.setNome(NOME);
		pessoa.setDataNascimento(DATA);
		return pessoa;
	}	

}
