package util;

import javax.swing.JComboBox;
import java.awt.Component;

public class EntradaFormsComboBox<T> extends EntradaFormsBase {
	private static final long serialVersionUID = 1L;
	
	private JComboBox<T> comboBox;

    public EntradaFormsComboBox(String textoLabel, T[] itens) {
        super(textoLabel);
        comboBox = new JComboBox<>(itens);
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(comboBox);
    }

    @Override
    public void setValor(Object valor) {
        comboBox.setSelectedItem(valor);
    }

    @Override
    public Object getValor() {
        return comboBox.getSelectedItem();
    }

    public JComboBox<T> getComboBox() {
        return comboBox;
    }
}
