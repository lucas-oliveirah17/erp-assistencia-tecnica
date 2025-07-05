package control;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import database.DBQuery;

import model.Usuario;

import model.enums.Privilegios;

public class UsuarioDAO {
    private DBQuery dbQuery;
    private String editableFieldsName = 
		"email, senha, privilegios";

    public UsuarioDAO() {
        String tableName = "tb_usuario";
        String fieldNames = 
    		"id_usuario, email, senha, privilegios, ativo";
        String fieldKey = "id_usuario";

        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }

    public boolean salvar(Usuario usuario) {
        String[] valores = {
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getPrivilegios().getValorDb()
        };

        return dbQuery.insert(valores, editableFieldsName) > 0;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("");

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setPrivilegios(Privilegios.fromDb(rs.getString("privilegios")));
                u.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(u);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }
    
    public boolean atualizar(Usuario usuario) {
        String[] valores = {
            String.valueOf(usuario.getId()),
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getPrivilegios().name(),
            usuario.isAtivo() ? "1" : "0"
        };
        
        return dbQuery.update(valores) > 0;
    }
    
    public boolean ativar(Usuario usuario) {
	    if (usuario == null || usuario.getId() <= 0) {
	        throw new IllegalArgumentException("Usuário inválido para aativar.");
	    }

	    String sql = "UPDATE " + dbQuery.getTableName() +
	                 " SET ativo = 1 WHERE " + dbQuery.getFieldKey() + " = " + usuario.getId();

	    int rowsAffected = dbQuery.execute(sql);
	    return rowsAffected > 0;
	}
    
    public boolean desativar(Usuario usuario) {
        if (usuario == null || usuario.getId() <= 0) {
            throw new IllegalArgumentException("Usuário inválido para desativar.");
        }

        String sql = "UPDATE " + dbQuery.getTableName() +
                     " SET ativo = 0 WHERE " + dbQuery.getFieldKey() + " = " + usuario.getId();

        int rowsAffected = dbQuery.execute(sql);
        return rowsAffected > 0;
    }
    
    public boolean excluir(Usuario usuario) {
    	String[] valores = {
    			String.valueOf(usuario.getId()),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPrivilegios().name(),
                usuario.isAtivo() ? "1" : "0"
            };

	    // Retorna true se a operação de exclusão for bem-sucedida
	    return dbQuery.delete(valores) > 0;
	}

    public List<Usuario> listarApenasAtivos() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("ativo = 1");

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setPrivilegios(Privilegios.fromDb(rs.getString("privilegios")));
                u.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(u);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }
    
    public Usuario buscar(int id) {
    	try {
    		ResultSet rs = dbQuery.select("id_usuario = " + id);
    		
    		if (rs != null && rs.next()) {
	    		Usuario u = new Usuario();
	            u.setId(rs.getInt("id_usuario"));
	            u.setEmail(rs.getString("email"));
	            u.setSenha(rs.getString("senha"));
	            u.setPrivilegios(Privilegios.fromDb(rs.getString("privilegios")));
	            u.setAtivo(rs.getBoolean("ativo"));
	            
	            rs.close();
	            return u; 
    		}
            
    	} catch (SQLException e) {
            e.printStackTrace();
        }
    	return null;
    }
}