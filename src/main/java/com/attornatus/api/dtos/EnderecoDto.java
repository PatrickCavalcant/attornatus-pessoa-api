package com.attornatus.api.dtos;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import com.attornatus.api.entities.Endereco;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public class EnderecoDto {

	private Optional<Long> id = Optional.empty();
	private Long pessoa_id;
	private String logradouro;
	private String cidade;
	private String numero;
	private String cep;
	private String cnpj;
	private String enderecoPrincipal;

	public EnderecoDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}

	public Long getPessoaId() {
		return pessoa_id;
	}

	public void setPessoaId(Long pessoa_id) {
		this.pessoa_id = pessoa_id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCep() {return cep;}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@CNPJ(message="CNPJ inválido.")
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEnderecoPrincipal() {
		return enderecoPrincipal;
	}

	public void setEnderecoPrincipal(String enderecoPrincipal) {
		this.enderecoPrincipal = enderecoPrincipal;
	}

	@Override
	public String toString() {
		return "EnderecoDto [id=" + id + ", pessoa_id=" + pessoa_id
				+ ", logradouro=" + logradouro + ", cidade=" + cidade
				+ ", numero=" + numero + ", cep=" + cep
				+ ", cnpj=" + cnpj + ", enderecoPrincipal" + enderecoPrincipal +"]";
	}


}