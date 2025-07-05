package model;

import model.enums.CategoriaAparelho;
import model.enums.EstadoAparelho;

import util.Dimensao;

public class Aparelho {
	private int id;
	private String modelo;
	private String numeroSerie;
	private CategoriaAparelho categoria;
	private Dimensao dimensao;
	private Float peso;
	private int idProprietario;
	private int idEstoqueAparelho;
	private String garantia;
	private EstadoAparelho estado;
	private boolean ativo;
	
	
	public Aparelho() {
		this.ativo = true;
	}

	public Aparelho(int id, String modelo, String numeroSerie, CategoriaAparelho categoria, Dimensao dimensao,
			Float peso, int idProprietario, int idEstoqueAparelho, String garantia, EstadoAparelho estado,
			boolean ativo) {
		this.id = id;
		this.modelo = modelo;
		this.numeroSerie = numeroSerie;
		this.categoria = categoria;
		this.dimensao = dimensao;
		this.peso = peso;
		this.idProprietario = idProprietario;
		this.idEstoqueAparelho = idEstoqueAparelho;
		this.garantia = garantia;
		this.estado = estado;
		this.ativo = ativo;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
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

	public Float getPeso() {
		return peso;
	}

	public void setPeso(Float peso) {
		this.peso = peso;
	}

	public int getIdProprietario() {
		return idProprietario;
	}

	public void setIdProprietario(int idProprietario) {
		this.idProprietario = idProprietario;
	}

	public int getIdEstoqueAparelho() {
		return idEstoqueAparelho;
	}

	public void setIdEstoqueAparelho(int idEstoqueAparelho) {
		this.idEstoqueAparelho = idEstoqueAparelho;
	}

	public String getGarantia() {
		return garantia;
	}

	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	public EstadoAparelho getEstado() {
		return estado;
	}

	public void setEstado(EstadoAparelho estado) {
		this.estado = estado;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
