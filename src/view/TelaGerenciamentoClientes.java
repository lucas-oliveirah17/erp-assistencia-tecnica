package view;

import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaGerenciamentoClientes extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    // Componentes da tabela de clientes
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    
    // Campos do formulário
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
    
    // Botões de ação
    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar"); 
    private JButton btnExcluir = new JButton("Excluir");

    public TelaGerenciamentoClientes() {
    	// Define o layout do painel principal como um BoxLayout na direção vertical (Y_AXIS),
    	// ou seja, os componentes serão empilhados verticalmente (um abaixo do outro).
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // --- Painel da Tabela (Lista de Clientes) ---
        JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Fixar altura da tabela

        // Cabeçalhos da tabela
        String[] colunas = {
    		"ID", "Nome", "CPF/CNPJ", "Tipo", "Telefone",
    		"Email", "Endereco", "Nº", "Complemento",
    		"Bairro", "Cidade", "UF", "CEP"
        };
        
        // Modelo da tabela com células não editáveis
        tableModel = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabelaClientes = new JTable(tableModel);
                
        carregarDadosTabela(); // Preenche a tabela com dados do banco
              
        painelTabela.add(new JScrollPane(tabelaClientes));
        
        // --- Painel do Formulário ---
        JPanel painelFormulario = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        // Painel de Dados Gerais (coluna esquerda)
        gbc.gridx = 0; // Define que o componente será colocado na coluna 0 do GridBagLayout
        gbc.weightx = 0.5; // Define o "peso" horizontal. 0.5 para ocupar metade do painel
        gbc.weighty = 0; // 0 para não crescer verticalmente
        gbc.insets = new Insets(0, 0, 0, 5); // Define margens internas. 5 para espaço a direita.
        gbc.fill = GridBagConstraints.HORIZONTAL; // Permite expandir horizontalmente
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; // Alinha topo esquerdo
        painelFormulario.add(criarPainelDadosGerais(), gbc);

        // Painel de Endereço (coluna direita)
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        painelFormulario.add(criarPainelEndereco(), gbc);
 
        // Evento para preencher o formulário ao clicar na tabela
        tabelaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaClientes.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });
                  
        // --- Painel de Botões ---
	    // Cria um painel FlowLayout alinhado à direita.
	    // 10 → espaçamento horizontal entre os componentes (botões), em pixels.
	    // 5 → espaçamento vertical entre os componentes e as bordas do painel, em pixels.
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));      
           
        // Define ações dos botões
        btnNovo.addActionListener(e -> new TelaCadastroCliente(this));
        btnAtualizar.addActionListener(e -> atualizarCliente());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnExcluir.addActionListener(e -> excluirCliente());
        
        // Define tamanho fixo dos botões (largura, altura)
        Dimension tamanhoBotao = new Dimension(90, 30);
        btnNovo.setPreferredSize(tamanhoBotao);
        btnAtualizar.setPreferredSize(tamanhoBotao);
        btnLimpar.setPreferredSize(tamanhoBotao);
        btnExcluir.setPreferredSize(tamanhoBotao);
        
        // Adiciona botões ao painel
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnLimpar);        
        painelBotoes.add(btnExcluir);
       
        // --- Montagem Final ---
        add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertica
        add(painelFormulario);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertical
        add(painelBotoes);
    }
    
    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.listarApenasAtivos();

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
        
        centralizarTextoColunas(tabelaClientes, 0); // coluna ID
        larguraColuna(tabelaClientes, 0, 40); // (tabela, coluna, largura) 
        
        centralizarTextoColunas(tabelaClientes, 7); // coluna Numero
        larguraColuna(tabelaClientes, 7, 40);
        
        centralizarTextoColunas(tabelaClientes, 11); // coluna UF
        larguraColuna(tabelaClientes, 11, 40);
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
    
    private void larguraColuna(JTable tabela, int indiceColuna, int largura) {
        TableColumn coluna = tabela.getColumnModel().getColumn(indiceColuna);
        coluna.setMinWidth(largura);
        coluna.setPreferredWidth(largura);
    }
    
    private void centralizarTextoColunas(JTable table, int... indices) {
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i : indices) {
            table.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }
    }
    
    private JPanel criarPainelDadosGerais() {
    	// O GridBagLayout permite posicionar componentes em uma grade flexível,
        JPanel painelDadosGerais = new JPanel(new GridBagLayout());
        painelDadosGerais.setBorder(BorderFactory.createTitledBorder("Dados"));
        painelDadosGerais.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START; // Alinhamento no canto esquerdo
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

        return painelDadosGerais;
    }

    
    private JPanel criarPainelEndereco() {
        JPanel painelEndereco = new JPanel(new GridBagLayout());
        painelEndereco.setBorder(BorderFactory.createTitledBorder("Endereço Residencial"));
        painelEndereco.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START; // Alinha os componentes à esquerda
        gbc.weightx = 1.0;

        int linha = 0;

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

        return painelEndereco;
    }
}