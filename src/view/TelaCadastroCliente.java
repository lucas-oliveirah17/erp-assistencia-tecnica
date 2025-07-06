package view;

import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import view.base.TelaCadastroAbstrata;
import view.components.FormInputH;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class TelaCadastroCliente extends TelaCadastroAbstrata {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private TelaGerenciamentoClientes painelGerenciamento;
    
    private static final String TITULO_JANELA = "Cadastro de Cliente";
    
    // -- COMPONENTES DE ENTRADA --
    private FormInputH tfNome;
    private FormInputH tfCpfCnpj;
    private FormInputH cbTipo;
    private FormInputH tfTelefone;
    private FormInputH tfEmail;
    private FormInputH tfEndereco;
    private FormInputH tfNumero;
    private FormInputH tfComplemento;
    private FormInputH tfBairro;
    private FormInputH tfCidade;
    private FormInputH cbUf;
    private FormInputH tfCep;
        
    public TelaCadastroCliente() {
    	this((TelaGerenciamentoClientes) null);
    }
    
    public TelaCadastroCliente(TelaGerenciamentoClientes painelGerenciamento) {
    	super(TITULO_JANELA);
    	
        this.painelGerenciamento = painelGerenciamento;
        inicializarCampos();
        construirFormulario();
        finalizarTela();
    }
    
    private void inicializarCampos() {
        this.tfNome = new FormInputH("Nome:", new JTextField());
        this.tfCpfCnpj = new FormInputH("CPF/CNPJ:", new JTextField());
        this.cbTipo = new FormInputH("Tipo de Cliente:", new JComboBox<>(TipoCliente.values()));
        this.tfTelefone = new FormInputH("Telefone:", new JTextField());
        this.tfEmail = new FormInputH("Email:", new JTextField());
        
        this.tfEndereco = new FormInputH("Endereço:", new JTextField());
        this.tfNumero = new FormInputH("Número:", new JTextField());
        this.tfComplemento = new FormInputH("Complemento:", new JTextField());
        this.tfBairro = new FormInputH("Bairro:", new JTextField());
        this.tfCidade = new FormInputH("Cidade:", new JTextField());
        this.cbUf = new FormInputH("UF:", new JComboBox<>(Uf.values()));
        this.tfCep = new FormInputH("CEP:", new JTextField());
    }
    
    @Override
    protected void construirFormulario() {
    	this.painelFormulario1 = criarPainelFormulario("Dados do Cliente");
    	adicionarEntrada(this.painelFormulario1, tfNome);
        adicionarEntrada(this.painelFormulario1, tfCpfCnpj);
        adicionarEntrada(this.painelFormulario1, cbTipo);
        adicionarEntrada(this.painelFormulario1, tfTelefone);
        adicionarEntrada(this.painelFormulario1, tfEmail);
        
        adicionarPainelFormulario(this.painelFormulario1);
        
        this.painelFormulario2 = criarPainelFormulario("Endereço Residencial");
        adicionarEntrada(this.painelFormulario2, tfEndereco);
        adicionarEntrada(this.painelFormulario2, tfNumero);
        adicionarEntrada(this.painelFormulario2, tfComplemento);
        adicionarEntrada(this.painelFormulario2, tfBairro);
        adicionarEntrada(this.painelFormulario2, tfCidade);
        adicionarEntrada(this.painelFormulario2, cbUf);
        adicionarEntrada(this.painelFormulario2, tfCep);
        
        adicionarPainelFormulario(this.painelFormulario2);
    	
    	btnSalvar.addActionListener(e -> aoSalvar());
    }

    @Override
    protected void aoSalvar() {
        Cliente cliente = new Cliente();
        try {
            cliente.setNome(tfNome.getText());
            cliente.setCpfCnpj(tfCpfCnpj.getText());
            cliente.setTipo((TipoCliente) cbTipo.getSelectedItem());
            cliente.setTelefone(tfTelefone.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setEndereco(tfEndereco.getText());
            cliente.setNumero(tfNumero.getText());
            cliente.setComplemento(tfComplemento.getText());
            cliente.setBairro(tfBairro.getText());
            cliente.setCidade(tfCidade.getText());
            cliente.setUf((Uf) cbUf.getSelectedItem());
            cliente.setCep(tfCep.getText());
            cliente.setAtivo(true);

            ClienteDAO dao = new ClienteDAO();
            int idGerado = dao.salvar(cliente);

            if (idGerado > 0) {
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                if (painelGerenciamento != null) {
                    painelGerenciamento.carregarDadosTabela();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }
}