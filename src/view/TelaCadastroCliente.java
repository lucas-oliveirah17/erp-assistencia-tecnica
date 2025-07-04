package view;

import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaCadastroCliente extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private TelaGerenciamentoClientes painelGerenciamento;

    // -- COMPONENTES DE ENTRADA --
    private JTextField tfNome = new JTextField();
    private JTextField tfCpfCnpj = new JTextField();
    private JComboBox<TipoCliente> cbTipo = new JComboBox<>(TipoCliente.values());
    private JTextField tfTelefone = new JTextField();
    private JTextField tfEmail = new JTextField();
    private JTextField tfEndereco = new JTextField();
    private JTextField tfNumero = new JTextField();
    private JTextField tfComplemento = new JTextField();
    private JTextField tfBairro = new JTextField();
    private JTextField tfCidade = new JTextField();
    private JComboBox<Uf> cbUf = new JComboBox<>(Uf.values());
    private JTextField tfCep = new JTextField();

    private JButton btnSalvar = new JButton("Salvar");
    private JButton btnCancelar = new JButton("Cancelar");

    // -- FONTE E TAMANHO DAS ENTRADAS --
    private Font panelFont = new Font("Arial", Font.BOLD, 14);
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private Dimension inputSize = new Dimension(200, 25);
    
    public TelaCadastroCliente() {
    	this((TelaGerenciamentoClientes) null);
    }
    
    public TelaCadastroCliente(TelaGerenciamentoClientes painelGerenciamento) {
        this.painelGerenciamento = painelGerenciamento;
        inicializar();
    }
    
    public void inicializar() {
    	// -- CONFIGURAÇÕES DA JANELA --
        this.setTitle("Cadastro de Cliente");
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
        painelDadosGerais.add(tfNome, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("CPF/CNPJ:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(tfCpfCnpj, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelDadosGerais.add(new JLabel("Tipo de Cliente:"), gbc);
        gbc.gridx = 1;
        painelDadosGerais.add(cbTipo, gbc);

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

        // -- PAINEL ENDEREÇO --
        JPanel painelEndereco = new JPanel(new GridBagLayout());
        painelEndereco.setBorder(BorderFactory.createTitledBorder("Endereço Residencial"));
        linha = 0;

        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfEndereco, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfNumero, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfComplemento, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfBairro, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfCidade, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("UF:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(cbUf, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelEndereco.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1;
        painelEndereco.add(tfCep, gbc);
        
        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));      
           
        btnSalvar.addActionListener(this::salvarCliente);
        btnCancelar.addActionListener(e -> dispose());
        
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        
        // --- ESTILIZAÇÃO --
        ((TitledBorder) painelDadosGerais.getBorder()).setTitleFont(panelFont);
        ((TitledBorder) painelEndereco.getBorder()).setTitleFont(panelFont);

        estilizarComponentes(painelDadosGerais, inputSize);
        estilizarComponentes(painelEndereco, inputSize);

        // -- MONTAGEM FINAL --
        painelPrincipal.add(painelDadosGerais);
        painelPrincipal.add(Box.createVerticalStrut(10)); // Cria um espaçamento de 10px entre os paineis
        painelPrincipal.add(painelEndereco);
        painelPrincipal.add(Box.createVerticalStrut(10));
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
    
    private void salvarCliente(ActionEvent e) {
        Cliente cliente = new Cliente();
        try {
            cliente.setNome(tfNome.getText());
            cliente.setCpfCnpj(tfCpfCnpj.getText());
            cliente.setTipo((TipoCliente) cbTipo.getSelectedItem());
            cliente.setTelefone(tfTelefone.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setEndereco(tfEndereco.getText());
            cliente.setNumero(tfNumero.getText());
            cliente.setComplemento(tfComplemento.getText());
            cliente.setBairro(tfBairro.getText());
            cliente.setCidade(tfCidade.getText());
            cliente.setUf((Uf) cbUf.getSelectedItem());
            cliente.setCep(tfCep.getText());
            cliente.setAtivo(true);

            ClienteDAO dao = new ClienteDAO();
            boolean sucesso = dao.salvar(cliente);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar cliente.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}