package view;

import util.EntradaFormsTextField;
import util.EntradaFormsComboBox;

import model.Cliente;
import model.Usuario;
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
    private Usuario usuarioLogado;
    
    // Componentes da tabela de clientes
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    
    // Campos do formulário
    private EntradaFormsTextField tfId = new EntradaFormsTextField("ID:");
    private EntradaFormsTextField tfNome = new EntradaFormsTextField("Nome:");
    private EntradaFormsTextField tfCpfCnpj = new EntradaFormsTextField("CPF/CNPJ:");
    private EntradaFormsComboBox<TipoCliente> cbTipo = new EntradaFormsComboBox<>("Tipo de Cliente:",TipoCliente.values());;
    private EntradaFormsTextField tfTelefone = new EntradaFormsTextField("Telefone:");
    private EntradaFormsTextField tfEmail = new EntradaFormsTextField("Email:");
    private EntradaFormsTextField tfEndereco = new EntradaFormsTextField("Endereço:"); 
    private EntradaFormsTextField tfNumero = new EntradaFormsTextField("Número:");
    private EntradaFormsTextField tfComplemento = new EntradaFormsTextField("Complemento::");
    private EntradaFormsTextField tfBairro = new EntradaFormsTextField("Bairro::");
    private EntradaFormsTextField tfCidade = new EntradaFormsTextField("Cidade::");
    private EntradaFormsComboBox<Uf> cbUf = new EntradaFormsComboBox<>("UF:",Uf.values());;
    private EntradaFormsTextField tfCep = new EntradaFormsTextField("CEP:");
    
    // Botões de ação
    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar");
    private JButton btnAtivar = new JButton("Ativar");
    private JButton btnDesativar = new JButton("Desativar");
    private JButton btnExcluir = new JButton("Excluir");
    
    // Checkbox de filtro de exibição
    private JCheckBox ckbMostrarInativos = new JCheckBox("Mostrar Inativos");

    public TelaGerenciamentoClientes(Usuario usuario) {
    	this.usuarioLogado = usuario;
    	
    	// Define o layout do painel principal como um BoxLayout na direção vertical (Y_AXIS),
    	// ou seja, os componentes serão empilhados verticalmente (um abaixo do outro).
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    	// Painel filtro (Admin)
    	JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        painelFiltro.add(ckbMostrarInativos);
        
        // --- Painel da Tabela (Lista de Clientes) ---
        JPanel painelTabela = new JPanel();
        
        painelTabela = criarPainelTabela();
        
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
                  
        // --- Painel dos Botões ---
        JPanel painelBotoes = new JPanel();
        painelBotoes = criarPainelBotoes();
           
        // --- Montagem Final ---
        if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
        	add(painelFiltro);
            add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertica
        }
        add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertica
        add(painelFormulario);
        add(Box.createRigidArea(new Dimension(0, 10))); // espaçamento vertical
        add(painelBotoes);
    }
    
    private JPanel criarPainelTabela() {
    	JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        // painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        painelTabela.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Lista de Clientes"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
        painelTabela.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Fixar altura da tabela
        
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
        
        // Preenche a tabela com dados do banco
        carregarDadosTabela();
        
        // Monta tabela ao painel
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);
        int larguraPainel = this.getWidth();
        scrollPane.setPreferredSize(new Dimension(larguraPainel, 200)); // Altura da tabela
        painelTabela.add(scrollPane);
    	
        return painelTabela;
    }
    
    private JPanel criarPainelDadosGerais() {
        JPanel painelDadosGerais = new JPanel();
        painelDadosGerais.setLayout(new BoxLayout(painelDadosGerais, BoxLayout.Y_AXIS));
        painelDadosGerais.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Endereço Residencial"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
        painelDadosGerais.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo

 
        // Adiciona os componentes ao painel com espaçamento
        painelDadosGerais.add(tfNome);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfCpfCnpj);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(cbTipo);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfTelefone);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfEmail);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        return painelDadosGerais;
    }
  
    private JPanel criarPainelEndereco() {
        JPanel painelEndereco = new JPanel();
        painelEndereco.setLayout(new BoxLayout(painelEndereco, BoxLayout.Y_AXIS));
        painelEndereco.setBorder(BorderFactory.createCompoundBorder(
    	    BorderFactory.createTitledBorder("Endereço Residencial"),
    	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
        ));
        painelEndereco.setAlignmentY(Component.TOP_ALIGNMENT);

        // Adiciona os componentes ao painel com espaçamento
        painelEndereco.add(tfEndereco);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(tfNumero);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(tfComplemento);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(tfBairro);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(tfCidade);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(cbUf);
        painelEndereco.add(Box.createVerticalStrut(10));

        painelEndereco.add(tfCep);
        painelEndereco.add(Box.createVerticalStrut(10));

        return painelEndereco;
    }
    
    private JPanel criarPainelBotoes() {
    	// --- Painel de Botões ---
	    // Cria um painel FlowLayout alinhado à direita.
	    // 10 → espaçamento horizontal entre os componentes (botões), em pixels.
	    // 5 → espaçamento vertical entre os componentes e as bordas do painel, em pixels.
    	JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
    	
    	// Define ações dos botões
        btnNovo.addActionListener(e -> new TelaCadastroCliente(this));
        btnAtualizar.addActionListener(e -> atualizarCliente());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnAtivar.addActionListener(e -> ativarCliente());
        btnDesativar.addActionListener(e -> desativarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        
        // Define tamanho fixo dos botões (largura, altura)
        Dimension tamanhoBotao = new Dimension(90, 30);
        btnNovo.setPreferredSize(tamanhoBotao);
        btnAtualizar.setPreferredSize(tamanhoBotao);
        btnLimpar.setPreferredSize(tamanhoBotao);
        btnAtivar.setPreferredSize(tamanhoBotao);
        btnDesativar.setPreferredSize(tamanhoBotao);
        btnExcluir.setPreferredSize(tamanhoBotao);
        
        // Adiciona botões ao painel
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnLimpar);
        if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
        	painelBotoes.add(btnAtivar);
        }
        painelBotoes.add(btnDesativar);
        if(usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
        	painelBotoes.add(btnExcluir);
        }
        
        return painelBotoes;
    }
    
    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        
        
        ClienteDAO dao = new ClienteDAO();
        
        List<Cliente> clientes = ckbMostrarInativos.isSelected()
                ? dao.listarTodos()
                : dao.listarApenasAtivos();

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
        tfId.setValor(id);
        tfNome.setValor(nome);
        tfCpfCnpj.setValor(cpfCnpj);
        cbTipo.setValor(tipo);
        tfTelefone.setValor(telefone);
        tfEmail.setValor(email);
        tfEndereco.setValor(endereco);
        tfNumero.setValor(numero);
        tfComplemento.setValor(complemento);
        tfBairro.setValor(bairro);
        tfCidade.setValor(cidade);
        tfCep.setValor(cep);
        cbUf.setValor(uf);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao preencher formulário: " + ex.getMessage());
        }
    }
    
    private void limparFormulario() {
    	tfId.setValor("");
        tfNome.setValor("");
        tfCpfCnpj.setValor("");
        cbTipo.setValor(null);
        tfTelefone.setValor("");
        tfEmail.setValor("");
        tfEndereco.setValor("");
        tfNumero.setValor("");
        tfComplemento.setValor("");
        tfBairro.setValor("");
        tfCidade.setValor("");
        tfCep.setValor("");
        cbUf.setValor(null);
    }
    
    private void atualizarCliente() {
        try {
            if (tfId.getValor().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfId.getValor());
            String nome = tfNome.getValor().trim();
            String cpfCnpj = tfCpfCnpj.getValor().trim();
            TipoCliente tipo = (TipoCliente) cbTipo.getValor();
            String telefone = tfTelefone.getValor().trim();
            String email = tfEmail.getValor().trim();
            String endereco = tfEndereco.getValor().trim();
            String numero = tfNumero.getValor().trim();
            String complemento = tfComplemento.getValor().trim();
            String bairro = tfBairro.getValor().trim();
            String cidade = tfCidade.getValor().trim();
            Uf uf = (Uf) cbUf.getValor();
            String cep = tfCep.getValor().trim();
            
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
    
    private void ativarCliente() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja ativar este cliente?", 
    		"Confirmação de ativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                
                Cliente cliente = new Cliente();
                cliente.setId(id);
                
                ClienteDAO dao = new ClienteDAO();
                boolean sucesso = dao.ativar(cliente);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Cliente ativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao ativar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void desativarCliente() {
        if (tfId.getValor().isEmpty()) {
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
            	int id = Integer.parseInt(tfId.getValor());
                
                Cliente cliente = new Cliente();
                cliente.setId(id);
                
                ClienteDAO dao = new ClienteDAO();
                boolean sucesso = dao.desativar(cliente);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, "Cliente desativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao desativar cliente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void excluirCliente() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para excluir.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja excluir este cliente? Essa é uma exclusão em cascata. "
    		+ "\nTodos os dados referentes a esse cliente serão excluídos definitivamente.", 
    		"Confirmação de exclusão", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                String nome = tfNome.getValor().trim();
                String cpfCnpj = tfCpfCnpj.getValor().trim();
                TipoCliente tipo = (TipoCliente) cbTipo.getValor();
                String telefone = tfTelefone.getValor().trim();
                String email = tfEmail.getValor().trim();
                String endereco = tfEndereco.getValor().trim();
                String numero = tfNumero.getValor().trim();
                String complemento = tfComplemento.getValor().trim();
                String bairro = tfBairro.getValor().trim();
                String cidade = tfCidade.getValor().trim();
                Uf uf = (Uf) cbUf.getValor();
                String cep = tfCep.getValor().trim();
                
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
                boolean sucesso = dao.excluir(cliente);
                
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
}