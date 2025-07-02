package model;

import model.enums.CategoriaAparelho;
import model.enums.EstadoAparelho;

public class Aparelho {
	private int id;
	private String modelo;
	private String numeroSerie;
	private CategoriaAparelho categoria;
	private String dimensao;
	private Float peso;
	private int proprietario;
	private int idEstoqueAparelho;
	private String garantia;
	private EstadoAparelho estado;
	private boolean ativo;
	
	
	public Aparelho() {
		this.ativo = true;
	}

	public Aparelho(int id, String modelo, String numeroSerie, CategoriaAparelho categoria, String dimensao, Float peso,
			int proprietario, int idEstoqueAparelho, String garantia, EstadoAparelho estado, boolean ativo) {
		super();
		this.id = id;
		this.modelo = modelo;
		this.numeroSerie = numeroSerie;
		this.categoria = categoria;
		this.dimensao = dimensao;
		this.peso = peso;
		this.proprietario = proprietario;
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

	public String getDimensao() {
		return dimensao;
	}
	public void setDimensao(String dimensao) {
		this.dimensao = dimensao;
	}

	public Float getPeso() {
		return peso;
	}
	public void setPeso(Float peso) {
		this.peso = peso;
	}

	public int getProprietario() {
		return proprietario;
	}
	public void setProprietario(int proprietario) {
		this.proprietario = proprietario;
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
