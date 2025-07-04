package view;

import util.EntradaFormsComboBox;
import util.EntradaFormsTextField;
import util.TabelaUtils;

import model.Funcionario;
import model.Usuario;

import model.enums.FuncaoFuncionario;

import control.FuncionarioDAO;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.List;

public class TelaGerenciamentoFuncionarios extends JPanel {
	private static final long serialVersionUID = 1L; // Default serialVersion
    
    private Usuario usuarioLogado;
    
 // Componentes da tabela de funcionário
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;
    
    // Campos do formulário
    private EntradaFormsTextField tfId = new EntradaFormsTextField("ID:");
    private EntradaFormsTextField tfNome = new EntradaFormsTextField("Nome:");
    private EntradaFormsTextField tfCpf = new EntradaFormsTextField("CPF:");
    private EntradaFormsComboBox<FuncaoFuncionario> cbFuncao = new EntradaFormsComboBox<>("Função:", FuncaoFuncionario.values());;
    private EntradaFormsTextField tfTelefone = new EntradaFormsTextField("Telefone:");
    private EntradaFormsTextField tfEmail = new EntradaFormsTextField("Email:");

    // Botões de ação
    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar");
    private JButton btnAtivar = new JButton("Ativar");
    private JButton btnDesativar = new JButton("Desativar");
    private JButton btnExcluir = new JButton("Excluir");
    
    // Checkbox de filtro de exibição
    private JCheckBox ckbMostrarInativos = new JCheckBox("Mostrar Inativos");
	
	public TelaGerenciamentoFuncionarios(Usuario usuarioInstancia) {
		this.usuarioLogado = usuarioInstancia;
		
		// Define o layout do painel principal como um BoxLayout na direção vertical (Y_AXIS),
    	// ou seja, os componentes serão empilhados verticalmente (um abaixo do outro).
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    	// Painel filtro (Admin)
    	JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        painelFiltro.add(ckbMostrarInativos);
        
        // --- Painel da Tabela (Lista de Funcionarios) ---
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
        gbc.insets = new Insets(5, 0, 0, 0); // Define margens internas. 5 para esquerda a direita.
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        painelFormulario.add(criarPainelVazio(), gbc);
        
     // Evento para preencher o formulário ao clicar na tabela
        tabelaFuncionarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
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
        painelTabela.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Lista de Funcionários"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
        painelTabela.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Fixar altura da tabela
        
        // Cabeçalhos da tabela
        String[] colunas = {
    		"ID", "Nome", "CPF", "Função", "Telefone", "Email"
        };
        
        // Modelo da tabela com células não editáveis
        tableModel = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFuncionarios = new JTable(tableModel);
        
        // Preenche a tabela com dados do banco
        carregarDadosTabela();
        
        // Monta tabela ao painel
        JScrollPane scrollPane = new JScrollPane(tabelaFuncionarios);
        int larguraPainel = this.getWidth();
        scrollPane.setPreferredSize(new Dimension(larguraPainel, 200)); // Altura da tabela
        painelTabela.add(scrollPane);
    	
        return painelTabela;
    }
	
	private JPanel criarPainelDadosGerais() {
        JPanel painelDadosGerais = new JPanel();
        painelDadosGerais.setLayout(new BoxLayout(painelDadosGerais, BoxLayout.Y_AXIS));
        painelDadosGerais.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createTitledBorder("Dados"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
        painelDadosGerais.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo

 
        // Adiciona os componentes ao painel com espaçamento
        painelDadosGerais.add(tfNome);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfCpf);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(cbFuncao);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfTelefone);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        painelDadosGerais.add(tfEmail);
        painelDadosGerais.add(Box.createVerticalStrut(10));

