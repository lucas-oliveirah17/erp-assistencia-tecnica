package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dao.ClienteDAO;
import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

public class TelaCadastroCliente extends JFrame {
	private static final long serialVersionUID = 1L; // Default serialVersion
	private JTextField txtNome, txtCpfCnpj, txtTelefone, txtEmail, txtEndereco, txtNumero,
                       txtBairro, txtCidade, txtCep, txtComplemento;
    private JComboBox<TipoCliente> cmbTipo;
    private JComboBox<Uf> cmbUf;

    public TelaCadastroCliente() {
    	// Configuração da janela
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Cadastro de Cliente");
        setSize(400, 600);
        setLayout(new GridLayout(13, 2));

        // Campos
        txtNome = new JTextField();
        txtCpfCnpj = new JTextField();
        cmbTipo = new JComboBox<>(TipoCliente.values());
        txtTelefone = new JTextField();
        txtEmail = new JTextField();
        txtEndereco = new JTextField();
        txtNumero = new JTextField();
        txtBairro = new JTextField();
        txtCidade = new JTextField();
        cmbUf = new JComboBox<>(Uf.values());
        txtCep = new JTextField();
        txtComplemento = new JTextField();

        // Adicionando campos ao layout
        add(new JLabel("Nome:")); add(txtNome);
        add(new JLabel("CPF/CNPJ:")); add(txtCpfCnpj);
        add(new JLabel("Tipo:")); add(cmbTipo);
        add(new JLabel("Telefone:")); add(txtTelefone);
        add(new JLabel("Email:")); add(txtEmail);
        add(new JLabel("Endereço:")); add(txtEndereco);
        add(new JLabel("Número:")); add(txtNumero);
        add(new JLabel("Bairro:")); add(txtBairro);
        add(new JLabel("Cidade:")); add(txtCidade);
        add(new JLabel("UF:")); add(cmbUf);
        add(new JLabel("CEP:")); add(txtCep);
        add(new JLabel("Complemento:")); add(txtComplemento);
        
        // Adicionando Botão Salvar
        JButton btnSalvar = new JButton("Salvar");
        add(btnSalvar);
        add(new JLabel()); // espaço vazio

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Cliente cliente = new Cliente(
                    0,
                    txtNome.getText(),
                    txtCpfCnpj.getText(),
                    (TipoCliente) cmbTipo.getSelectedItem(),
                    txtTelefone.getText(),
                    txtEmail.getText(),
                    txtEndereco.getText(),
                    txtNumero.getText(),
                    txtBairro.getText(),
                    txtCidade.getText(),
                    (Uf) cmbUf.getSelectedItem(),
                    txtCep.getText(),
                    txtComplemento.getText(),
                    true
                );

                ClienteDAO dao = new ClienteDAO();
                if (dao.salvar(cliente)) {
                    JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao cadastrar cliente.");
                }
            }
        });

        setVisible(true);
    }
}
