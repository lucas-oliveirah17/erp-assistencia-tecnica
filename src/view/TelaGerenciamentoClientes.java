package view;

import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

import control.ClienteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaGerenciamentoClientes extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    
    private JTextField tfId, tfNome, tfCpfCnpj, tfTelefone, tfEmail,
    tfEndereco, tfNumero, tfComplemento, tfBairro, tfCidade, tfCep ;
    
    private JComboBox<TipoCliente> cbTipo;
    private JComboBox<Uf> cbUf;

    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar"); 
    private JButton btnExcluir = new JButton("Excluir");

    public TelaGerenciamentoClientes() {
    	// -- CONFIGURAÇÕES DO PAINEL --
        this.setLayout(new BorderLayout(10, 10));
        
        // --- PAINEL DA TABELA (LISTAR) ---
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));

        // Cabeçalho da tabela
        String[] colunas = {
    		"ID", "Nome", "CPF/CNPJ", "Tipo", "Telefone",
    		"Email", "Endereco", "Nº", "Complemento",
    		"Bairro", "Cidade", "UF", "CEP"
        };
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaClientes = new JTable(tableModel);
                
        carregarDadosTabela(); // Carrega a lista de dados
              
        painelTabela.add(new JScrollPane(tabelaClientes), BorderLayout.CENTER);
        
        // -- PAINEL BOTÕES --
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));      
           
        btnNovo.addActionListener(e -> new TelaCadastroCliente(this));
        
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnLimpar);        
        painelBotoes.add(btnExcluir);
       
        // -- MONTAGEM FINAL --
        add(painelTabela, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        ClienteDAO dao = new ClienteDAO();
        List<Cliente> clientes = dao.listarTodos();

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
                c.getCep()
            });
        }
    }
}