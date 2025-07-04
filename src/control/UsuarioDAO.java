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
    private String editableFieldsName = "email, senha, privilegios, ativo";

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
            usuario.getPrivilegios().getValorDb(),
            usuario.isAtivo() ? "1" : "0"
        };

        return dbQuery.insert(values, editableFieldsName) > 0;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("");

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPrivilegios(Privilegios.fromDb(rs.getString("privilegios")));
                usuario.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(usuario);
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
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPrivilegios(Privilegios.fromDb(rs.getString("privilegios")));
                usuario.setAtivo(rs.getBoolean("ativo"));

                usuarios.add(usuario);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public boolean atualizar(Usuario usuario) {
        String[] values = {
            String.valueOf(usuario.getId()),
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getPrivilegios().name(),
            usuario.isAtivo() ? "1" : "0"
        };

        return dbQuery.update(values) > 0;
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
}