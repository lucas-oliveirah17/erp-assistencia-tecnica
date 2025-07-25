package view;

import model.Usuario;
import model.enums.Privilegios;

import control.UsuarioDAO;

import view.base.TelaGerenciamentoAbstrata;
import view.components.FormInputV;
import view.components.GerenciamentoButton;
import view.components.GerenciamentoForm;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.Dimension;

import java.util.List;

public class TelaGerenciamentoUsuarios extends TelaGerenciamentoAbstrata {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    // Tabela
	private String nomeTabela = "Lista de Usuários";
    private String[] colunas = {
		"ID", "Nome de Usuário", "Email", "Senha", "Privilégio"
    };
     
    // Campos do formulário
    private String nomeFormulario1   = "Dados do Usuário";
    
    private FormInputV tfId 		 = new FormInputV("ID:", new JTextField());
    private FormInputV tfUsuario 	 = new FormInputV("Nome de Usuário:", new JTextField());
    private FormInputV tfEmail 		 = new FormInputV("Email:", new JTextField());
    private FormInputV pwSenha 		 = new FormInputV("Senha:", new JPasswordField());
    private FormInputV cbPrivilegios = new FormInputV("Privilégio:", new JComboBox<>(Privilegios.values()));
    
    public TelaGerenciamentoUsuarios(Usuario usuarioInstancia) {
    	super(usuarioInstancia);
        
        criarPainelTabela(nomeTabela, colunas);
        
        formularioEsquerdo = new GerenciamentoForm(
        		nomeFormulario1, tfUsuario, tfEmail, pwSenha, cbPrivilegios
        );
    	
        formularioDireito = new GerenciamentoForm();
        
        criarPainelFormulario();
        
        // --- Painel dos Botões ---
        painelBotoes = new GerenciamentoButton(
    		usuarioLogado,
    		new Dimension(90, 30),
    	    e -> atualizarUsuario(),
    	    e -> limparFormulario(),
    	    e -> ativarUsuario(),
    	    e -> desativarUsuario(),
    	    e -> excluirUsuario()	
        );

        finalizarTela();
    }

    @Override
    protected void carregarDadosTabela() {
    	tabela.limparTabela();
        
        UsuarioDAO daoUsuario = new UsuarioDAO();
        
        List<Usuario> usuarios = ckbMostrarInativos.isSelected()
    		? daoUsuario.listarTodos()
            : daoUsuario.listarApenasAtivos();
        
        for (Usuario u : usuarios) {
        	tabela.adicionarLinha(new Object[]{
                u.getId(),
                u.getUsuario(),
                u.getEmail(),
                u.getSenha(),
                u.getPrivilegios(),
            });
        }
        
        // Centralizar as colunas ID
        tabela.centralizarColuna(0);   
    }
    
