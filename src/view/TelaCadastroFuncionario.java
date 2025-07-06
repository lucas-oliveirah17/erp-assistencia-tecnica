package view;


import model.Funcionario;
import model.enums.FuncaoFuncionario;

import control.FuncionarioDAO;

import view.base.TelaCadastroAbstrata;
import view.components.FormInputH;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    	
    	btnSalvar.addActionListener(e -> aoSalvar());
    }
   
    @Override
    protected void aoSalvar() {
        Funcionario funcionario = new Funcionario();
        try {
            funcionario.setNome(tfNome.getText());
            funcionario.setCpf(tfCpf.getText());
            funcionario.setFuncao((FuncaoFuncionario) cbFuncao.getSelectedItem());
            funcionario.setTelefone(tfTelefone.getText());
            funcionario.setEmail(tfEmail.getText());
            funcionario.setAtivo(true);            

            FuncionarioDAO dao = new FuncionarioDAO();
            boolean sucesso = dao.salvar(funcionario);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao salvar funcionário.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}