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
    protected JPanel painelFormulario = new JPanel();
    protected JPanel painelBotoes = new JPanel();

    protected JButton btnSalvar = new JButton("Salvar");
    protected JButton btnCancelar = new JButton("Cancelar");

    protected Font panelFont = new Font("Arial", Font.BOLD, 14);

    protected Dimension buttonSize = new Dimension(100, 30);
    
    private int linhaAtual = 0;

    public TelaCadastroAbstrata(String tituloJanela) {
    	// -- CONFIGURAÇÕES DA JANELA --
        setTitle(tituloJanela);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // -- CONFIGURAÇÕES DOS PAINEIS --
        this.painelPrincipal.setLayout(new BoxLayout(this.painelPrincipal, BoxLayout.Y_AXIS));
        this.painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.painelFormulario.setLayout(new GridBagLayout());
        this.painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados"));

        this.painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        // -- AÇÕES E MONTAGEM DOS BOTÕES --
        btnCancelar.addActionListener(e -> dispose());
        this.painelBotoes.add(this.btnCancelar);
        this.painelBotoes.add(this.btnSalvar);

        // -- MONTAGEM FINAL --
        this.painelPrincipal.add(this.painelFormulario);
        this.painelPrincipal.add(Box.createVerticalStrut(10));
        this.painelPrincipal.add(this.painelBotoes);

        setContentPane(painelPrincipal);
        
        construirFormulario(); // chamada obrigatória da subclasse
        aplicarEstiloPainel();
        
        pack(); // Ajusta o tamanho da janela ao conteúdo
        setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
    }

    // Método utilitário para adicionar componentes ao painel de formulário
    protected void adicionarEntrada(FormInputH input) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = linhaAtual++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.LINE_END;
        this.painelFormulario.add(input, gbc);
    }

    protected void aplicarEstiloPainel() {
        if (painelFormulario.getBorder() instanceof TitledBorder tb) {
            tb.setTitleFont(panelFont);
        }

        btnSalvar.setPreferredSize(buttonSize);
        btnCancelar.setPreferredSize(buttonSize);
    }

    // Subclasse deve definir o layout e ação de salvar
    protected abstract void construirFormulario();
    protected abstract void aoSalvar();
}