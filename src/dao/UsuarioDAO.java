package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBQuery;
import model.Usuario;
import model.enums.Privilegios;

public class UsuarioDAO {
	
	private DBQuery dbQuery;

	public UsuarioDAO() {

        String tableName = "tb_usuario";
        String fieldNames = "id_usuario, email, senha, privilegios, ativo";
        String fieldKey = "id_usuario";

        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	

	public boolean salvar(Usuario usuario) {

        String[] values = {
	        usuario.getEmail(),
	        usuario.getSenha(),
	        usuario.getPrivilegios().getValorDb(), // Converte o enum para String
	        usuario.isAtivo() ? "1" : "0"    // Converte o boolean para "1" ou "0"
        };
        
        // Chama o método da classe DBQuery para executar o INSERT.
        return dbQuery.insert(values, true) > 0;
    }
	
	/**
	 * Atualiza um usuário existente no banco de dados (Operação UPDATE).
	 * @param usuario O objeto Usuario com os dados atualizados (o ID deve estar presente).
	 * @return true se a atualização for bem-sucedida, false caso contrário.
	 */
	public boolean atualizar(Usuario usuario) {
		// Os valores são montados na ordem exata dos campos definidos em fieldNames.
		String[] values = {
			String.valueOf(usuario.getId()),
			usuario.getEmail(),
	        usuario.getSenha(),
	        usuario.getPrivilegios().name(),
	        usuario.isAtivo() ? "1" : "0"
		};
		
		// Chama o método da classe DBQuery para executar o UPDATE.
		return dbQuery.update(values) > 0;
	}
	
	/**
	 * Exclui um usuário do banco de dados (Operação DELETE).
	 * @param usuario O objeto Usuario a ser excluído (usa o ID para a exclusão).
	 * @return true se a exclusão for bem-sucedida, false caso contrário.
	 */
	public boolean excluir(Usuario usuario) {
        // --- CÓDIGO CORRIGIDO AQUI ---
        // A classe DBQuery.delete() espera um array com o mesmo tamanho do número de campos.
        // Criamos um array com o tamanho correto (5) e colocamos o ID na posição correta (índice 0).
        // Os outros valores podem ser vazios, pois não são usados pela query DELETE.
		String[] values = new String[5];
		values[0] = String.valueOf(usuario.getId()); // ID na posição 0
        values[1] = "";
        values[2] = "";
        values[3] = "";
        values[4] = "";
		
		// Chama o método da classe DBQuery para executar o DELETE.
		return dbQuery.delete(values) > 0;
	}
	
	/**
	 * Lista todos os usuários do banco de dados (Operação READ).
	 * @return Uma lista de objetos Usuario.
	 */
	public List<Usuario> listarTodos() {
        ResultSet rs = dbQuery.select("");
        List<Usuario> usuarios = new ArrayList<>();
        
        try {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    Privilegios.fromDb(rs.getString("privilegios")),
                    rs.getBoolean("ativo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return usuarios;
    }
}
