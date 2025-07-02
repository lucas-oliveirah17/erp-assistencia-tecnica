package view;

import model.Usuario;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class TelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L; // Default serialVersion

    private Usuario usuarioLogado;

    public TelaPrincipal(Usuario usuario) {
        this.usuarioLogado = usuario;

        // -- CONFIGURAÇÕES DA JANELA --
        setTitle("Sistema ERP - Bem-vindo, " + usuario.getEmail());
        this.setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // BARRA MENU SUPERIOR --
        JMenuBar menuBar = new JMenuBar();

        // -- MENU CADASTRO --
        JMenu menuCadastro = new JMenu("Cadastros");
        JMenuItem menuCliente = new JMenuItem("Clientes");
        JMenuItem menuFuncionario = new JMenuItem("Funcionários");
        JMenuItem menuUsuario = new JMenuItem("Usuários");

        menuCliente.addActionListener(e -> new TelaCadastroCliente());
        menuCadastro.add(menuCliente);
        
        menuFuncionario.addActionListener(e -> new TelaCadastroFuncionario());
        menuCadastro.add(menuFuncionario);

        // Só mostra o menu de usuários se o privilégio for ADMINISTRADOR
        if (usuarioLogado.getPrivilegios() == model.enums.Privilegios.ADMINISTRADOR) {
            menuUsuario.addActionListener(e -> new TelaCadastroUsuario());
            menuCadastro.add(menuUsuario);
        }

        menuBar.add(menuCadastro);

        // -- MENU SISTEMA --
        JMenu menuSair = new JMenu("Sistema");
        JMenuItem sair = new JMenuItem("Sair");
        sair.addActionListener((ActionEvent e) -> {
            dispose();
            new TelaLogin();
        });

        menuSair.add(sair);
        menuBar.add(menuSair);

        // -- MONTAGEM FINAL --
        setJMenuBar(menuBar); // Insere o menuBar no Frame
        
        this.pack(); // Ajusta o tamanho da janela ao conteúdo
        this.setLocationRelativeTo(null); // Aparece centralizado na tela
        setVisible(true);
    }
}
