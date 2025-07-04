package view;

import util.EntradaFormsComboBox;
import util.EntradaFormsTextField;
import util.PainelBotoesUtil;
import util.PainelFormularioUtil;
import util.TabelaUtils;

import model.Cliente;
import model.Usuario;

import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
    private EntradaFormsComboBox<TipoCliente> cbTipo = new EntradaFormsComboBox<>("Tipo de Cliente:", TipoCliente.values());;
    private EntradaFormsTextField tfTelefone = new EntradaFormsTextField("Telefone:");
    private EntradaFormsTextField tfEmail = new EntradaFormsTextField("Email:");
    private EntradaFormsTextField tfEndereco = new EntradaFormsTextField("Endereço:"); 
    private EntradaFormsTextField tfNumero = new EntradaFormsTextField("Número:");
    private EntradaFormsTextField tfComplemento = new EntradaFormsTextField("Complemento::");
    private EntradaFormsTextField tfBairro = new EntradaFormsTextField("Bairro::");
    private EntradaFormsTextField tfCidade = new EntradaFormsTextField("Cidade::");
    private EntradaFormsComboBox<Uf> cbUf = new EntradaFormsComboBox<>("UF:", Uf.values());;
    private EntradaFormsTextField tfCep = new EntradaFormsTextField("CEP:");
        
    // Checkbox de filtro de exibição
    private JCheckBox ckbMostrarInativos = new JCheckBox("Mostrar Inativos");

    public TelaGerenciamentoClientes(Usuario usuarioInstancia) {
    	this.usuarioLogado = usuarioInstancia;
    	
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

        // Cria formulário Dados
        painelFormulario.add(PainelFormularioUtil.criarPainelFormulario(
    		"Dados", tfNome, tfCpfCnpj, cbTipo, tfTelefone, tfEmail), gbc
        );
        

        // Painel de Endereço (coluna direita)
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 0, 0, 0); // Define margens internas. 5 para esquerda a direita.
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        // Cria formulário Endereço Residencial
        painelFormulario.add(PainelFormularioUtil.criarPainelFormulario(
    		"Endereço Residencial", tfEndereco, tfNumero, tfComplemento,
    		tfBairro, tfCidade, cbUf, tfCep), gbc
        );
         
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
        PainelBotoesUtil painelBotoes = new PainelBotoesUtil(
    		usuarioLogado,
    		new Dimension(90, 30),
    	    e -> new TelaCadastroCliente(this),
    	    e -> atualizarCliente(),
    	    e -> limparFormulario(),
    	    e -> ativarCliente(),
    	    e -> desativarCliente(),
    	    e -> excluirCliente()	
        );
           
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
        
        // Ajustar largura das colunas ID, Número e UF
        TabelaUtils.larguraColunas(tabelaClientes, 40, 0, 7, 11);
        
        // Centralizar as colunas ID, Número e UF
        TabelaUtils.centralizarTextoColunas(tabelaClientes, 0, 7, 11);   
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
                JOptionPane.showMessageDialog(this, 
            		"Selecione um cliente na tabela para atualizar.", 
            		"Aviso", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.showMessageDialog(this, 
            		"O nome não pode estar vazio.", 
            		"Erro", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, 
            		"Cliente atualizado com sucesso!");
                carregarDadosTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
            		"Erro ao atualizar cliente.", 
            		"Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", 
        		"Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), 
        		"Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ativarCliente() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para ativar.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, 
                		"Cliente ativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao ativar cliente.", 
                		"Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
            		"ID inválido.", 
            		"Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
            		"Erro inesperado: " + e.getMessage(), 
            		"Erro", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, 
            			"Cliente desativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao desativar cliente.", 
                		"Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
            		"ID inválido.", 
            		"Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
            		"Erro inesperado: " + e.getMessage(), 
            		"Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void excluirCliente() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para excluir.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
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
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao excluir cliente.", 
                		"Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
            		"ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
            		"Erro inesperado: " + e.getMessage(), 
            		"Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}