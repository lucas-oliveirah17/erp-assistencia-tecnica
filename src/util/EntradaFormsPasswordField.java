package util;

import javax.swing.JPasswordField;
import java.awt.Component;
import java.util.Arrays;

public class EntradaFormsPasswordField extends EntradaFormsBase {
	private static final long serialVersionUID = 1L;
	
	private JPasswordField passwordField;

    public EntradaFormsPasswordField(String textoLabel, int largura) {
        super(textoLabel);
        passwordField = new JPasswordField(largura);
        inicializar();
    }
    
    public EntradaFormsPasswordField(String textoLabel) {
        super(textoLabel);
        passwordField = new JPasswordField();
        inicializar();
    }
    
    private void inicializar() {
    	passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(passwordField);
    }

    @Override
    public void setValor(Object valor) {
    	passwordField.setText(valor == null ? "" : valor.toString());
    }

    @Override
    public String getValor() {
        char[] password = passwordField.getPassword();
        String passwordString = new String(password);
        Arrays.fill(password, ' '); // Limpa o conteúdo sensível da memória
        return passwordString;
    }

    public JPasswordField getPasswordField () {
        return passwordField;
    }
}
