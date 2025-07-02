package view;

import model.Funcionario;

import control.FuncionarioDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaCadastroFuncionario extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes de entrada
    private JTextField tfNome = new JTextField();
    private JTextField tfCpf = new JTextField();
    private JTextField tfFuncao = new JTextField();
    private JTextField tfTelefone = new JTextField();
    private JTextField tfEmail = new JTextField();

    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");

    // Fonte e tamanho das entradas
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private Dimension inputSize = new Dimension(200, 25);

    public TelaCadastroFuncionario() {
    	// Configuração da janela
        this.setTitle("Cadastro de Funcionário");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 280);
        this.setLocationRelativeTo(null);

        // -- PAINEL PRINCIPAL --
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -- PAINEL DADOS GERAIS --
        JPanel painelDadosGerais = new JPanel(new GridBagLayout());
        painelDadosGerais.setBorder(BorderFactory.createTitledBorder("Dados"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int linha = 0;

        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfNome, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfCpf, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Função:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfFuncao, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfTelefone, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfEmail, gbc);

        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));      
           
        btnSalvar.addActionListener(this::salvarFuncionario);
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        // --- ESTILIZAÇÃO --
        ((TitledBorder) painelDadosGerais.getBorder()).setTitleFont(panelFont);

        estilizarComponentes(painelDadosGerais, inputSize);

        // -- MONTAGEM FINAL --
        painelPrincipal.add(painelDadosGerais);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        painelPrincipal.add(painelBotoes);

        this.setContentPane(painelPrincipal);
        this.setVisible(true);
    }

    // Método de apoio para aplicar estilo
    private void estilizarComponentes(JPanel painel, Dimension inputSize) {
        for (Component c : painel.getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(labelFont);
            } else if (c instanceof JTextField || c instanceof JComboBox) {
                c.setFont(inputFont);
                ((JComponent) c).setPreferredSize(inputSize);
            }
        }
    }
    
    private void salvarFuncionario(ActionEvent e) {
        Funcionario funcionario = new Funcionario();
        try {
            funcionario.setNome(tfNome.getText());
            funcionario.setCpf(tfCpf.getText());
            funcionario.setFuncao(tfFuncao.getText());
            funcionario.setTelefone(tfTelefone.getText());
            funcionario.setEmail(tfEmail.getText());
            

            FuncionarioDAO dao = new FuncionarioDAO();
            boolean sucesso = dao.salvar(funcionario);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Funcionario salvo com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar funcionario.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}