package model;

import model.enums.FuncaoFuncionario;

public class Funcionario {
	private int id;
	private String nome;
	private String cpf;
	private FuncaoFuncionario funcao;
	private String telefone;
	private String email;
	private boolean ativo;

	public Funcionario() {
		this.ativo = true;
	}

	public Funcionario(int id, String nome, String cpf, FuncaoFuncionario funcao, String telefone, String email, boolean ativo) {
		super();
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.funcao = funcao;
		this.telefone = telefone;
		this.email = email;
		this.ativo = ativo;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public FuncaoFuncionario getFuncao() {
		return funcao;
	}
	public void setFuncao(FuncaoFuncionario funcao) {
		this.funcao = funcao;
	}

	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
