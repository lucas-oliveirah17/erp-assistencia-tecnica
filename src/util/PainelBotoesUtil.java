package util;

import model.Usuario;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PainelBotoesUtil extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btnNovo = new JButton("Novo");
	private JButton btnAtualizar = new JButton("Atualizar");
	private JButton btnLimpar = new JButton("Limpar");
	private JButton btnAtivar = new JButton("Ativar");
	private JButton btnDesativar = new JButton("Desativar");
	private JButton btnExcluir = new JButton("Excluir");

	public PainelBotoesUtil(
			Usuario usuarioLogado,
			Dimension tamanhoBotao,
			ActionListener novoAcao,
		    ActionListener atualizarAcao,
		    ActionListener limparAcao,
		    ActionListener ativarAcao,
		    ActionListener desativarAcao,
		    ActionListener excluirAcao
		    ) {
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		// Adiciona ActionListener
	    btnNovo.addActionListener(novoAcao);
	    btnAtualizar.addActionListener(atualizarAcao);
	    btnLimpar.addActionListener(limparAcao);
	    btnAtivar.addActionListener(ativarAcao);
	    btnDesativar.addActionListener(desativarAcao);
	    btnExcluir.addActionListener(excluirAcao);

	    // Define tamanho fixo dos botões
	    btnNovo.setPreferredSize(tamanhoBotao);
	    btnAtualizar.setPreferredSize(tamanhoBotao);
	    btnLimpar.setPreferredSize(tamanhoBotao);
	    btnAtivar.setPreferredSize(tamanhoBotao);
	    btnDesativar.setPreferredSize(tamanhoBotao);
	    btnExcluir.setPreferredSize(tamanhoBotao);

	    // Adiciona botões ao painel
	    this.add(btnNovo);
	    this.add(btnAtualizar);
	    this.add(btnLimpar);
	    if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
	    	this.add(btnAtivar);
	    }
	    this.add(btnDesativar);
	    if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
	    	this.add(btnExcluir);
	    }	
	}
}
