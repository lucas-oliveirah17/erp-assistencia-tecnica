package view;

import control.FuncionarioDAO;
import model.Funcionario;
import model.enums.FuncaoFuncionario;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciamentoFuncionarios2 extends JPanel {
    private static final long serialVersionUID = 1L;

    // Componentes da tabela
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;

    // Campos do formulário
    private JTextField tfId = new JTextField();
    private JTextField tfNome = new JTextField();
    private JTextField tfCpf = new JTextField();
    private JComboBox<FuncaoFuncionario> cbFuncao = new JComboBox<>(FuncaoFuncionario.values());
    private JTextField tfTelefone = new JTextField();
    private JTextField tfEmail = new JTextField();
    private JCheckBox cbAtivo = new JCheckBox("Ativo", true); // Mostrar se está ativo

    // Botões de ação
    private JButton btnNovo = new JButton("Novo");
    private JButton btnAtualizar = new JButton("Atualizar");
    private JButton btnLimpar = new JButton("Limpar");

    // Checkbox de filtro
    private JCheckBox cbMostrarInativos = new JCheckBox("Mostrar Inativos");

    public TelaGerenciamentoFuncionarios2() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // --- Painel da Tabela ---
        JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Funcionários"));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Cabeçalhos da tabela
        String[] colunas = {"ID", "Nome", "CPF", "Função", "Telefone", "Email"};

        // Modelo da tabela
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFuncionarios = new JTable(tableModel);
        carregarDadosTabela();

        painelTabela.add(new JScrollPane(tabelaFuncionarios));

        // --- Painel do Formulário ---
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.weightx = 1.0;

        int linha = 0;

        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(tfNome, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(tfCpf, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Função:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(cbFuncao, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(tfTelefone, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(tfEmail, gbc);

        linha++;
        gbc.gridx = 0; gbc.gridy = linha;
        painelFormulario.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(cbAtivo, gbc);

        // Evento para preencher formulário ao clicar na tabela
        tabelaFuncionarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        btnNovo.setPreferredSize(new Dimension(90, 30));
        btnAtualizar.setPreferredSize(new Dimension(90, 30));
        btnLimpar.setPreferredSize(new Dimension(90, 30));

        btnNovo.addActionListener(e -> new TelaCadastroFuncionario(this));
        btnAtualizar.addActionListener(e -> atualizarFuncionario());
        btnLimpar.addActionListener(e -> limparFormulario());

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnLimpar);

        // --- Painel de Filtro ---
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        painelFiltro.add(cbMostrarInativos);

        // --- Montagem Final ---
        add(painelTabela);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(painelFiltro);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(painelFormulario);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(painelBotoes);
    }

    public void carregarDadosTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        FuncionarioDAO dao = new FuncionarioDAO();
        List<Funcionario> funcionarios = cbMostrarInativos.isSelected()
                ? dao.listarTodos()
                : dao.listarApenasAtivos();

        for (Funcionario f : funcionarios) {
            tableModel.addRow(new Object[]{
                f.getId(),
                f.getNome(),
                f.getCpf(),
                f.getFuncao(),
                f.getTelefone(),
                f.getEmail()
            });
        }

        centralizarTextoColunas(tabelaFuncionarios, 0); // coluna ID
        larguraColuna(tabelaFuncionarios, 0, 40);
    }

    private void preencherFormulario(int linha) {
        try {
            int id = (int) tableModel.getValueAt(linha, 0);
            String nome = (String) tableModel.getValueAt(linha, 1);
            String cpf = (String) tableModel.getValueAt(linha, 2);
            FuncaoFuncionario funcao = FuncaoFuncionario.fromString(tableModel.getValueAt(linha, 3).toString());
            String telefone = (String) tableModel.getValueAt(linha, 4);
            String email = (String) tableModel.getValueAt(linha, 5);

            tfId.setText(String.valueOf(id));
            tfNome.setText(nome);
            tfCpf.setText(cpf);
            cbFuncao.setSelectedItem(funcao);
            tfTelefone.setText(telefone);
            tfEmail.setText(email);

            // Checar se funcionário está ativo na DAO
            FuncionarioDAO dao = new FuncionarioDAO();
            for (Funcionario f : dao.listarTodos()) {
                if (f.getId() == id) {
                    cbAtivo.setSelected(f.isAtivo());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao preencher formulário: " + ex.getMessage());
        }
    }

    private void atualizarFuncionario() {
        try {
            if (tfId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um funcionário na tabela para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(tfId.getText());
            String nome = tfNome.getText().trim();
            String cpf = tfCpf.getText().trim();
            FuncaoFuncionario funcao = (FuncaoFuncionario) cbFuncao.getSelectedItem();
            String telefone = tfTelefone.getText().trim();
            String email = tfEmail.getText().trim();
            boolean ativo = cbAtivo.isSelected();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Funcionario f = new Funcionario();
            f.setId(id);
            f.setNome(nome);
            f.setCpf(cpf);
            f.setFuncao(funcao);
            f.setTelefone(telefone);
            f.setEmail(email);
            f.setAtivo(ativo);

            FuncionarioDAO dao = new FuncionarioDAO();
            boolean sucesso = dao.atualizar(f);

            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Funcionário atualizado com sucesso!");
                carregarDadosTabela();
                limparFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar funcionário.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        tfId.setText("");
        tfNome.setText("");
        tfCpf.setText("");
        cbFuncao.setSelectedIndex(0);
        tfTelefone.setText("");
        tfEmail.setText("");
        cbAtivo.setSelected(true);
    }

    private void larguraColuna(JTable tabela, int indiceColuna, int largura) {
        TableColumn coluna = tabela.getColumnModel().getColumn(indiceColuna);
        coluna.setMinWidth(largura);
        coluna.setPreferredWidth(largura);
    }

    private void centralizarTextoColunas(JTable tabela, int... indices) {
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i : indices) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }
    }
}