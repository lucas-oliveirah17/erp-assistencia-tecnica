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
    
    // -- PAINEIS --
    private String tituloJanela = "Login";
    private JPanel painelPrincipal = new JPanel();
    private JPanel painelEntradasLogin = new JPanel();
    private JPanel painelBotoes = new JPanel();
    
    // -- COMPONENTES DE ENTRADA --
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

    public TelaLogin() {
    	// Configurações da janela
        setTitle(tituloJanela);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Painel principal
        this.painelPrincipal.setLayout(new BoxLayout(this.painelPrincipal, BoxLayout.Y_AXIS));
        this.painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de entradas
        criarPainelEntradasLogin();
        
        // Painel de botões
        criarPainelBotoes();
        
        // Estilizar
        estilizarComponentes();
        
        // Montagem final
        this.painelPrincipal.add(this.painelEntradasLogin);
        this.painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        this.painelPrincipal.add(this.painelBotoes);

        setContentPane(this.painelPrincipal);
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
    }
    
    private void criarPainelEntradasLogin() {
    	this.painelEntradasLogin.setLayout(new GridBagLayout());
    	this.painelEntradasLogin.setBorder(BorderFactory.createTitledBorder(tituloJanela));
        
        this.lbSenha.setFont(this.labelFont);
        this.pfSenha.setFont(this.inputFont);
        this.pfSenha.setPreferredSize(this.inputSize);

        this.ckbMostrarSenha.setFont(this.labelFont);
        
        // Adicionando ações
        this.ckbMostrarSenha.addActionListener(e -> {
        	this.pfSenha.setEchoChar(this.ckbMostrarSenha.isSelected() ? (char) 0 : '●');
        });
        
        // Montando entradas no painel  
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.weightx = 1.0;
        
        int linha = 0;
        
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelEntradasLogin.add(this.lbLogin, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelEntradasLogin.add(this.tfLogin, gbc);

        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = linha;
        this.painelEntradasLogin.add(lbSenha, gbc);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = linha;
        this.painelEntradasLogin.add(pfSenha, gbc);
        
        linha++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 1; gbc.gridy = linha;      
        this.painelEntradasLogin.add(ckbMostrarSenha, gbc);
    }
    
    private void criarPainelBotoes() {
    	this.painelBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
    	
		// Adicionando ações
 		this.btnEntrar.addActionListener(this::verificarLogin);
 		this.btnSair.addActionListener(e -> dispose());
		
		// Montando botões no painel        
 		this.painelBotoes.add(this.btnSair);
 		this.painelBotoes.add(Box.createHorizontalStrut(10)); // Espaçamento
 		this.painelBotoes.add(this.btnEntrar);
		
		// Enter aciona o botão Entrar        
		getRootPane().setDefaultButton(this.btnEntrar); 
    }
    
    private void estilizarComponentes() {
        // Login
        ((TitledBorder) this.painelEntradasLogin.getBorder()).setTitleFont(this.panelFont);
        this.lbLogin.setFont(this.labelFont);
        this.tfLogin.setFont(this.inputFont);
        this.tfLogin.setPreferredSize(this.inputSize);
    	
 		// Botões
        this.btnSair.setPreferredSize(new Dimension(this.buttonSize));
 		this.btnEntrar.setPreferredSize(new Dimension(this.buttonSize));	
    }
    
    private void verificarLogin(ActionEvent e) {
        String login = this.tfLogin.getText().trim();
        String senha = new String(this.pfSenha.getPassword());
        
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
