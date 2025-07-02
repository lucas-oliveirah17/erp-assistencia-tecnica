package model;

import model.enums.TipoServico;
import model.enums.TipoPagamento;
import model.enums.FormaPagamento;
import model.enums.EstadoOrdemServico;

public class OrdemServico {
	private int id;
	private int idCliente;
	private int idAparelho;
	private int idTecnico;
	private int idAtendente;
	private String descricao;
	private String comentario;
	private TipoServico tipoServico;
	private Float valorServico;
	private TipoPagamento tipoPagamento;
	private FormaPagamento formaPagamento;
	private String prazo;
	private String dataCriacao;
	private EstadoOrdemServico estado;
	private boolean ativo;

	
	public OrdemServico() {
		this.ativo = true;
	}


	public OrdemServico(int id, int idCliente, int idAparelho, int idTecnico, int idAtendente, String descricao,
			String comentario, TipoServico tipoServico, Float valorServico, TipoPagamento tipoPagamento,
			FormaPagamento formaPagamento, String prazo, String dataCriacao, EstadoOrdemServico estado, boolean ativo) {
		super();
		this.id = id;
		this.idCliente = idCliente;
		this.idAparelho = idAparelho;
		this.idTecnico = idTecnico;
		this.idAtendente = idAtendente;
		this.descricao = descricao;
		this.comentario = comentario;
		this.tipoServico = tipoServico;
		this.valorServico = valorServico;
		this.tipoPagamento = tipoPagamento;
		this.formaPagamento = formaPagamento;
		this.prazo = prazo;
		this.dataCriacao = dataCriacao;
		this.estado = estado;
		this.ativo = ativo;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdAparelho() {
		return idAparelho;
	}
	public void setIdAparelho(int idAparelho) {
		this.idAparelho = idAparelho;
	}

	public int getIdTecnico() {
		return idTecnico;
	}
	public void setIdTecnico(int idTecnico) {
		this.idTecnico = idTecnico;
	}

	public int getIdAtendente() {
		return idAtendente;
	}
	public void setIdAtendente(int idAtendente) {
		this.idAtendente = idAtendente;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}
	
	public Float getValorServico() {
		return valorServico;
	}
	public void setValorServico(Float valorServico) {
		this.valorServico = valorServico;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}
	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getPrazo() {
		return prazo;
	}
	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	public String getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(String dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public EstadoOrdemServico getEstado() {
		return estado;
	}
	public void setEstado(EstadoOrdemServico estado) {
		this.estado = estado;
	}

	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
}
