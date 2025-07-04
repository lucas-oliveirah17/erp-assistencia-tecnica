package util;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import java.awt.Component;

public abstract class EntradaFormsBase extends JPanel{
	private static final long serialVersionUID = 1L;
	
	protected JLabel label;
	
	public EntradaFormsBase(String textoLabel) {
		label = new JLabel(textoLabel);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(2)); // Espaçamento
	}
	
	// Método que obliga as classes filhas a implementar uma 
	// forma de definir o valor que o componente exibe.
	public abstract void setValor(Object valor);
	
	// Método que obriga as classes filhas a implementar uma 
	// forma de obter o valor atual do componente.
    public abstract Object getValor();

    public void setTextoLabel(String texto) {
        label.setText(texto);
    }

    public String getTextoLabel() {
        return label.getText();
    }
}
