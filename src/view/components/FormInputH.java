package view.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class FormInputH extends JPanel {
	private static final long serialVersionUID = 1L; // Default serialVersion
	
    private JLabel label;
    private JComponent campo;
    
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    
    private Dimension inputSize = new Dimension(200, 25);

    public FormInputH(String textoLabel, JComponent c) {
        if (c == null) {
            throw new IllegalArgumentException("Campo não pode ser nulo em FormInputH.");
        }
        this.label = new JLabel(textoLabel);
        this.campo = c;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false); // permite que o painel fique transparente se o pai quiser

        // Alinhamento vertical centralizado
        setAlignmentY(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        campo.setAlignmentY(Component.CENTER_ALIGNMENT);

        // Estilização
        label.setFont(labelFont);
        campo.setFont(inputFont);
        campo.setPreferredSize(inputSize);
        campo.setMaximumSize(inputSize);
        //campo.setMaximumSize(inputSize); // para evitar crescer horizontalmente

        // Montagem dos componentes
        add(label);
        add(Box.createHorizontalStrut(5)); // pequeno espaçamento vertical entre label e campo
        add(campo);
    }

    // -- MÉTODOS UTILITÁRIOS --
    public JComponent getCampo() {
        return this.campo;
    }

    public JLabel getLabel() {
        return this.label;
    }
    
    public String getText() {
        if (campo instanceof JTextField textField) {
            return textField.getText();
        }
        
        if (campo instanceof JPasswordField passwordField) {
            return new String(passwordField.getPassword());
        }
        
        return null;
    }

    public void setText(String texto) {
        if (campo instanceof JTextField textField) {
            textField.setText(texto);
        }
        
        if (campo instanceof JPasswordField passwordField) {
            passwordField.setText(texto);
        }
    }
    
    public Object getSelectedItem() {
        if (campo instanceof JComboBox<?> comboBox) {
            return comboBox.getSelectedItem();
        }
        return null;
    }

    public void setSelectedItem(Object valor) {
        if (campo instanceof JComboBox<?> comboBox) {
            comboBox.setSelectedItem(valor);
        }
    }
    
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        campo.setEnabled(enabled);
        label.setEnabled(enabled);
    }
    
    public String getPassword() {        
        if (campo instanceof JPasswordField passwordField) {
            return new String(passwordField.getPassword());
        }
        
        return null;
    }
    
    public static void toggleMostrarSenha(JPasswordField pfSenha, JCheckBox ckbMostrarSenha) {
        if (pfSenha == null || ckbMostrarSenha == null) return;
        pfSenha.setEchoChar(ckbMostrarSenha.isSelected() ? (char) 0 : '●');
    }
}
