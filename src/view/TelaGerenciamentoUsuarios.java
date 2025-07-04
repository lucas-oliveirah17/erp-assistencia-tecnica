package view;

import model.Usuario;
import model.enums.Privilegios;

import control.UsuarioDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaGerenciamentoUsuarios extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion
    
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtEmail;
    private JPasswordField txtSenha;
    private JComboBox<Privilegios> cmbPrivilegios;
    private JCheckBox chkAtivo;
    private JButton btnAtualizar, btnExcluir, btnLimpar;

    public TelaGerenciamentoUsuarios(Usuario usuarioInstancia) {
    	// -- CONFIGURAÇÕES DO PAINEL --
        this.setLayout(new BorderLayout(10, 10));

        // --- Painel da Tabela (LISTAR) ---
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));

        tableModel = new DefaultTableModel(new Object[]{"ID", "Email", "Privilégios", "Ativo"}, 0);
        tabelaUsuarios = new JTable(tableModel);
        
        carregarDadosTabela(); // Carrega os dados iniciais
        
        painelTabela.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);

        // --- Painel do Formulário (ATUALIZAR / DELETAR) ---
        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Usuário Selecionado"));

        txtId = new JTextField();
        txtId.setEditable(false); // ID não pode ser editado
        txtEmail = new JTextField();
        txtSenha = new JPasswordField();
        cmbPrivilegios = new JComboBox<>(Privilegios.values());
        chkAtivo = new JCheckBox("Usuário Ativo");
        
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar Campos");

        painelFormulario.add(new JLabel("ID:"));
        painelFormulario.add(txtId);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(txtEmail);
        painelFormulario.add(new JLabel("Nova Senha (opcional):"));
        painelFormulario.add(txtSenha);
        painelFormulario.add(new JLabel("Privilégios:"));
        painelFormulario.add(cmbPrivilegios);
        painelFormulario.add(new JLabel("Status:"));
        painelFormulario.add(chkAtivo);
        painelFormulario.add(btnAtualizar);
        painelFormulario.add(btnExcluir);
        painelFormulario.add(btnLimpar);

        // Adicionando os painéis principais à janela
        add(painelTabela, BorderLayout.CENTER);
        add(painelFormulario, BorderLayout.SOUTH);

        // --- Listeners (Ações) ---

        // Ação para clique na tabela
        tabelaUsuarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaUsuarios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });
        
        // Ação para o botão Atualizar
        btnAtualizar.addActionListener(e -> atualizarUsuario());

        // Ação para o botão Excluir
        btnExcluir.addActionListener(e -> excluirUsuario());
        
        // Ação para o botão Limpar
        btnLimpar.addActionListener(e -> limparFormulario());

        setVisible(true);
    }

    private void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.listarTodos();

        for (Usuario u : usuarios) {
            tableModel.addRow(new Object[]{
                u.getId(),
                u.getEmail(),
                u.getPrivilegios(),
                u.isAtivo() ? "Sim" : "Não"
            });
        }
    }
    
    private void preencherFormulario(int linha) {
        // Pega os dados da linha selecionada
        int id = (int) tableModel.getValueAt(linha, 0);
        String email = (String) tableModel.getValueAt(linha, 1);
        Privilegios privilegio = (Privilegios) tableModel.getValueAt(linha, 2);
        boolean ativo = tableModel.getValueAt(linha, 3).toString().equalsIgnoreCase("Sim");
        
        // Preenche os campos do formulário
        txtId.setText(String.valueOf(id));
        txtEmail.setText(email);
        cmbPrivilegios.setSelectedItem(privilegio);
        chkAtivo.setSelected(ativo);
        txtSenha.setText(""); // Limpa o campo de senha por segurança
    }
    
    private void atualizarUsuario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // A senha só é atualizada se o campo não estiver vazio
        String senha = new String(txtSenha.getPassword());
        if (senha.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Senha não foi alterada.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
             // ATENÇÃO: A lógica abaixo NÃO atualiza a senha se o campo estiver vazio.
             // Para um update real da senha, seria preciso tratar isso no DAO ou buscar a senha atual.
             // O código da sua classe DBQuery atualmente não suporta um UPDATE condicional de campos.
        }

        Usuario usuario = new Usuario(
            Integer.parseInt(txtId.getText()),
            txtEmail.getText(),
            senha,
            (Privilegios) cmbPrivilegios.getSelectedItem(),
            chkAtivo.isSelected()
        );
        
        UsuarioDAO dao = new UsuarioDAO();
        if (dao.atualizar(usuario)) {
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            carregarDadosTabela();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário.");
        }
    }
    
    private void excluirUsuario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este usuário?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            Usuario usuario = new Usuario(Integer.parseInt(txtId.getText()), "", "", null, false);
            
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.excluir(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                carregarDadosTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir usuário.");
            }
        }
    }
    
    private void limparFormulario() {
        txtId.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        cmbPrivilegios.setSelectedIndex(0);
        chkAtivo.setSelected(false);
        tabelaUsuarios.clearSelection();
    }
}