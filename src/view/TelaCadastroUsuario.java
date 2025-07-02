package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import control.UsuarioDAO;

import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Usuario;
import model.enums.Privilegios;

public class TelaCadastroUsuario extends JFrame {
    
    private static final long serialVersionUID = 1L; // Default serialVersion
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JComboBox<Privilegios> cmbPrivilegios;
    private JCheckBox chkAtivo;

    public TelaCadastroUsuario() {
    	// Configuração da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Usuário");
        setSize(400, 200);
        setLayout(new GridLayout(5, 2, 5, 5));

        // Instanciação dos campos
        txtEmail = new JTextField();
        txtSenha = new JPasswordField();
        cmbPrivilegios = new JComboBox<>(Privilegios.values());
        chkAtivo = new JCheckBox("Usuário Ativo", true); // Default para true

        // Adicionando campos e rótulos ao layout, na mesma ordem
        add(new JLabel("Email:"));
        add(txtEmail);
        
        add(new JLabel("Senha:"));
        add(txtSenha);
        
        add(new JLabel("Privilégios:"));
        add(cmbPrivilegios);
        
        add(chkAtivo);
        add(new JLabel()); // Célula vazia para alinhar o layout, como no exemplo

        // Adicionando Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        add(new JLabel()); // Célula vazia

        // ActionListener para o botão, usando a mesma estrutura de classe anônima
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica de criação e salvamento dentro do listener
                Usuario usuario = new Usuario(
                    0, // ID é gerado pelo banco
                    txtEmail.getText(),
                    new String(txtSenha.getPassword()),
                    (Privilegios) cmbPrivilegios.getSelectedItem(),
                    chkAtivo.isSelected()
                );

                UsuarioDAO dao = new UsuarioDAO();
                if (dao.salvar(usuario)) {
                    JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário.");
                }
            }
        });

        // Torna a janela visível ao final do construtor
        setVisible(true);
    }
}
