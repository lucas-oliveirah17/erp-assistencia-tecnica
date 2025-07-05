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
		"id_funcionario, usuario, senha, email, privilegios";

    public UsuarioDAO() {
        String tableName = "tb_usuario";
        String fieldNames = 
    		"id_usuario, id_funcionario, usuario, senha, "
    		+ "email, privilegios, ativo";
        String fieldKey = "id_usuario";

        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }

    public boolean salvar(Usuario usuario) {
        String[] valores = {
    		String.valueOf(usuario.getIdFuncionario()),
            usuario.getUsuario(),            
            usuario.getSenha(),
            usuario.getEmail(),
            usuario.getPrivilegios().getValorDb()
        };

        return dbQuery.insert(valores, editableFieldsName) > 0;
    }
    
    public boolean atualizar(Usuario usuario) {
        String[] valores = {
            String.valueOf(usuario.getId()),
            String.valueOf(usuario.getIdFuncionario()),
            usuario.getUsuario(),
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getPrivilegios().name(),
            usuario.isAtivo() ? "1" : "0"
        };
        
        return dbQuery.update(valores) > 0;
    }
    
    public boolean ativar(Usuario usuario) {
	    if (usuario == null || usuario.getId() <= 0) {
	        throw new IllegalArgumentException("Usuário inválido para ativar.");
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
    			String.valueOf(usuario.getIdFuncionario()),
                usuario.getUsuario(),
                usuario.getSenha(),
                usuario.getEmail(),
                usuario.getPrivilegios().name(),
                usuario.isAtivo() ? "1" : "0"
            };

	    return dbQuery.delete(valores) > 0;
	}
    
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("");

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setIdFuncionario(rs.getInt("id_funcionario"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setEmail(rs.getString("email"));
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
    
    public List<Usuario> listarApenasAtivos() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("ativo = 1");

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setIdFuncionario(rs.getInt("id_funcionario"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setEmail(rs.getString("email"));
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
                u.setIdFuncionario(rs.getInt("id_funcionario"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setEmail(rs.getString("email"));
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
    
    public Usuario autenticar(String login, String senha) {
        try {
            String filtro = "(usuario = '" + login + "' OR email = '" + login + "') AND senha = '" + senha + "' AND ativo = 1";
            ResultSet rs = dbQuery.select(filtro);

            if (rs != null && rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setIdFuncionario(rs.getInt("id_funcionario"));
                u.setUsuario(rs.getString("usuario"));
                u.setSenha(rs.getString("senha"));
                u.setEmail(rs.getString("email"));
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