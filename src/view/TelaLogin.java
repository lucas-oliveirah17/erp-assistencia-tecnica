package view;

import control.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaLogin extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    // -- COMPONENTES DE ENTRADA --
    private JTextField tfEmail = new JTextField();
    private JPasswordField pfSenha = new JPasswordField();
    
    private JButton btnEntrar = new JButton("Entrar");
    private JButton btnSair = new JButton("Sair");
    
    // -- FONTE E TAMANHO DAS ENTRADAS --
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private Dimension inputSize = new Dimension(200, 25);

    public TelaLogin() {
    	// -- CONFIGURAÇÕES DA JANELA --
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // -- PAINEL PRINCIPAL --
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // -- PAINEL ENTRADAS LOGIN
        JPanel painelEntradasLogin = new JPanel(new GridBagLayout());
        painelEntradasLogin.setBorder(BorderFactory.createTitledBorder("Login"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        int linha = 0;
        
        gbc.gridx = 0; gbc.gridy = linha;
        painelEntradasLogin.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelEntradasLogin.add(tfEmail, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEntradasLogin.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painelEntradasLogin.add(pfSenha, gbc);

        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        btnEntrar.addActionListener(this::verificarLogin);
        btnSair.addActionListener(e -> dispose());
        
        painelBotoes.add(btnSair);
        painelBotoes.add(btnEntrar);
        
        // --- ESTILIZAÇÃO --
        ((TitledBorder) painelEntradasLogin.getBorder()).setTitleFont(panelFont);

        estilizarComponentes(painelEntradasLogin, inputSize);

        // -- MONTAGEM FINAL --
        painelPrincipal.add(painelEntradasLogin);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        painelPrincipal.add(painelBotoes);

        setContentPane(painelPrincipal);
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
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

    private void verificarLogin(ActionEvent e) {
        String email = tfEmail.getText();
        String senha = new String(pfSenha.getPassword());

        try {
        	UsuarioDAO dao = new UsuarioDAO();
            List<Usuario> usuarios = dao.listarTodos();
            
            for (Usuario usuario : usuarios) {
                if (usuario.getEmail().equals(email) &&
                    usuario.getSenha().equals(senha) &&
                    usuario.isAtivo()) {
                    
                    JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                    new TelaPrincipal(usuario); // Passa o usuário logado
                    dispose(); // Fecha a tela de login
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.");
            
        } catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
