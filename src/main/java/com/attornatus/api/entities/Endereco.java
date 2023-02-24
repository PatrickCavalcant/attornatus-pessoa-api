package com.attornatus.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Endereco")
public class Endereco implements Serializable {

	private static final long serialVersionUID = -5754246207015712518L;

	private Long id;
	private Long pessoa_id;
	private String logradouro;
	private String cidade;
	private String numero;
	private String cep;
	private String enderecoPrincipal;
	private Empresa empresa;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "pessoa", referencedColumnName ="id", foreignKey = @ForeignKey(name = "fk_pessoa"))
	private Pessoa pessoa;

	public Endereco() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPessoaId() {
		return pessoa_id;
	}

	public void setPessoaId(Long pessoa_id) {
		this.pessoa_id = pessoa_id;
	}

	@Column(name = "logradouro", nullable = false)
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	@Column(name = "cidade", nullable = false)
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

	@Column(name = "cep", nullable = false)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getEnderecoPrincipal() {
		return enderecoPrincipal;
	}

	public void setEnderecoPrincipal(String enderecoPrincipal) {
		this.enderecoPrincipal = enderecoPrincipal;
	}

	@Override
	public String toString() {
		return "Endereco [id=" + id + ", pessoa_id=" + pessoa_id
				+ ", logradouro=" + logradouro	+ ", cidade=" + cidade
				+ ", numero=" + numero + ", cep=" + cep + ", enderecoPrincipal" + enderecoPrincipal
				+ ", empresa=" + empresa + "]";
	}


}