package view;

import model.Cliente;
import model.Usuario;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import view.base.TelaGerenciamentoAbstrata;
import view.components.FormInputV;
import view.components.GerenciamentoButton;
import view.components.GerenciamentoForm;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.Dimension;

import java.util.List;

public class TelaGerenciamentoClientes extends TelaGerenciamentoAbstrata {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    // Tabela
	private String nomeTabela = "Lista de Cliente";
	private String[] colunas = {
    		"ID", "Nome", "CPF/CNPJ", "Tipo", "Telefone",
    		"Email", "Endereco", "Nº", "Complemento",
    		"Bairro", "Cidade", "UF", "CEP"
    };
            
    // Campos do formulário
	private String nomeFormulario1   = "Dados do Cliente";
	
	private FormInputV tfId          = new FormInputV("ID:", new JTextField());
    private FormInputV tfNome        = new FormInputV("Nome:", new JTextField());
    private FormInputV tfCpfCnpj     = new FormInputV("CPF/CNPJ:", new JTextField());
    private FormInputV cbTipo		 = new FormInputV("Tipo de Cliente:", new JComboBox<>(TipoCliente.values()));
    private FormInputV tfTelefone    = new FormInputV("Telefone:", new JTextField());
    private FormInputV tfEmail       = new FormInputV("Email:", new JTextField());
    
    
    private String nomeFormulario2 	 = "Endereço Residencial";
    
    private FormInputV tfEndereco    = new FormInputV("Endereço:", new JTextField());
    private FormInputV tfNumero      = new FormInputV("Número:", new JTextField());
    private FormInputV tfComplemento = new FormInputV("Complemento:", new JTextField());
    private FormInputV tfBairro      = new FormInputV("Bairro:", new JTextField());
    private FormInputV tfCidade      = new FormInputV("Cidade:", new JTextField());
    private FormInputV cbUf   		 = new FormInputV("UF:", new JComboBox<>(Uf.values()));
    private FormInputV tfCep         = new FormInputV("CEP:", new JTextField());
        
    public TelaGerenciamentoClientes(Usuario usuarioInstancia) {
    	super(usuarioInstancia);
    	
        criarPainelTabela(nomeTabela, colunas);
        
        formularioEsquerdo = new GerenciamentoForm(
        		nomeFormulario1, tfNome, tfCpfCnpj, cbTipo, tfTelefone, tfEmail
        );
        
        formularioDireito = new GerenciamentoForm(
        		nomeFormulario2, tfEndereco, tfNumero, tfComplemento,
        		tfBairro, tfCidade, cbUf, tfCep
		);
        
        criarPainelFormulario();
     
                  
        // --- Painel dos Botões ---
        painelBotoes = new GerenciamentoButton(
    		usuarioLogado,
    		new Dimension(90, 30),
    	    e -> new TelaCadastroCliente(this),
    	    e -> atualizarCliente(),
    	    e -> limparFormulario(),
    	    e -> ativarCliente(),
    	    e -> desativarCliente(),
    	    e -> excluirCliente()	
        );
        
        finalizarTela();
    }
              
    protected void carregarDadosTabela() {
        tabela.limparTabela();
        
        ClienteDAO dao = new ClienteDAO();
        
        List<Cliente> clientes = ckbMostrarInativos.isSelected()
            ? dao.listarTodos()
            : dao.listarApenasAtivos();

        for (Cliente c : clientes) {
            tabela.adicionarLinha(new Object[]{
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
        
        // Centralizar as colunas ID, Número e UF
        tabela.centralizarColuna(0, 7, 11);   
    }
    
    protected void preencherFormulario(int linha) {
        try {
	    	// Pega os dados da linha selecionada
	        int id = (int) tabela.getValorEm(linha, 0);
	        String nome = (String) tabela.getValorEm(linha, 1);
	        String cpfCnpj = (String) tabela.getValorEm(linha, 2);
	        TipoCliente tipo = TipoCliente.fromString(tabela.getValorEm(linha, 3).toString());
	        String telefone = (String) tabela.getValorEm(linha, 4);
	        String email = (String) tabela.getValorEm(linha, 5);
	        String endereco = (String) tabela.getValorEm(linha, 6);
	        String numero = (String) tabela.getValorEm(linha, 7);
	        String complemento = (String) tabela.getValorEm(linha, 8);
	        String bairro = (String) tabela.getValorEm(linha, 9);
	        String cidade = (String) tabela.getValorEm(linha, 10);
	        Uf uf = Uf.fromString(tabela.getValorEm(linha, 11).toString());
	        String cep = (String) tabela.getValorEm(linha, 12);
	        
	        // Preenche os campos do formulário
	        tfId.setText(String.valueOf(id));
	        tfNome.setText(nome);
	        tfCpfCnpj.setText(cpfCnpj);
	        cbTipo.setSelectedItem(tipo);
	        tfTelefone.setText(telefone);
	        tfEmail.setText(email);
	        tfEndereco.setText(endereco);
	        tfNumero.setText(numero);
	        tfComplemento.setText(complemento);
	        tfBairro.setText(bairro);
	        tfCidade.setText(cidade);
	        tfCep.setText(cep);
	        cbUf.setSelectedItem(uf);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
        		"Erro ao preencher formulário: " + ex.getMessage());
        }
    }
    
    protected void limparFormulario() {
    	tfId.setText("");
    	tfNome.setText("");
    	tfCpfCnpj.setText("");
    	cbTipo.setSelectedItem(null);
    	tfTelefone.setText("");
    	tfEmail.setText("");
    	tfEndereco.setText("");
    	tfNumero.setText("");
    	tfComplemento.setText("");
    	tfBairro.setText("");
    	tfCidade.setText("");
    	cbUf.setSelectedItem(null);
    	tfCep.setText("");
    	
    	tabela.clearSelection();
    }
    
    private void atualizarCliente() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
            		"Selecione um cliente na tabela para atualizar.", 
            		"Aviso", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, 
            	"Erro inesperado: " + e.getMessage(), 
        		"Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ativarCliente() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para ativar.", 
        		"Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
        	int id = Integer.parseInt(tfId.getText());
            
            ClienteDAO dao = new ClienteDAO();
            Cliente cliente = dao.buscar(id);
            
            if(cliente == null) {
            	JOptionPane.showMessageDialog(this, 
            		"Cliente não encontrado.", 
            		"Aviso", JOptionPane.WARNING_MESSAGE);
                return;     	
            }
            
            if(cliente.isAtivo() == true) {
            	JOptionPane.showMessageDialog(this, 
            		"Cliente já está ativo.", 
            		"Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            	
            }
            
            int confirmacao = JOptionPane.showConfirmDialog(this, 
        		"Tem certeza que deseja ativar este cliente?", 
        		"Confirmação de ativação", 
        		JOptionPane.YES_NO_OPTION);
            
            if (confirmacao == JOptionPane.YES_OPTION) {
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
    
    private void desativarCliente() {
        if (tfId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
        		"Selecione um cliente na tabela para desativar.", 
        		"Aviso", 
        		JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, 
    		"Tem certeza que deseja desativar este cliente?", 
    		"Confirmação de desativação", 
    		JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
            	int id = Integer.parseInt(tfId.getText());
                
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
        if (tfId.getText().isEmpty()) {
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