package view.base;

import view.components.FormInputH;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public abstract class TelaCadastroAbstrata extends JFrame {
	private static final long serialVersionUID = 1L; // Default serialVersion
		
    protected JPanel painelPrincipal = new JPanel();
    protected JPanel painelFormulario1 = new JPanel();
    protected JPanel painelFormulario2 = new JPanel();
    protected JPanel painelBotoes = new JPanel();

    protected JButton btnSalvar = new JButton("Salvar");
    protected JButton btnCancelar = new JButton("Cancelar");

    // Personalização
    protected Font panelFont = new Font("Arial", Font.BOLD, 14);

    protected Dimension buttonSize = new Dimension(100, 30);
    
    public TelaCadastroAbstrata(String tituloJanela) {
    	// -- CONFIGURAÇÕES DA JANELA --
        setTitle(tituloJanela);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // -- CONFIGURAÇÕES DOS PAINEIS --
        this.painelPrincipal.setLayout(new BoxLayout(this.painelPrincipal, BoxLayout.Y_AXIS));
        this.painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // -- AÇÕES E MONTAGEM DOS BOTÕES --
        btnCancelar.addActionListener(e -> dispose());
        this.painelBotoes.add(this.btnCancelar);
        this.painelBotoes.add(this.btnSalvar);
    }
    
    protected void finalizarTela() {
    	
        aplicarEstiloPainel();
        this.painelPrincipal.add(this.painelBotoes);
        this.add(this.painelPrincipal);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    // Cria um painel de formulário
    protected JPanel criarPainelFormulario(String titulo) {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));
        return painel;
    }

    
    // Adiciona um painel personalizado ao painel principal
    protected void adicionarPainelFormulario(JPanel painel) {
    	this.painelPrincipal.add(painel);
    	this.painelPrincipal.add(Box.createVerticalStrut(10)); // espaçamento entre painéis
    }

    // Adicionar componentes ao painel de formulário  
    protected void adicionarEntrada(JPanel painel, FormInputH input) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = painel.getComponentCount(); // posição baseada em quantidade
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.LINE_END;
        painel.add(input, gbc);
    }

    protected void aplicarEstiloPainel() {
        if (this.painelFormulario1.getBorder() instanceof TitledBorder tb) {
            tb.setTitleFont(this.panelFont);
        }
        if (this.painelFormulario2.getBorder() instanceof TitledBorder tb) {
            tb.setTitleFont(this.panelFont);
        }

        this.btnSalvar.setPreferredSize(this.buttonSize);
        this.btnCancelar.setPreferredSize(this.buttonSize);
    }
  
    // Subclasse deve definir o layout e ação de salvar
    protected abstract void construirFormulario();
    protected abstract void aoSalvar();
}