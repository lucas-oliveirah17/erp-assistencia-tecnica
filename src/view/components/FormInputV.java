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

public class FormInputV extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel label;
    private JComponent campo;

    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    private int inputHeight = 25;

    public FormInputV(String textoLabel, JComponent c) {
        if (c == null) {
            throw new IllegalArgumentException("Campo não pode ser nulo em FormInputV.");
        }
        this.label = new JLabel(textoLabel);
        this.campo = c;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false); // permite que o painel fique transparente se o pai quiser

        // Alinhamento horizontal à esquerda
        setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Estilização
        label.setFont(labelFont);
        campo.setFont(inputFont);
        campo.setPreferredSize(new Dimension(300, inputHeight));
        //campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.height));
        //campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15));

        // Montagem dos componentes
        add(label);
        add(Box.createVerticalStrut(2)); // pequeno espaçamento vertical entre label e campo
        add(campo);
    }

    // -- MÉTODOS UTILITÁRIOS --
    public JComponent getCampo() {
        return campo;
    }

    public JLabel getLabel() {
        return label;
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

    @Override
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