    @Override
    protected void preencherFormulario(int linha) {
    	try {
	        // Pega os dados da linha selecionada
	        int id = (int) tabela.getValorEm(linha, 0);
	        String usuario = (String) tabela.getValorEm(linha, 1);
	        String email = (String) tabela.getValorEm(linha, 2);
	        Privilegios privilegio = (Privilegios) tabela.getValorEm(linha, 4);
	        
	        // Preenche os campos do formulário
	        tfId.setText(String.valueOf(id));
	        tfUsuario.setText(usuario);
	        tfEmail.setText(email);
	        cbPrivilegios.setSelectedItem(privilegio);
	        pwSenha.setText(""); // Limpa o campo de senha por segurança
    	} catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, 
        		"Erro ao preencher formulário: " + ex.getMessage());
	    }
    }
    
    @Override
    protected void limparFormulario() {
        tfId.setText("");
        tfUsuario.setText("");
        tfEmail.setText("");
        pwSenha.setText("");
        cbPrivilegios.setSelectedItem(null);
        
        tabela.clearSelection();
    }
    
    private void atualizarUsuario() {
    	try {
	        if (tfId.getText().isEmpty()) {
	            JOptionPane.showMessageDialog(this, 
	            	"Selecione um usuário na tabela para atualizar.", 
	            	"Aviso", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        
	        int id = Integer.parseInt(tfId.getText());
	        
	        String nomeUsuario = tfUsuario.getText().trim();
	        String email = tfEmail.getText().trim();
	        
	        String senha = new String(pwSenha.getPassword());	        
	        if (senha.trim().isEmpty()) {	
	            JOptionPane.showMessageDialog(this, 
	            	"Senha não foi alterada.", 
	            	"Aviso", JOptionPane.INFORMATION_MESSAGE);
	            Usuario usuarioBanco = new Usuario();
		        UsuarioDAO busca = new UsuarioDAO();
		        usuarioBanco = busca.buscar(id);
		        senha = usuarioBanco.getSenha();
	        }
	        
	        Privilegios privilegios = (Privilegios) cbPrivilegios.getSelectedItem();
	           
	        // Validações
	        if (nomeUsuario.isEmpty()) {
	            JOptionPane.showMessageDialog(this, 
	        		"O nome de usuário não pode estar vazio.", 
	        		"Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        if (email.isEmpty()) {
	            JOptionPane.showMessageDialog(this, 
	        		"O email não pode estar vazio.", 
	        		"Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	
	        Usuario usuario = new Usuario();
	        usuario.setId(id);
	        usuario.setIdFuncionario(id);
	        usuario.setUsuario(nomeUsuario);
	        usuario.setEmail(email);
	        usuario.setSenha(senha);
	        usuario.setPrivilegios(privilegios);
	        
	        UsuarioDAO dao = new UsuarioDAO();
	        boolean sucesso = dao.atualizar(usuario);
	        
	        if (sucesso) {
	            JOptionPane.showMessageDialog(this, 
	        		"Usuário atualizado com sucesso!");
	            carregarDadosTabela();
	            limparFormulario();
	        } else {
	            JOptionPane.showMessageDialog(this, 
	        		"Erro ao atualizar usuário.",
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
    
    private void ativarUsuario() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário na tabela para ativar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText());
            
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.buscar(id);
            
            if(usuario == null) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário não encontrado.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;     	
            }
            
            if(usuario.isAtivo() == true) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário já está ativo.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja ativar este usuário?", 
                "Confirmação de ativação", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = dao.ativar(usuario);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Usuário ativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao ativar Usuário.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
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
	
    private void desativarUsuario() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário na tabela para desativar.", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(tfId.getText());
            
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.buscar(id);
            
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário não encontrado.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (usuario.isAtivo() == false) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário já está inativo.", 
                    "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja desativar este usuário?", 
                "Confirmação de desativação", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = dao.desativar(usuario);
                
                if (sucesso) {
                    JOptionPane.showMessageDialog(this, 
                        "Usuário desativado com sucesso!");
                    carregarDadosTabela();
                    limparFormulario();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao desativar usuário.", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
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
    
    private void excluirUsuario() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um usuário na tabela para excluir.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja excluir este usuário? Essa é uma exclusão em cascata. "
    		+ "\nTodos os dados referentes a esse usuário serão excluídos definitivamente.",
    		"Confirmação de Exclusão", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
        	try {
	    		int id = Integer.parseInt(tfId.getText());
	    		String nomeUsuario = tfUsuario.getText().trim();
	            String email = tfEmail.getText().trim();
	            String senha = pwSenha.getPassword().trim();
	            Privilegios privilegios = (Privilegios) cbPrivilegios.getSelectedItem();
	            
	            Usuario usuario = new Usuario();
	            usuario.setId(id);
	            usuario.setUsuario(nomeUsuario);
	            usuario.setEmail(email);
	            usuario.setSenha(senha);
	            usuario.setPrivilegios(privilegios);
	   
	            UsuarioDAO dao = new UsuarioDAO();
	            boolean sucesso = dao.excluir(usuario);
	            
	            if (sucesso) {
	                JOptionPane.showMessageDialog(this, 
	            		"Usuário excluído com sucesso!");
	                carregarDadosTabela();
	                limparFormulario();
	            } else {
	                JOptionPane.showMessageDialog(this, 
	            		"Erro ao excluir usuário.",
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