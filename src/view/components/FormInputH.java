package view.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormInputH extends JPanel {
	private static final long serialVersionUID = 1L; // Default serialVersion
	
    private JLabel label;
    private JComponent campo;
    
    private Font labelFont = new Font("Arial", Font.BOLD, 12);
    private Font inputFont = new Font("Arial", Font.PLAIN, 12);
    
    private Dimension inputSize = new Dimension(200, 25);

    public FormInputH(String textoLabel, JComponent c) {
        this.label = new JLabel(textoLabel);
        if (c == null) {
            throw new IllegalArgumentException("Campo não pode ser nulo em FormInputH.");
        }
        this.campo = c;

        setLayout(new GridBagLayout());
        setOpaque(false); // permite que o painel fique transparente se o pai quiser

        // Estilização
        this.label.setFont(labelFont);
        this.campo.setFont(inputFont);
        this.campo.setPreferredSize(inputSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5); // margem

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0; gbc.gridy = 0;
        add(this.label, gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 0;
        add(this.campo, gbc);
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
        return null;
    }

    public void setText(String texto) {
        if (campo instanceof JTextField textField) {
            textField.setText(texto);
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
}
