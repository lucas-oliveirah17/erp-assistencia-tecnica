package util;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PainelFormularioUtil {
	public static JPanel criarPainelFormulario(String titulo, Component... componentes) {
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
        ));
        painelFormulario.setAlignmentY(Component.TOP_ALIGNMENT);

        // Adiciona os componentes ao painel com espaçamento
        for (Component componente : componentes) {
        	painelFormulario.add(componente);
        	painelFormulario.add(Box.createVerticalStrut(10)); // Espaço entre os componentes
        }

        return painelFormulario;
    }

    public static JPanel criarPainelVazio() {
        JPanel painelVazio = new JPanel();
        painelVazio.setOpaque(false); // Deixa o painel invisível (sem cor de fundo)
        painelVazio.setLayout(new BorderLayout()); // Layout neutro, ocupa todo o espaço disponível
        return painelVazio;
    }
}
