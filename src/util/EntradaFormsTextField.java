package util;

import javax.swing.JTextField;
import java.awt.Component;

public class EntradaFormsTextField extends EntradaFormsBase {
	private static final long serialVersionUID = 1L;
	
	private JTextField textField;

    public EntradaFormsTextField(String textoLabel, int largura) {
        super(textoLabel);
        textField = new JTextField(largura);
        inicializar();
    }
    
    public EntradaFormsTextField(String textoLabel) {
        super(textoLabel);
        textField = new JTextField();
        inicializar();
    }
    
    private void inicializar() {
    	textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(textField);
    }

    @Override
    public void setValor(Object valor) {
        textField.setText(valor == null ? "" : valor.toString());
    }

    @Override
    public String getValor() {
        return textField.getText();
    }

    public JTextField getTextField() {
        return textField;
    }
}
