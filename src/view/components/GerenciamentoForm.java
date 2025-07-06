package view.components;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class GerenciamentoForm extends JPanel{
	private static final long serialVersionUID = 1L; // Default serialVersion

	private String titulo;
	
	public GerenciamentoForm(String tituloForm, FormInputV... entradas) {
		this.titulo = tituloForm;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setOpaque(false); // Deixa o fundo transparente (coerente com construtor vazio)
        
		this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(titulo),
                BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding
        ));
		this.setAlignmentY(FormInputV.TOP_ALIGNMENT);
		
		// Adiciona os componentes ao painel com espaçamento
        for (int i = 0; i < entradas.length; i++) {
        	this.add(entradas[i]);
        	if (i < entradas.length - 1) { // Espaço entre os componentes (exceto o último)
                this.add(Box.createVerticalStrut(10));
            }
        }
	}
	
	public GerenciamentoForm() { // Cria um painel de preenchimento
		this.setOpaque(false);
	}
	
	public String getTitulo() {
        return this.titulo;
    }
}
