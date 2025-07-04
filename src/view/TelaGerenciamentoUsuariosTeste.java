package view;

import model.Usuario;
import model.enums.Privilegios;
import control.UsuarioDAOTeste;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TelaGerenciamentoUsuariosTeste extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable tabela;
    private DefaultTableModel tableModel;

    private JTextField txtId, txtEmail;
    private JPasswordField txtSenha;
    private JComboBox<Privilegios> cmbPrivilegios;
    private JCheckBox chkAtivo;

    private JButton btnAtualizar, btnLimpar;
    private JCheckBox chkMostrarApenasAtivos;

    public TelaGerenciamentoUsuariosTeste() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkMostrarApenasAtivos = new JCheckBox("Mostrar apenas usuários ativos");
        painelFiltro.add(chkMostrarApenasAtivos);

        JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Usuários"));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        String[] colunas = {"ID", "Email", "Privilégios", "Ativo"};
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(tableModel);
        painelTabela.add(new JScrollPane(tabela));

        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Usuário Selecionado"));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtEmail = new JTextField();
        txtSenha = new JPasswordField();
        cmbPrivilegios = new JComboBox<>(Privilegios.values());
        chkAtivo = new JCheckBox("Usuário Ativo");

        btnAtualizar = new JButton("Atualizar");
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
        painelFormulario.add(btnLimpar);

        add(painelFiltro);
        add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(painelFormulario);

        tabela.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabela.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });

        btnAtualizar.addActionListener(e -> atualizarUsuario());
        btnLimpar.addActionListener(e -> limparFormulario());
        chkMostrarApenasAtivos.addActionListener(e -> carregarDadosTabela());

        carregarDadosTabela();
        setVisible(true);
    }

    public void carregarDadosTabela() {
        tableModel.setRowCount(0);
        UsuarioDAOTeste dao = new UsuarioDAOTeste();
        List<Usuario> usuarios = chkMostrarApenasAtivos.isSelected() ? dao.listarApenasAtivos() : dao.listarTodos();

        for (Usuario u : usuarios) {
            tableModel.addRow(new Object[]{
                u.getId(),
                u.getEmail(),
                u.getPrivilegios(),
                u.isAtivo() ? "Sim" : "Não"
            });
        }

        centralizarTextoColunas(tabela, 0, 3);
        larguraColuna(tabela, 0, 40);
        larguraColuna(tabela, 3, 50);
    }

    private void preencherFormulario(int linha) {
        txtId.setText(tableModel.getValueAt(linha, 0).toString());
        txtEmail.setText(tableModel.getValueAt(linha, 1).toString());
        cmbPrivilegios.setSelectedItem(tableModel.getValueAt(linha, 2));
        chkAtivo.setSelected("Sim".equalsIgnoreCase(tableModel.getValueAt(linha, 3).toString()));
        txtSenha.setText("");
    }

    private void atualizarUsuario() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String senha = new String(txtSenha.getPassword());
        if (senha.trim().isEmpty()) {
            UsuarioDAOTeste dao = new UsuarioDAOTeste();
            Usuario existente = dao.listarTodos().stream()
                .filter(u -> u.getId() == Integer.parseInt(txtId.getText()))
                .findFirst()
                .orElse(null);
            if (existente != null) {
                senha = existente.getSenha();
            }
        }

        Usuario usuario = new Usuario(
            Integer.parseInt(txtId.getText()),
            txtEmail.getText(),
            senha,
            (Privilegios) cmbPrivilegios.getSelectedItem(),
            chkAtivo.isSelected()
        );

        UsuarioDAOTeste dao = new UsuarioDAOTeste();
        if (dao.atualizar(usuario)) {
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            carregarDadosTabela();
            limparFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário.");
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
        cmbPrivilegios.setSelectedIndex(0);
        chkAtivo.setSelected(false);
        tabela.clearSelection();
    }

    private void larguraColuna(JTable tabela, int indiceColuna, int largura) {
        TableColumn coluna = tabela.getColumnModel().getColumn(indiceColuna);
        coluna.setMinWidth(largura);
        coluna.setPreferredWidth(largura);
    }

    private void centralizarTextoColunas(JTable tabela, int... indices) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i : indices) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
}