        return painelDadosGerais;
    }
	
	private JPanel criarPainelVazio() {
		JPanel painelVazio = new JPanel();
	    painelVazio.setOpaque(false); // Deixa o painel invisível (sem cor de fundo)
	    painelVazio.setLayout(new BorderLayout()); // Layout neutro, ocupa todo o espaço disponível
	    return painelVazio;
	}
	
	private JPanel criarPainelBotoes() {
    	// --- Painel de Botões ---
	    // Cria um painel FlowLayout alinhado à direita.
	    // 10 → espaçamento horizontal entre os componentes (botões), em pixels.
	    // 5 → espaçamento vertical entre os componentes e as bordas do painel, em pixels.
    	JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
    	
    	// Define ações dos botões
        btnNovo.addActionListener(e -> new TelaCadastroFuncionario(this));
        btnAtualizar.addActionListener(e -> atualizarFuncionario());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnAtivar.addActionListener(e -> ativarFuncionario());
        btnDesativar.addActionListener(e -> desativarFuncionario());
        btnExcluir.addActionListener(e -> excluirFuncionario());
        
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
        
        
        FuncionarioDAO dao = new FuncionarioDAO();
        
        List<Funcionario> funcionario = ckbMostrarInativos.isSelected()
                ? dao.listarTodos()
                : dao.listarApenasAtivos();

        for (Funcionario f : funcionario) {
            tableModel.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                f.getCpf(),
                f.getFuncao(),
                f.getTelefone(),
                f.getEmail(),
            });
        }
        
        // Centralizar as colunas ID, CPF, Função e Telefone
        TabelaUtils.centralizarTextoColunas(tabelaFuncionarios, 0, 2, 3, 4);
    }
	
	private void preencherFormulario(int linha) {
        try {
    	// Pega os dados da linha selecionada
        int id = (int) tableModel.getValueAt(linha, 0);
        String nome = (String) tableModel.getValueAt(linha, 1);
        String cpf = (String) tableModel.getValueAt(linha, 2);
        FuncaoFuncionario tipo = FuncaoFuncionario.fromString(tableModel.getValueAt(linha, 3).toString());
        String telefone = (String) tableModel.getValueAt(linha, 4);
        String email = (String) tableModel.getValueAt(linha, 5);
        
        // Preenche os campos do formulário
        tfId.setValor(id);
        tfNome.setValor(nome);
        tfCpf.setValor(cpf);
        cbFuncao.setValor(tipo);
        tfTelefone.setValor(telefone);
        tfEmail.setValor(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao preencher formulário: " + ex.getMessage());
        }
    }
	
	private void limparFormulario() {
    	tfId.setValor("");
        tfNome.setValor("");
        tfCpf.setValor("");
        cbFuncao.setValor(null);
        tfTelefone.setValor("");
        tfEmail.setValor("");
    }
	
	private void atualizarFuncionario() {
        try {
            if (tfId.getValor().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                	"Selecione um funcionário na tabela para atualizar.", 
                	"Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfId.getValor());
            String nome = tfNome.getValor().trim();
            String cpf = tfCpf.getValor().trim();
            FuncaoFuncionario funcao = (FuncaoFuncionario) cbFuncao.getValor();
            String telefone = tfTelefone.getValor().trim();
            String email = tfEmail.getValor().trim();
            
            // Validações
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
            		"O nome não pode estar vazio.", 
            		"Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario funcionario = new Funcionario();
            funcionario.setId(id);
            funcionario.setNome(nome);
            funcionario.setCpf(cpf);
            funcionario.setFuncao(funcao);
            funcionario.setTelefone(telefone);
            funcionario.setEmail(email);

            FuncionarioDAO dao = new FuncionarioDAO();
            boolean sucesso = dao.atualizar(funcionario);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
            		"Funcionário atualizado com sucesso!");
                carregarDadosTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, 
            		"Erro ao atualizar funcionário.", 
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
	
	private void ativarFuncionario() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um funcionário na tabela para ativar.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja ativar este funcionário?", 
    		"Confirmação de ativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                
            	Funcionario funcionario = new Funcionario();
            	funcionario.setId(id);
                
                FuncionarioDAO dao = new FuncionarioDAO();
                boolean sucesso = dao.ativar(funcionario);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                		"Funcionário ativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao ativar funcionário.", 
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
	
	private void desativarFuncionario() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um funcionário na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja desativar este funcionário?", 
    		"Confirmação de Desativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                
                Funcionario funcionario = new Funcionario();
                funcionario.setId(id);
                
                FuncionarioDAO dao = new FuncionarioDAO();
                boolean sucesso = dao.desativar(funcionario);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                		"Funcionário desativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao desativar funcionário.", 
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
	
	private void excluirFuncionario() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um funcionário na tabela para excluir.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja excluir este funcionário? Essa é uma exclusão em cascata. "
    		+ "\nTodos os dados referentes a esse funcionário serão excluídos definitivamente.", 
    		"Confirmação de exclusão", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                String nome = tfNome.getValor().trim();
                String cpf = tfCpf.getValor().trim();
                FuncaoFuncionario funcao = (FuncaoFuncionario) cbFuncao.getValor();
                String telefone = tfTelefone.getValor().trim();
                String email = tfEmail.getValor().trim();
                
                Funcionario funcionario = new Funcionario();
                funcionario.setId(id);
                funcionario.setNome(nome);
                funcionario.setCpf(cpf);
                funcionario.setFuncao(funcao);
                funcionario.setTelefone(telefone);
                funcionario.setEmail(email);
                
                FuncionarioDAO dao = new FuncionarioDAO();
                boolean sucesso = dao.excluir(funcionario);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                		"Funcionário excluído com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                		"Erro ao excluir funcionário.", 
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
}
