package view;

import control.FuncionarioDAO;
import model.Funcionario;
import model.enums.FuncaoFuncionario;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaGerenciamentoFuncionarios1 extends JPanel {
    private static final long serialVersionUID = 1L; // Default serialVersion

    // Componentes da tabela de funcionários
    private JTable tabelaFuncionarios;
    private DefaultTableModel tableModel;

    // Campos do formulário
    private JTextField tfId = new JTextField();
    private JTextField tfNome = new JTextField();
    private JTextField tfCpf = new JTextField();
    private JComboBox<FuncaoFuncionario> cbFuncao = new JComboBox<>(FuncaoFuncionario.values());
    private JTextField tfTelefone = new JTextField();
    private JTextField tfEmail = new JTextField();
    private JCheckBox cbAtivo = new JCheckBox("Ativo", true);

    // Botões de ação
    private JButton btnNovo = new JButton("Novo"); 
    private JButton btnAtualizar = new JButton("Atualizar"); 
    private JButton btnLimpar = new JButton("Limpar"); 

    // Checkbox de filtro de exibição
    private JCheckBox cbMostrarInativos = new JCheckBox("Mostrar Inativos");

    public TelaGerenciamentoFuncionarios1() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // --- Painel da Tabela (Lista de Funcionários) ---
        JPanel painelTabela = new JPanel();
        painelTabela.setLayout(new BoxLayout(painelTabela, BoxLayout.Y_AXIS));
        painelTabela.setBorder(BorderFactory.createTitledBorder("Lista de Funcionários"));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        String[] colunas = {"ID", "Nome", "CPF", "Função", "Telefone", "Email"};

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

        // --- Painel do Filtro ---
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbMostrarInativos.addActionListener(e -> carregarDadosTabela());
        painelFiltro.add(cbMostrarInativos);
        
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
        painelFormulario.add(criarPainelDadosGerais(), gbc);

        // Painel de Endereço (coluna direita)
        JPanel painelVazio = new JPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        painelFormulario.add(painelVazio, gbc);
        

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

        // Evento para preencher o formulário ao clicar na tabela
        tabelaFuncionarios.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaFuncionarios.getSelectedRow();
                if (linhaSelecionada != -1) {
                    preencherFormulario(linhaSelecionada);
                }
            }
        });

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
        tableModel.setRowCount(0);
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

        centralizarTextoColunas(tabelaFuncionarios, 0);
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
    
    private JPanel criarPainelDadosGerais() {
    	// O GridBagLayout permite posicionar componentes em uma grade flexível,
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados"));
        painelFormulario.setAlignmentY(Component.TOP_ALIGNMENT); // Alinha verticalmente no topo

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Espaçamento interno
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START; // Alinhamento no canto esquerdo
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

        return painelFormulario;
    }
}
