package view.components;

import model.Usuario;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GerenciamentoButton extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JButton btnNovo = new JButton("Novo");
	private JButton btnAtualizar = new JButton("Atualizar");
	private JButton btnLimpar = new JButton("Limpar");
	private JButton btnAtivar = new JButton("Ativar");
	private JButton btnDesativar = new JButton("Desativar");
	private JButton btnExcluir = new JButton("Excluir");

	public GerenciamentoButton(
			Usuario usuarioLogado,
			Dimension tamanhoBotao,
		    ActionListener atualizarAcao,
		    ActionListener limparAcao,
		    ActionListener ativarAcao,
		    ActionListener desativarAcao,
		    ActionListener excluirAcao
		    ) {
		inicializar(
			usuarioLogado, tamanhoBotao, atualizarAcao, limparAcao, 
			ativarAcao, desativarAcao, excluirAcao
	    );
	}
	
	public GerenciamentoButton(
			Usuario usuarioLogado,
			Dimension tamanhoBotao,
			ActionListener novoAcao,
		    ActionListener atualizarAcao,
		    ActionListener limparAcao,
		    ActionListener ativarAcao,
		    ActionListener desativarAcao,
		    ActionListener excluirAcao
		    ) {
		btnNovo.addActionListener(novoAcao);
	    btnNovo.setPreferredSize(tamanhoBotao);
	    this.add(btnNovo);
		inicializar(
			usuarioLogado, tamanhoBotao, atualizarAcao, limparAcao, 
			ativarAcao, desativarAcao, excluirAcao
		);
	}
		
	private void inicializar(
			Usuario usuarioLogado,
			Dimension tamanhoBotao,
		    ActionListener atualizarAcao,
		    ActionListener limparAcao,
		    ActionListener ativarAcao,
		    ActionListener desativarAcao,
		    ActionListener excluirAcao) {
		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

		// ActionListener, estilização e montagem dos botões
	    
	    
	    btnAtualizar.addActionListener(atualizarAcao);
	    btnAtualizar.setPreferredSize(tamanhoBotao);
	    this.add(btnAtualizar);
	    
	    btnLimpar.addActionListener(limparAcao);
	    btnLimpar.setPreferredSize(tamanhoBotao);
	    
	    btnAtivar.addActionListener(ativarAcao);
	    btnAtivar.setPreferredSize(tamanhoBotao);
	    
	    btnDesativar.addActionListener(desativarAcao);
	    btnDesativar.setPreferredSize(tamanhoBotao);
	    
	    btnExcluir.addActionListener(excluirAcao);
	    btnExcluir.setPreferredSize(tamanhoBotao);

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
