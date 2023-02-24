package com.attornatus.api.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "pessoa")
public class Pessoa implements Serializable {
	
	private static final long serialVersionUID = 6524560251526772839L;

	private Long id;
	private String dataNascimento;
	private String nome;

	public Pessoa() {
	}

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "nome", nullable = true)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "dataNascimento", nullable = true)
	public String getDataNascimento() {return dataNascimento;}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + ", dataNascimento=" + dataNascimento + "]";
	}

}
