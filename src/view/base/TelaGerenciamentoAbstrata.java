package view.base;

import model.Usuario;

import view.components.GerenciamentoForm;
import view.components.GerenciamentoTable;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public abstract class TelaGerenciamentoAbstrata extends JPanel {
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
    
    public  TelaGerenciamentoAbstrata(Usuario usuarioInstancia) {
    	this.usuarioLogado = usuarioInstancia;
    	
    	this.setLayout(new GridBagLayout());
    }
    
    protected void finalizarTela() {
    	GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.weightx = 1;       
        gbc.weighty = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha topo esquerdo
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permite expandir horizontalmente
        
        gbc.insets = new Insets(10, 10, 0, 10); // padding
        gbc.gridy = 0; // Define que o componente será colocado na linha 0 do GridBagLayout
        
    	add(painelTabela, gbc);
    	
    	gbc.insets = new Insets(0, 0, 0, 0); // padding 0px
        gbc.gridy = 1;
        add(painelFormularios, gbc);
        
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Alinha topo esquerdo
        add(painelBotoes, gbc);
    }
    
    protected void criarPainelTabela(String nomeTabela, String[] colunas) {
    	// Configuração do painel da tabela
    	this.painelTabela.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder(nomeTabela),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
    	painelTabela.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.gridx = 0; gbc.weightx = 1;
    	gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha topo esquerdo
    	gbc.fill = GridBagConstraints.HORIZONTAL; // Permite expandir horizontalmente
    	int linhaAtual = 0;
        
        // Inclusão do filtro
    	this.ckbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        
    	if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
            gbc.insets = new Insets(0, 5, 5, 5); // padding
            gbc.gridy = linhaAtual; // Define que o componente será colocado na linha 0 do GridBagLayout
    		linhaAtual++;
    		
    		this.painelTabela.add(this.ckbMostrarInativos, gbc);
    	}
    	
    	this.colunasTabela = colunas;
    	
    	// Tabela com células não editáveis
        this.tabela = new GerenciamentoTable(colunas);
        
        carregarDadosTabela();
        
        gbc.insets = new Insets(5, 5, 5, 5); // padding
        gbc.gridy = linhaAtual; // Define que o componente será colocado na linha 0 do GridBagLayout
        
        this.painelTabela.add(tabela.comScrollPanel(), gbc);
        
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
