package model;

import model.enums.Privilegios;

public class Usuario {
	private int id;
	private String email;
	private String senha;
	private Privilegios privilegios;
	private boolean ativo;

	public Usuario() {
		this.ativo = true;
	}
	
	public Usuario(int id, String email, String senha, Privilegios privilegios, boolean ativo) {
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.privilegios = privilegios;
		this.ativo = ativo;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Privilegios getPrivilegios() {
		return privilegios;
	}
	public void setPrivilegios(Privilegios privilegios) {
		this.privilegios = privilegios;
	}

	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
	

