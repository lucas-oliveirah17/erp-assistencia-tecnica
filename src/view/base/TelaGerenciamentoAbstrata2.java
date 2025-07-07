package view.base;

import model.Usuario;

import view.components.GerenciamentoForm;
import view.components.GerenciamentoTable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public abstract class TelaGerenciamentoAbstrata2 extends JPanel {
	private static final long serialVersionUID = 1L; // Default serialVersion
	
	protected Usuario usuarioLogado;

    protected JPanel painelTabela = new JPanel();
    protected JPanel painelFormularios = new JPanel();
    protected JPanel painelBotoes = new JPanel();
    
    // Componentes do painel tabela
    protected GerenciamentoTable tabela;    
    protected GerenciamentoForm formularioEsquerdo;
    protected GerenciamentoForm formularioDireito;
    
    protected JCheckBox ckbMostrarInativos = new JCheckBox("Mostrar Inativos");
    protected String[] colunasTabela;
   
    // Personalização
    protected Font panelFont = new Font("Arial", Font.BOLD, 14);
    protected Dimension buttonSize = new Dimension(100, 30);
    
    public  TelaGerenciamentoAbstrata2(Usuario usuarioInstancia) {
    	this.usuarioLogado = usuarioInstancia;
    	
    	// Define o layout do painel principal como um BoxLayout na direção vertical (Y_AXIS),
    	// ou seja, os componentes serão empilhados verticalmente (um abaixo do outro).
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    
    protected void finalizarTela() {
    	add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertica
        add(painelFormularios);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertical
        add(painelBotoes);
    }
    
    protected void criarPainelTabela(String[] colunas) {
    	// Configuração do painel da tabela
    	this.painelTabela.setLayout(new BoxLayout(this.painelTabela, BoxLayout.Y_AXIS));
    	this.painelTabela.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Lista de Clientes"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
    	this.painelTabela.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo
        
        // Inclusão do filtro
    	this.ckbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        
    	if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
    		this.ckbMostrarInativos.setAlignmentX(Component.LEFT_ALIGNMENT);
    		this.painelTabela.add(this.ckbMostrarInativos);
        	add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertica
    	}
    	
    	this.colunasTabela = colunas;
    	
    	// Tabela com células não editáveis
        this.tabela = new GerenciamentoTable(colunas);
        
        carregarDadosTabela();
        
        this.painelTabela.add(tabela.comScrollPanel());
        
        // Evento para preencher o formulário ao clicar na tabela
        this.tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        }); 
    }
    
    protected abstract void carregarDadosTabela();
    
    protected void criarPainelFormulario() {
    	this.painelFormularios = new JPanel(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
    	
    	if(formularioDireito.getTitulo() != null) {
    		// Formulario da coluna esquerda
            gbc.gridx = 0; // Define que o componente será colocado na coluna 0 do GridBagLayout
            gbc.weightx = 0.5; // Define o "peso" horizontal. 0.5 para ocupar metade do painel
            gbc.weighty = 1; // 0 para não esticar verticalmente
            gbc.insets = new Insets(10, 10, 10, 10); // padding 10px
            gbc.fill = GridBagConstraints.HORIZONTAL; // Permite expandir horizontalmente
            gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha topo esquerdo
            
            this.painelFormularios.add(this.formularioEsquerdo, gbc);
            
            // Painel da coluna direita
            gbc.gridx = 1;
            gbc.weightx = 0.5;
            gbc.weighty = 1;
            gbc.insets = new Insets(10, 10, 10, 10); // padding 10px
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            
            this.painelFormularios.add(this.formularioDireito, gbc);
    	} else {
    		GerenciamentoForm painelVazioAux = new GerenciamentoForm();
    		
    		// Painel (VAZIO) para colocar na primeira coluna
            gbc.gridx = 0;
            gbc.weightx = 0.3;
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 0, 0); // Sem padding
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            
            this.painelFormularios.add(painelVazioAux, gbc);
    		
    		// FormularioEsquerdo na coluna central
            gbc.gridx = 1; // Define que o componente será colocado na coluna 0 do GridBagLayout
            gbc.weightx = 0.7; // Define o "peso" horizontal. 0.7, para controlar o quanto ele ocupe o painel
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 0, 0); // padding 10px
            gbc.fill = GridBagConstraints.HORIZONTAL; // Permite expandir horizontalmente
            gbc.anchor = GridBagConstraints.CENTER; // Alinha no meio superior
            
            this.painelFormularios.add(this.formularioEsquerdo, gbc);
            
            // Painel do formulário direito na coluna direita (VAZIO) 
            gbc.gridx = 2;
            gbc.weightx = 0.3;
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 0, 0); // Sem padding
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;            
            
            this.painelFormularios.add(this.formularioDireito, gbc);
    	}
    }
    
    protected abstract void preencherFormulario(int linha);
    
    protected abstract void limparFormulario();
}
