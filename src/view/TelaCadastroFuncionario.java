package view;


import model.Funcionario;
import model.Usuario;
import model.enums.FuncaoFuncionario;
import model.enums.Privilegios;
import control.FuncionarioDAO;
import control.UsuarioDAO;

import view.base.TelaCadastroAbstrata;
import view.components.FormInputH;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaCadastroFuncionario extends TelaCadastroAbstrata {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private TelaGerenciamentoFuncionarios painelGerenciamento;
    
    private static final String TITULO_JANELA = "Cadastro de Funcionário";
        
    // -- COMPONENTES DE ENTRADA --
    private FormInputH tfNome;
    private FormInputH tfCpf;
    private FormInputH cbFuncao;
    private FormInputH tfTelefone;
    private FormInputH tfEmail;
    
    private FormInputH tfUsuario;
    private FormInputH pfSenha;
    private FormInputH cbPrivilegios;
    
    public TelaCadastroFuncionario() {
    	this((TelaGerenciamentoFuncionarios) null);
    }
    
    public TelaCadastroFuncionario(TelaGerenciamentoFuncionarios painelGerenciamento) {	
    	super(TITULO_JANELA);
    	
        this.painelGerenciamento = painelGerenciamento;
        inicializarCampos();
        construirFormulario();
        finalizarTela();
    }
    
    private void inicializarCampos() {
        this.tfNome = new FormInputH("Nome:", new JTextField());
        this.tfCpf = new FormInputH("CPF:", new JTextField());
        this.cbFuncao = new FormInputH("Função:", new JComboBox<>(FuncaoFuncionario.values()));
        this.tfTelefone = new FormInputH("Telefone:", new JTextField());
        this.tfEmail = new FormInputH("Email:", new JTextField());
        
        this.tfUsuario = new FormInputH("Nome de Usuário:", new JTextField());
        this.pfSenha = new FormInputH("Senha:", new JPasswordField());
        this.cbPrivilegios = new FormInputH("Privilégio:", new JComboBox<>(Privilegios.values()));
    }

    @Override
    protected void construirFormulario() {
    	JPanel painelDados = criarPainelFormulario("Dados do Funcionário");
    	adicionarEntrada(painelDados, tfNome);
        adicionarEntrada(painelDados, tfCpf);
        adicionarEntrada(painelDados, cbFuncao);
        adicionarEntrada(painelDados, tfTelefone);
        adicionarEntrada(painelDados, tfEmail);
        
        adicionarPainelFormulario(painelDados);
        
        JPanel painelConta = criarPainelFormulario("Dados do Funcionário");
        adicionarEntrada(painelConta, tfUsuario);
        adicionarEntrada(painelConta, pfSenha);
        adicionarEntrada(painelConta, cbPrivilegios);
        
        adicionarPainelFormulario(painelConta);
    	
    	btnSalvar.addActionListener(e -> aoSalvar());
    }
   
    @Override
    protected void aoSalvar() {
        Funcionario funcionario = new Funcionario();
        Usuario usuario = new Usuario();
        try {
            funcionario.setNome(tfNome.getText());
            funcionario.setCpf(tfCpf.getText());
            funcionario.setFuncao((FuncaoFuncionario) cbFuncao.getSelectedItem());
            funcionario.setTelefone(tfTelefone.getText());
            funcionario.setEmail(tfEmail.getText());
            funcionario.setAtivo(true);            

            FuncionarioDAO daoFuncionario = new FuncionarioDAO();
            int idGerado = daoFuncionario.salvar(funcionario);
            
            if(idGerado == 0) {
            	JOptionPane.showMessageDialog(this, 
            	"Erro ao cadastrar funcionário.");
            }
            
            usuario.setIdFuncionario(idGerado);
            usuario.setUsuario(tfUsuario.getText());
            usuario.setSenha(pfSenha.getText());
            usuario.setEmail(tfEmail.getText());
            usuario.setPrivilegios((Privilegios) cbPrivilegios.getSelectedItem());
            usuario.setAtivo(true);
            
            UsuarioDAO daoUsuario = new UsuarioDAO();
            idGerado = daoUsuario.salvar(usuario);

            if (idGerado > 0) {
                JOptionPane.showMessageDialog(this, "Funcionário cadastrado com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                "Funcionário cadastro, mas erro ao cadastrar usuário.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}