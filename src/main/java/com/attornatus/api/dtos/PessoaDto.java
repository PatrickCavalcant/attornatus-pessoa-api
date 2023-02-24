package com.attornatus.api.dtos;

import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PessoaDto {
	
	private Optional<Long> id = Optional.empty();

	private String dataNascimento;
	private String nome;
	private List<EnderecoDto> enderecos;

	public PessoaDto() {
	}

	public Optional<Long> getId() {
		return id;
	}

	public void setId(Optional<Long> id) {
		this.id = id;
	}

	@NotEmpty(message = "Nome da pessoa não pode ser vazio.")
	@Length(min = 3, max = 200, message = "Nome da pessoa deve conter entre 3 e 200 caracteres.")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@NotEmpty(message = "Data nascimento não pode ser vazia.")
	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public List<EnderecoDto> getEnderecos() {
		return enderecos;
	}
	public void setEnderecos (List<EnderecoDto> enderecos) {
		this.enderecos = enderecos;
	}

	@Override
	public String toString() {
		return "PessoasDto [id=" + ", nome=" + nome + ", dataNascimento=" + dataNascimento + "]";
	}
	
}
