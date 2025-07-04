package view;

import javax.swing.*;
import java.awt.*;

public class TelaSobre extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private Font boldFont = new Font("Arial", Font.BOLD, 12);
	private Font normalFont = new Font("Arial", Font.PLAIN, 12);

	public TelaSobre() {
		// Configurações básicas da janela
        setTitle("Sobre");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true); // Bloqueia outras janelas enquanto aberta

        // Painel principal com layout vertical
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem interna

        // Labels com alinhamento à esquerda
        JLabel header = new JLabel("Desenvolvido por:");
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel colaborador1 = new JLabel("Guilherme Dionizo Ludgero - GU3054918");
        colaborador1.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel colaborador2 = new JLabel("Lucas Silva de Oliveira - GU3054314");
        colaborador2.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel empresa = new JLabel("Sistema ERP - Assistência Técnica © 2025");
        empresa.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        header.setFont(boldFont);
        colaborador1.setFont(normalFont);
        colaborador2.setFont(normalFont);
        empresa.setFont(boldFont);

        // Adiciona os labels no painel
        painel.add(header);
        painel.add(Box.createVerticalStrut(30)); // espaço de 8 pixels
        painel.add(colaborador1);
        painel.add(Box.createVerticalStrut(10)); // espaço de 4 pixels
        painel.add(colaborador2);
        painel.add(Box.createVerticalStrut(30)); // espaço de 8 pixels
        painel.add(empresa);

        // Adiciona painel no dialog
        add(painel);
        pack();
        setLocationRelativeTo(null); // Centralizar na tela
        
        setVisible(true);
	}
}
