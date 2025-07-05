package view;

import model.Usuario;

import control.UsuarioDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TelaLogin extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    // -- COMPONENTES DE ENTRADA --
    private String tituloJanela = "Login";
    
    private JLabel lbLogin = new JLabel("Usuário ou Email:");
    private JTextField tfLogin = new JTextField();
    
    private JLabel lbSenha = new JLabel("Senha:");
    private JPasswordField pfSenha = new JPasswordField();
    
    private JCheckBox ckbMostrarSenha = new JCheckBox("Mostrar senha");
    
    private JButton btnEntrar = new JButton("Entrar");
    private JButton btnSair = new JButton("Sair");
    
    // -- FONTE E TAMANHO DAS ENTRADAS --
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    
    private Dimension inputSize = new Dimension(200, 25);
    private Dimension buttonSize = new Dimension(100, 30);
    
    // Paineis
    private JPanel painelPrincipal = new JPanel();
    private JPanel painelEntradasLogin = new JPanel();
    private JPanel painelBotoes = new JPanel();

    public TelaLogin() {
    	// Configurações da janela
        setTitle(tituloJanela);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Painel principal
        this.painelPrincipal.setLayout(new BoxLayout(this.painelPrincipal, BoxLayout.Y_AXIS));
        this.painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entradas
        this.painelEntradasLogin = criarPainelEntradasLogin();
        
        // Painel de botões
        this.painelBotoes = criarPainelBotoes();
        

        // Montagem final
        this.painelPrincipal.add(this.painelEntradasLogin);
        this.painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        this.painelPrincipal.add(this.painelBotoes);

        setContentPane(this.painelPrincipal);
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
    }
    
    private JPanel criarPainelEntradasLogin() {
    	JPanel painelEntradasLogin = new JPanel(new GridBagLayout());
        painelEntradasLogin.setBorder(BorderFactory.createTitledBorder("Login"));
        
        // Estilização
        ((TitledBorder) painelEntradasLogin.getBorder()).setTitleFont(panelFont);
        lbLogin.setFont(labelFont);
        tfLogin.setFont(inputFont);
        tfLogin.setPreferredSize(inputSize);

        lbSenha.setFont(labelFont);
        pfSenha.setFont(inputFont);
        pfSenha.setPreferredSize(inputSize);

        ckbMostrarSenha.setFont(labelFont);
        
        // Adicionando ações
        ckbMostrarSenha.addActionListener(e -> {
            pfSenha.setEchoChar(ckbMostrarSenha.isSelected() ? (char) 0 : '●');
        });
        
        // Montando entradas no painel  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        
        int linha = 0;
        
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEntradasLogin.add(lbLogin, gbc);
        
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelEntradasLogin.add(tfLogin, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEntradasLogin.add(lbSenha, gbc);
        gbc.gridx = 1; gbc.gridy = linha;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelEntradasLogin.add(pfSenha, gbc);
        
        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 1; gbc.gridy = linha;      
        painelEntradasLogin.add(ckbMostrarSenha, gbc);
        
        return painelEntradasLogin;
    }
    
    private JPanel criarPainelBotoes() {
    	JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 
 		// Estilização
 		btnSair.setPreferredSize(new Dimension(buttonSize));
 		btnEntrar.setPreferredSize(new Dimension(buttonSize));
              
		// Adicionando ações
		btnEntrar.addActionListener(this::verificarLogin);
		btnSair.addActionListener(e -> dispose());
		
		// Montando botões no painel        
		painelBotoes.add(btnSair);
		painelBotoes.add(Box.createHorizontalStrut(10)); // Espaçamento
		painelBotoes.add(btnEntrar);
		
		// Enter aciona o botão Entrar        
		getRootPane().setDefaultButton(btnEntrar); 
		
		return painelBotoes;
    }
    
    private void verificarLogin(ActionEvent e) {
        String login = tfLogin.getText().trim();
        String senha = new String(pfSenha.getPassword());
        
        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Por favor, preencha todos os campos.");
            return;
        }

        try {
        	UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.autenticar(login, senha);
            
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                new TelaPrincipal(usuario);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Login ou senha inválidos.");
            }
            
        } catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}
