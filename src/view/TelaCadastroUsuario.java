package view;

import model.Usuario;

import model.enums.Privilegios;

import control.UsuarioDAO;

import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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

public class TelaCadastroUsuario extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private TelaGerenciamentoUsuarios painelGerenciamento;

    // -- COMPONENTES DE ENTRADA --
    private JTextField tfEmail = new JTextField();
    private JTextField tfSenha = new JTextField();
    private JComboBox<Privilegios> cbPrivilegios = new JComboBox<>(Privilegios.values());

    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");

    // -- FONTE E TAMANHO DAS ENTRADAS --
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private Dimension inputSize = new Dimension(200, 25);

    public TelaCadastroUsuario() {
    	this((TelaGerenciamentoUsuarios) null);
    }
    
    public TelaCadastroUsuario(TelaGerenciamentoUsuarios painelGerenciamento) {
        this.painelGerenciamento = painelGerenciamento;
        inicializar();
    }
    
    public void inicializar() {
    	// -- CONFIGURAÇÕES DA JANELA --
        this.setTitle("Cadastro de Usuários");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);

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
        painelDadosGerais.add(tfEmail, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfSenha, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Função:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(cbPrivilegios, gbc);

        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));      
           
        btnSalvar.addActionListener(this::salvarUsuario);
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        // --- ESTILIZAÇÃO --
        ((TitledBorder) painelDadosGerais.getBorder()).setTitleFont(panelFont);

        estilizarComponentes(painelDadosGerais, inputSize);

        // -- MONTAGEM FINAL --
        painelPrincipal.add(painelDadosGerais);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        painelPrincipal.add(painelBotoes);

        this.setContentPane(painelPrincipal);
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        this.setVisible(true);
    }

    // -- MÉTODO DE APOIO PARA APLICAR ESTILO --
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
    
    private void salvarUsuario(ActionEvent e) {
        Usuario usuario = new Usuario();
        try {
        	usuario.setEmail(tfEmail.getText());
        	usuario.setSenha(tfSenha.getText());
            usuario.setPrivilegios((Privilegios) cbPrivilegios.getSelectedItem());

            UsuarioDAO dao = new UsuarioDAO();
            int idGerado = dao.salvar(usuario);

            if (idGerado > 0) {
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}