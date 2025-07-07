package view;


import model.Funcionario;
import model.Usuario;
import model.enums.FuncaoFuncionario;

import view.base.TelaGerenciamentoAbstrata;
import view.components.FormInputV;
import view.components.GerenciamentoButton;
import view.components.GerenciamentoForm;

import control.FuncionarioDAO;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Dimension;

import java.util.List;

public class TelaGerenciamentoFuncionarios extends TelaGerenciamentoAbstrata {
	private static final long serialVersionUID = 1L; // Default serialVersion
        
	// Campos do formulário
    private FormInputV tfId 	  = new FormInputV("ID:", new JTextField());
    private FormInputV tfNome 	  = new FormInputV("Nome:", new JTextField());
    private FormInputV tfCpf 	  = new FormInputV("CPF:", new JTextField());
    private FormInputV cbFuncao   = new FormInputV("Função:", new JComboBox<>(FuncaoFuncionario.values()));
    private FormInputV tfTelefone = new FormInputV("Telefone:", new JTextField());
    private FormInputV tfEmail    = new FormInputV("Email:", new JTextField());
    
	public TelaGerenciamentoFuncionarios(Usuario usuarioInstancia) {
		super(usuarioInstancia);
		
		// Tabela
		String nomeTabela = "Lista de Funcionários";
        String[] colunas = {
    		"ID", "Nome", "CPF", "Função", "Telefone", "Email"
        };
        
        criarPainelTabela(nomeTabela, colunas);
        
        formularioEsquerdo = new GerenciamentoForm(
        		"Dados", tfNome, tfCpf, cbFuncao, tfTelefone, tfEmail
        );
        
        formularioDireito = new GerenciamentoForm();
        
        criarPainelFormulario();
        
     // --- Painel dos Botões ---
        painelBotoes = new GerenciamentoButton(
    		usuarioLogado,
    		new Dimension(90, 30),
    	    e -> new TelaCadastroFuncionario(this),
    	    e -> atualizarFuncionario(),
    	    e -> limparFormulario(),
    	    e -> ativarFuncionario(),
    	    e -> desativarFuncionario(),
    	    e -> excluirFuncionario()	
        );
        
        finalizarTela();
	}
	
	protected void carregarDadosTabela() {
        tabela.limparTabela();
        
        FuncionarioDAO dao = new FuncionarioDAO();
        
        List<Funcionario> funcionario = ckbMostrarInativos.isSelected()
            ? dao.listarTodos()
            : dao.listarApenasAtivos();

        for (Funcionario f : funcionario) {
            tabela.adicionarLinha(new Object[]{
                f.getId(),
                f.getNome(),
                f.getCpf(),
                f.getFuncao(),
                f.getTelefone(),
                f.getEmail(),
            });
        }
        
        // Centralizar as colunas ID, Número e UF
        tabela.centralizarColuna(0, 2, 3, 4);
    }
	
	protected void preencherFormulario(int linha) {
        try {
	    	// Pega os dados da linha selecionada
	        int id = (int) tabela.getValorEm(linha, 0);
	        String nome = (String) tabela.getValorEm(linha, 1);
	        String cpf = (String) tabela.getValorEm(linha, 2);
	        FuncaoFuncionario tipo = FuncaoFuncionario.fromString(tabela.getValorEm(linha, 3).toString());
	        String telefone = (String) tabela.getValorEm(linha, 4);
	        String email = (String) tabela.getValorEm(linha, 5);
	        
	        // Preenche os campos do formulário
	        tfId.setText(String.valueOf(id));
	        tfNome.setText(nome);
	        tfCpf.setText(cpf);
	        cbFuncao.setSelectedItem(tipo);
	        tfTelefone.setText(telefone);
	        tfEmail.setText(email);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
        		"Erro ao preencher formulário: " + ex.getMessage());
        }
    }
	
	protected void limparFormulario() {
    	tfId.setText("");
        tfNome.setText("");
        tfCpf.setText("");
        cbFuncao.setText(null);
        tfTelefone.setText("");
        tfEmail.setText("");
        
        tabela.clearSelection();
    }
	
	private void atualizarFuncionario() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                	"Selecione um funcionário na tabela para atualizar.", 
                	"Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfId.getText());
            String nome = tfNome.getText().trim();
            String cpf = tfCpf.getText().trim();
            FuncaoFuncionario funcao = (FuncaoFuncionario) cbFuncao.getSelectedItem();
            String telefone = tfTelefone.getText().trim();
            String email = tfEmail.getText().trim();
            
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
            JOptionPane.showMessageDialog(this, 
            	"Erro inesperado: " + e.getMessage(), 
        		"Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
	
	private void ativarFuncionario() {
        if (tfId.getText().isEmpty()) {
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
            	int id = Integer.parseInt(tfId.getText());
                
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
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um funcionário na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja desativar este funcionário?", 
    		"Confirmação de desativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getText());
                
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
        if (tfId.getText().isEmpty()) {
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
            	int id = Integer.parseInt(tfId.getText());
                String nome = tfNome.getText().trim();
                String cpf = tfCpf.getText().trim();
                FuncaoFuncionario funcao = (FuncaoFuncionario) cbFuncao.getSelectedItem();
                String telefone = tfTelefone.getText().trim();
                String email = tfEmail.getText().trim();
                
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
