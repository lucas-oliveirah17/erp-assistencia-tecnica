package view;

import javax.swing.*;
import java.awt.*;

public class ExemploTabbedPane extends JFrame {
	private static final long serialVersionUID = 1L;

	public ExemploTabbedPane() {
        setTitle("Exemplo com JTabbedPane");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centraliza na tela

        // Criando o JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Abas
        tabbedPane.addTab("Cliente", criarPainelCliente());
        tabbedPane.addTab("Funcionário", criarPainelFuncionario());
        tabbedPane.addTab("Sobre", criarPainelSobre());

        // Adiciona o tabbedPane ao JFrame
        add(tabbedPane);

        setVisible(true);
    }

    private JPanel criarPainelCliente() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Nome:"));
        panel.add(new JTextField());
        panel.add(new JLabel("CPF:"));
        panel.add(new JTextField());
        panel.add(new JLabel("Telefone:"));
        panel.add(new JTextField());
        return panel;
    }

    private JPanel criarPainelFuncionario() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea areaTexto = new JTextArea("Informações do funcionário...");
        areaTexto.setLineWrap(true);
        panel.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarPainelSobre() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Sistema ERP - Assistência Técnica © 2025"));
        return panel;
    }

    public static void main(String[] args) {
        // Usa o look and feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        new ExemploTabbedPane();
    }
}
