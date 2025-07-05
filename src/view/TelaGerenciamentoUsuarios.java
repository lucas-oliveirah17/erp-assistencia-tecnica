package view;

import util.EntradaFormsComboBox;
import util.EntradaFormsPasswordField;
import util.EntradaFormsTextField;
import util.PainelBotoesUtil;
import util.PainelFormularioUtil;
import util.TabelaUtils;

import model.Usuario;

import model.enums.Privilegios;

import control.UsuarioDAO;

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

public class TelaGerenciamentoUsuarios extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private Usuario usuarioLogado;
    
    // Componentes da tabela de usuário
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    
    private EntradaFormsTextField tfId = new EntradaFormsTextField("ID:");
    private EntradaFormsTextField tfEmail = new EntradaFormsTextField("Email:");
    private EntradaFormsPasswordField pwSenha = new EntradaFormsPasswordField("Senha:");
    private EntradaFormsComboBox<Privilegios> cbPrivilegios = new EntradaFormsComboBox<>("Privilégio:", Privilegios.values());

    // Checkbox de filtro de exibição
    private JCheckBox ckbMostrarInativos = new JCheckBox("Mostrar Inativos");
    
    public TelaGerenciamentoUsuarios(Usuario usuarioInstancia) {
    	this.usuarioLogado = usuarioInstancia;
    	
    	// Define o layout do painel principal como um BoxLayout na direção vertical (Y_AXIS),
    	// ou seja, os componentes serão empilhados verticalmente (um abaixo do outro).
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    	
    	// Painel filtro (Admin)
    	JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ckbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        painelFiltro.add(ckbMostrarInativos);

        // --- Painel da Tabela (Lista de Usuário) ---
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

        // Cria formulário
        painelFormulario.add(PainelFormularioUtil.criarPainelFormulario(
    		"Dados", tfEmail, pwSenha, cbPrivilegios), gbc
        );
        
        // Painel de Endereço (coluna direita)
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 0, 0, 0); // Define margens internas. 5 para esquerda a direita.
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        // Cria painel vazio
        painelFormulario.add(PainelFormularioUtil.criarPainelVazio(), gbc);

        // --- Listeners (Ações) ---

        // Evento para preencher o formulário ao clicar na tabela
        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaUsuarios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });
        
        // --- Painel dos Botões ---
        PainelBotoesUtil painelBotoes = new PainelBotoesUtil(
    		usuarioLogado,
    		new Dimension(90, 30),
    	    e -> new TelaCadastroUsuario(this),
    	    e -> atualizarUsuario(),
    	    e -> limparFormulario(),
    	    e -> ativarUsuario(),
    	    e -> desativarUsuario(),
    	    e -> excluirUsuario()	
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
        	    BorderFactory.createTitledBorder("Lista de Usuários"),
        	    BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
            ));
        painelTabela.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200)); // Fixar altura da tabela
        
        // Cabeçalhos da tabela
        String[] colunas = {
    		"ID", "Email", "Senha", "Privilégio"
        };
        
        // Modelo da tabela com células não editáveis
        tableModel = new DefaultTableModel(colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaUsuarios = new JTable(tableModel);
        
        // Preenche a tabela com dados do banco
        carregarDadosTabela();
        
        // Monta tabela ao painel
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        int larguraPainel = this.getWidth();
        scrollPane.setPreferredSize(new Dimension(larguraPainel, 200)); // Altura da tabela
        painelTabela.add(scrollPane);
    	
        return painelTabela;
    }

    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        
        UsuarioDAO dao = new UsuarioDAO();
        
        List<Usuario> usuarios = ckbMostrarInativos.isSelected()
    		? dao.listarTodos()
            : dao.listarApenasAtivos();

        for (Usuario u : usuarios) {
            tableModel.addRow(new Object[]{
                u.getId(),
                u.getEmail(),
                u.getSenha(),
                u.getPrivilegios(),
            });
        }
        
        // Ajustar largura das colunas ID
        TabelaUtils.larguraColunas(tabelaUsuarios, 0);
        
        // Centralizar as colunas ID
        TabelaUtils.centralizarTextoColunas(tabelaUsuarios, 0);
    }
    
    private void preencherFormulario(int linha) {
    	try {
	        // Pega os dados da linha selecionada
	        int id = (int) tableModel.getValueAt(linha, 0);
	        String email = (String) tableModel.getValueAt(linha, 1);
	        Privilegios privilegio = (Privilegios) tableModel.getValueAt(linha, 3);
	        
	        // Preenche os campos do formulário
	        tfId.setValor(String.valueOf(id));
	        tfEmail.setValor(email);
	        cbPrivilegios.setValor(privilegio);
	        pwSenha.setValor(""); // Limpa o campo de senha por segurança
    	} catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Erro ao preencher formulário: " + ex.getMessage());
	    }
    }
    
    private void limparFormulario() {
        tfId.setValor("");
        tfEmail.setValor("");
        pwSenha.setValor("");
        cbPrivilegios.setValor(null);
        
        tabelaUsuarios.clearSelection();
    }
    
    private void atualizarUsuario() {
    	try {
	        if (tfId.getValor().isEmpty()) {
	            JOptionPane.showMessageDialog(this, 
	            	"Selecione um usuário na tabela para atualizar.", 
	            	"Aviso", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        
	        int id = Integer.parseInt(tfId.getValor());
	        String senha = new String(pwSenha.getValor());
	        
	        if (senha.trim().isEmpty()) {	
	            JOptionPane.showMessageDialog(this, 
	            	"Senha não foi alterada.", 
	            	"Aviso", JOptionPane.INFORMATION_MESSAGE);
	            Usuario usuarioBanco = new Usuario();
		        UsuarioDAO busca = new UsuarioDAO();
		        usuarioBanco = busca.buscar(id);
		        senha = usuarioBanco.getSenha();
	        }
	        
	        String email = tfEmail.getValor().trim();
	        Privilegios privilegios = (Privilegios) cbPrivilegios.getValor();
	           
	        // Validações
	        if (email.isEmpty()) {
	            JOptionPane.showMessageDialog(this, 
	        		"O nome não pode estar vazio.", 
	        		"Erro", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	
	        Usuario usuario = new Usuario();
	        usuario.setId(id);
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
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um usuário na tabela para ativar.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja ativar este usuário?", 
    		"Confirmação de ativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                
            	Usuario usuario = new Usuario();
            	usuario.setId(id);
                
            	UsuarioDAO dao = new UsuarioDAO();
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
	
	private void desativarUsuario() {
        if (tfId.getValor().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um usuário na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja desativar este usuário?", 
    		"Confirmação de desativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getValor());
                
            	Usuario usuario = new Usuario();
            	usuario.setId(id);
                
                UsuarioDAO dao = new UsuarioDAO();
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
    
    private void excluirUsuario() {
        if (tfId.getValor().isEmpty()) {
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
	    		int id = Integer.parseInt(tfId.getValor());
	            String email = tfEmail.getValor().trim();
	            String senha = pwSenha.getValor().trim();
	            Privilegios privilegios = (Privilegios) cbPrivilegios.getValor();
	            
	            Usuario usuario = new Usuario();
	            usuario.setId(id);
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