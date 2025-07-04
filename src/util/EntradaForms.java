package util;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;

public class EntradaForms extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private JLabel label;
	private JTextField entrada;
	
	// Construtor completo com label e largura
	public EntradaForms(String textoLabel, int larguraCampo) {
        this.label = new JLabel(textoLabel);
        this.entrada = new JTextField(larguraCampo);
        configurarLayout();
    }

    // Construtor polimórfico: apenas label, largura será automática
    public EntradaForms(String textoLabel) {
        this.label = new JLabel(textoLabel);
        this.entrada = new JTextField(); // largura padrão, expansível
        configurarLayout();
    }
	
    // Layout comum aos dois construtores
	private void configurarLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);
		entrada.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(label);
        add(Box.createVerticalStrut(2)); // Espaçamento
        add(entrada);
    }
	
	public JTextField getEntrada() {
        return entrada;
    }

    public void setTextoLabel(String texto) {
        label.setText(texto);
    }

    public String getTextoLabel() {
        return label.getText();
    }

    public void setTextoEntrada(String texto) {
        entrada.setText(texto);
    }

    public String getTextoEntrada() {
        return entrada.getText();
    }
}
