package model;

import model.enums.Privilegios;

public class Usuario {
	private int id;
	private int idFuncionario;
	private String usuario;
	private String email;
	private String senha;
	private Privilegios privilegios;
	private boolean ativo;

	
	public Usuario() {
		this.ativo = true;
	}
	
	public Usuario(
			int id, int idFuncionario, String usuario, String email, 
			String senha, Privilegios privilegios,boolean ativo) {
		this.setId(id);
		this.setIdFuncionario(idFuncionario);
		this.setUsuario(usuario);
		this.setEmail(email);
		this.setSenha(senha);
		this.setPrivilegios(privilegios);
		this.setAtivo(ativo);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(int idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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