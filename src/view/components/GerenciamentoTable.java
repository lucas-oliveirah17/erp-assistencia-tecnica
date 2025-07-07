package view.components;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class GerenciamentoTable extends JTable {
	private static final long serialVersionUID = 1L; // Default serialVersion

	// Componentes da tabela
	private DefaultTableModel tableModel;
	private String[] colunas;
    
	// Personalização
	private Font headerFont = new Font("Arial", Font.BOLD, 12);
	private Font cellsFont = new Font("Arial", Font.PLAIN, 12);
	private int alturaTabelaScroll = 180;
	
	public GerenciamentoTable (String[] colunasTabela){
		this.colunas = colunasTabela;
		
		this.tableModel = new DefaultTableModel(this.colunas, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        super.setModel(tableModel);
        
        configurarTabela();
	}
	
	private void configurarTabela() {
		// Personalização
        this.getTableHeader().setFont(this.headerFont);
        this.setFont(this.cellsFont);
        
        // Redimensionamento automático das colunas
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Alinhamento padrão: esquerda
        alinharTodasColunas(SwingConstants.LEFT);
	}
	
	// Alinhamento das células
    public void alinharTodasColunas(int alinhamento) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(alinhamento);

        TableColumnModel colModel = this.getColumnModel();
        for (int i = 0; i < colModel.getColumnCount(); i++) {
            colModel.getColumn(i).setCellRenderer(renderer);
        }
    }
    
    public void centralizarColuna(int... indices) {
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i : indices) {
            this.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    // Facilita o uso com JScrollPane
    public JScrollPane comScrollPanel() {
        JScrollPane scroll = new JScrollPane(this);
        scroll.setPreferredSize(new Dimension(0, this.alturaTabelaScroll)); // Altura da tabela
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.alturaTabelaScroll));
                
        return scroll;
    }
    
    public void adicionarLinha(Object... dados) {
        this.tableModel.addRow(dados);
    }
    
    public void limparTabela() {
    	this.tableModel.setRowCount(0);
    }
    
    public Object getValorEm(int linha, int coluna) {
    	if (linha < 0 || linha >= getRowCount()) {
            throw new IndexOutOfBoundsException("Linha inválida: " + linha);
        }
        if (coluna < 0 || coluna >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Coluna inválida: " + coluna);
        }
        return this.getValueAt(linha, coluna);
    }
}
