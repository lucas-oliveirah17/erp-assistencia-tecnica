package model;

import model.enums.CategoriaAparelho;

import util.Dimensao;

public class EstoqueAparelho {
	private int id;
	private String localizacao;
	private CategoriaAparelho categoria;
	private Dimensao dimensao;
	private int capacidade;
	private boolean ativo;
	
	
	public EstoqueAparelho() {
		this.ativo = true;
	}
	
	public EstoqueAparelho(int id, String localizacao, CategoriaAparelho categoria, Dimensao dimensao, int capacidade,
			boolean ativo) {
		this.id = id;
		this.localizacao = localizacao;
		this.categoria = categoria;
		this.dimensao = dimensao;
		this.capacidade = capacidade;
		this.ativo = ativo;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	public CategoriaAparelho getCategoria() {
		return categoria;
	}
	public void setCategoria(CategoriaAparelho categoria) {
		this.categoria = categoria;
	}
	
	public Dimensao getDimensao() {
		return dimensao;
	}
	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}
	
	public int getCapacidade() {
		return capacidade;
	}
	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
