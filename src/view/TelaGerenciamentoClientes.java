package view;

import model.Cliente;
import model.Usuario;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;
import control.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaGerenciamentoClientes extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    
    private JTextField tfId = new JTextField();
    private JTextField tfNome = new JTextField();
    private JTextField tfCpfCnpj = new JTextField();
    private JComboBox<TipoCliente> cbTipo = new JComboBox<>(TipoCliente.values());;
    private JTextField tfTelefone = new JTextField();
    private JTextField tfEmail = new JTextField();
    private JTextField tfEndereco = new JTextField(); 
    private JTextField tfNumero = new JTextField();
    private JTextField tfComplemento = new JTextField();
    private JTextField tfBairro = new JTextField();
    private JTextField tfCidade = new JTextField();
    private JComboBox<Uf> cbUf = new JComboBox<>(Uf.values());;
    private JTextField tfCep = new JTextField();
    
    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar"); 
    private JButton btnExcluir = new JButton("Excluir");

    public TelaGerenciamentoClientes() {
    	// -- CONFIGURAÇÕES DO PAINEL --
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // --- PAINEL DA TABELA (LISTAR) ---
        JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Fixar altura da tabela

        // Cabeçalho da tabela
        String[] colunas = {
    		"ID", "Nome", "CPF/CNPJ", "Tipo", "Telefone",
    		"Email", "Endereco", "Nº", "Complemento",
    		"Bairro", "Cidade", "UF", "CEP"
        };
        
        // DESATIVA EDIÇÂO NA LINHA DA TABELA
        tableModel = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;  // nenhuma célula será editável
            }
        };
        
        tabelaClientes = new JTable(tableModel);
                
        carregarDadosTabela(); // Carrega a lista de dados
              
        painelTabela.add(new JScrollPane(tabelaClientes));
        
        // -- PAINEL DADOS GERAIS DO CLIENTE --
        JPanel painelCliente = new JPanel();
        painelCliente.setLayout(new GridLayout(1, 2, 10, 0)); // 1 linha, 2 colunas, 10px espaço entre colunas

        // Coluna 1
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

        // Coluna 2
        // -- PAINEL ENDEREÇO --
        JPanel painelEndereco = new JPanel(new GridBagLayout());
        painelEndereco.setBorder(BorderFactory.createTitledBorder("Endereço Residencial"));
        painelEndereco.setAlignmentY(Component.TOP_ALIGNMENT);
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
                
        painelCliente.add(painelDadosGerais);
        painelCliente.add(painelEndereco);
        
        // PREENCHE FORMULARIO AO CLICAR EM ALGUMA LINHA DA TABELA
        tabelaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaClientes.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });
                  
        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));      
           
        // AÇÕES DOS BOTÕES
        btnNovo.addActionListener(e -> new TelaCadastroCliente(this));
        btnAtualizar.addActionListener(e -> atualizarCliente());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirCliente());
        
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnLimpar);        
        painelBotoes.add(btnExcluir);
       
        // -- MONTAGEM FINAL --
        add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertical
        add(painelCliente);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertical
        add(painelBotoes);
    }
    
    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.listarTodos();

        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                c.getCpfCnpj(),
                c.getTipo(),
                c.getTelefone(),
                c.getEmail(),
                c.getEndereco(),
                c.getNumero(),
                c.getComplemento(),
                c.getBairro(),
                c.getCidade(),
                c.getUf(),
                c.getCep(),
            });
        }
    }
    
    private void preencherFormulario(int linha) {
        try {
    	// Pega os dados da linha selecionada
        int id = (int) tableModel.getValueAt(linha, 0);
        String nome = (String) tableModel.getValueAt(linha, 1);
        String cpfCnpj = (String) tableModel.getValueAt(linha, 2);
        TipoCliente tipo = TipoCliente.fromString(tableModel.getValueAt(linha, 3).toString());
        String telefone = (String) tableModel.getValueAt(linha, 4);
        String email = (String) tableModel.getValueAt(linha, 5);
        String endereco = (String) tableModel.getValueAt(linha, 6);
        String numero = (String) tableModel.getValueAt(linha, 7);
        String complemento = (String) tableModel.getValueAt(linha, 8);
        String bairro = (String) tableModel.getValueAt(linha, 9);
        String cidade = (String) tableModel.getValueAt(linha, 10);
        Uf uf = Uf.fromString(tableModel.getValueAt(linha, 11).toString());
        String cep = (String) tableModel.getValueAt(linha, 12);
        
        // Preenche os campos do formulário
        tfId.setText(String.valueOf(id));
        tfNome.setText(String.valueOf(nome));
        tfCpfCnpj.setText(String.valueOf(cpfCnpj));
        cbTipo.setSelectedItem(tipo);
        tfTelefone.setText(String.valueOf(telefone));
        tfEmail.setText(String.valueOf(email));
        tfEndereco.setText(String.valueOf(endereco));
        tfNumero.setText(String.valueOf(numero));
        tfComplemento.setText(String.valueOf(complemento));
        tfBairro.setText(String.valueOf(bairro));
        tfCidade.setText(String.valueOf(cidade));
        tfCep.setText(String.valueOf(cep));
        cbUf.setSelectedItem(uf);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao preencher formulário: " + ex.getMessage());
        }
    }
    
    private void limparFormulario() {
        tfId.setText("");
        tfNome.setText("");
        tfCpfCnpj.setText("");
        cbTipo.setSelectedIndex(0);
        tfTelefone.setText("");
        tfEmail.setText("");
        tfEndereco.setText("");
        tfNumero.setText("");
        tfComplemento.setText("");
        tfBairro.setText("");
        tfCidade.setText("");
        tfCep.setText("");
        cbUf.setSelectedIndex(0);
    }
    
    private void atualizarCliente() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfId.getText());
            String nome = tfNome.getText().trim();
            String cpfCnpj = tfCpfCnpj.getText().trim();
            TipoCliente tipo = (TipoCliente) cbTipo.getSelectedItem();
            String telefone = tfTelefone.getText().trim();
            String email = tfEmail.getText().trim();
            String endereco = tfEndereco.getText().trim();
            String numero = tfNumero.getText().trim();
            String complemento = tfComplemento.getText().trim();
            String bairro = tfBairro.getText().trim();
            String cidade = tfCidade.getText().trim();
            Uf uf = (Uf) cbUf.getSelectedItem();
            String cep = tfCep.getText().trim();

            // Validações
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Cliente cliente = new Cliente();
            cliente.setId(id);
            cliente.setNome(nome);
            cliente.setCpfCnpj(cpfCnpj);
            cliente.setTipo(tipo);
            cliente.setTelefone(telefone);
            cliente.setEmail(email);
            cliente.setEndereco(endereco);
            cliente.setNumero(numero);
            cliente.setComplemento(complemento);
            cliente.setBairro(bairro);
            cliente.setCidade(cidade);
            cliente.setUf(uf);
            cliente.setCep(cep);

            ClienteDAO dao = new ClienteDAO();
            boolean sucesso = dao.atualizar(cliente);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                carregarDadosTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirCliente() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja desativar este cliente?", 
    		"Confirmação de Desativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tfId.getText());
                
                Cliente cliente = new Cliente();
                cliente.setId(id);
                
                ClienteDAO dao = new ClienteDAO();
                boolean sucesso = dao.desativar(cliente);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}