package view;

import model.Usuario;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion

    private Usuario usuarioLogado;

    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;

        // -- CONFIGURAÇÕES DA JANELA --
        setTitle("Sistema ERP - Bem-vindo, " + usuario.getUsuario());
        this.setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // BARRA MENU SUPERIOR --
        JMenuBar menuBar = new JMenuBar();

        // -- MENU CADASTRO --
        JMenu menuCadastro = new JMenu("Cadastrar");
        JMenuItem menuCliente = new JMenuItem("Clientes");
        JMenuItem menuFuncionario = new JMenuItem("Funcionários");

        menuCliente.addActionListener(e -> new TelaCadastroCliente());
        menuCadastro.add(menuCliente);
        
        // Itens do Menu exclusivo do privilégio ADMINISTRADOR
        if (usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
        	menuFuncionario.addActionListener(e -> new TelaCadastroFuncionario());
            menuCadastro.add(menuFuncionario);
        }

        menuBar.add(menuCadastro);

        // -- MENU SISTEMA --
        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem sobre = new JMenuItem("Sobre");
        JMenuItem sair = new JMenuItem("Sair");
        
        sobre.addActionListener((ActionEvent e) -> {
            new TelaSobre();
        });
        
        sair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin();
        });

        menuSistema.add(sobre);
        menuSistema.add(sair);
        menuBar.add(menuSistema);
        
        setJMenuBar(menuBar); // Insere o menuBar no Frame
        
        // CONFIGURAÇÃO DAS ABAS
        JTabbedPane telas = new JTabbedPane();
        telas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // ADICIONANDO TELAS EM ABAS
        telas.addTab("Cliente", new JScrollPane(new TelaGerenciamentoClientes(usuarioLogado)));
        
        // Abas exclusiva do privilégio ADMINISTRADOR
        if (usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
        	telas.addTab("Funcionário", new JScrollPane(new TelaGerenciamentoFuncionarios(usuarioLogado)));
        	telas.addTab("Usuário", new JScrollPane(new TelaGerenciamentoUsuarios(usuarioLogado)));
        }
              
        add(telas); // Insere as abas no Frame
        
        // CONFIGURAÇÃO FINAL
        pack();
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
    }
}
