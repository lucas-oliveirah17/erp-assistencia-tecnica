package view;

import model.Funcionario;

import model.enums.FuncaoFuncionario;

import control.FuncionarioDAO;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TelaCadastroFuncionario extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private TelaGerenciamentoFuncionarios painelGerenciamento;
    
    // -- PAINÉIS --
    private String tituloJanela = "Cadastro de Funcionário";
    private JPanel painelPrincipal = new JPanel();
    private JPanel painelFormulario = new JPanel();
    private JPanel painelBotoes = new JPanel(); 

    // -- COMPONENTES DE ENTRADA --
    private JLabel jlNome = new JLabel("Nome:");
    private JTextField tfNome = new JTextField();
    
    private JLabel jlCpf = new JLabel("CPF:");
    private JTextField tfCpf = new JTextField();
    
    private JLabel jlFuncao = new JLabel("Função:");
    private JComboBox<FuncaoFuncionario> cbFuncao = new JComboBox<>(FuncaoFuncionario.values());
    
    private JLabel jlTelefone = new JLabel("Telefone:");
    private JTextField tfTelefone = new JTextField();
    
    private JLabel jlEmail = new JLabel("Email:");
    private JTextField tfEmail = new JTextField();

    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");

    // -- FONTE E TAMANHO DAS ENTRADAS --
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private Dimension inputSize = new Dimension(200, 25);
    private Dimension buttonSize = new Dimension(100, 30);
    
    public TelaCadastroFuncionario() {
    	this((TelaGerenciamentoFuncionarios) null);
    }
    
    public TelaCadastroFuncionario(TelaGerenciamentoFuncionarios painelGerenciamento) {
        this.painelGerenciamento = painelGerenciamento;
        inicializar();
    }

    public void inicializar() {
    	// -- CONFIGURAÇÕES DA JANELA --
        this.setTitle(this.tituloJanela);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

        // -- PAINEL PRINCIPAL --
        this.painelPrincipal.setLayout(new BoxLayout(this.painelPrincipal, BoxLayout.Y_AXIS));
        this.painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -- PAINEL DADOS GERAIS --
        criarPainelFormulario();

        // -- PAINEL BOTÕES --
        criarPainelBotoes();

        // --- ESTILIZAÇÃO --
        estilizarComponentes();

        // -- MONTAGEM FINAL --
        painelPrincipal.add(painelFormulario);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        painelPrincipal.add(painelBotoes);

        this.setContentPane(painelPrincipal);
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        this.setVisible(true);
    }
    private void criarPainelFormulario() {
    	this.painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados"));
        this.painelFormulario.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Padding
        gbc.weightx = 1.0;

        int linha = 0;

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelFormulario.add(this.jlNome, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelFormulario.add(this.tfNome, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelFormulario.add(this.jlCpf, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelFormulario.add(this.tfCpf, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelFormulario.add(this.jlFuncao, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelFormulario.add(this.cbFuncao, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelFormulario.add(this.jlTelefone, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelFormulario.add(this.tfTelefone, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelFormulario.add(this.jlEmail, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelFormulario.add(this.tfEmail, gbc);
    }
    
    private void criarPainelBotoes() {
    	this.painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        this.btnSalvar.addActionListener(this::salvarFuncionario);
        this.btnCancelar.addActionListener(e -> dispose());
        
        this.painelBotoes.add(this.btnCancelar);
        this.painelBotoes.add(this.btnSalvar);   	
    }

    // -- MÉTODO DE APOIO PARA APLICAR ESTILO --
    private void estilizarComponentes() {
    	// Define a fonte da borda do painel
        if (this.painelFormulario.getBorder() instanceof TitledBorder tb) {
            tb.setTitleFont(this.panelFont);
        }

        // Aplica estilo aos componentes dentro do painel de dados
        for (Component c : this.painelFormulario.getComponents()) {
            if (c instanceof JLabel label) {
                label.setFont(this.labelFont);
            } else if (c instanceof JTextField textField) {
                textField.setFont(this.inputFont);
                textField.setPreferredSize(this.inputSize);
            } else if (c instanceof JComboBox<?> comboBox) {
                comboBox.setFont(this.inputFont);
                comboBox.setPreferredSize(this.inputSize);
            }
        }
        
        this.btnSalvar.setPreferredSize(buttonSize);
        this.btnCancelar.setPreferredSize(buttonSize);
    }
    
    private void salvarFuncionario(ActionEvent e) {
        Funcionario funcionario = new Funcionario();
        try {
            funcionario.setNome(tfNome.getText());
            funcionario.setCpf(tfCpf.getText());
            funcionario.setFuncao((FuncaoFuncionario) cbFuncao.getSelectedItem());
            funcionario.setTelefone(tfTelefone.getText());
            funcionario.setEmail(tfEmail.getText());
            funcionario.setAtivo(true);            

            FuncionarioDAO dao = new FuncionarioDAO();
            boolean sucesso = dao.salvar(funcionario);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar funcionário.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}