package util;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

public class TabelaUtils {

	public static void larguraColunas(JTable tabela, int largura, int... indiceColuna) {
        for(int indice : indiceColuna) {
        	TableColumn coluna = tabela.getColumnModel().getColumn(indice);
            coluna.setMinWidth(largura);
            coluna.setPreferredWidth(largura);     	
        }
    }
    
	public static void centralizarTextoColunas(JTable table, int... indices) {
        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i : indices) {
            table.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }
    }
}
