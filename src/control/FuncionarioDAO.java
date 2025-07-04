package control;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import database.DBQuery;

import model.Funcionario;
import model.enums.FuncaoFuncionario;

public class FuncionarioDAO {
    private DBQuery dbQuery;
    private String editableFieldsName = 
    	"nome, cpf, funcao, telefone, email";

    public FuncionarioDAO() {
        String tableName = "tb_funcionario";
        String fieldNames = 
    		"id_funcionario, nome, cpf, funcao, telefone, email, ativo";
        String fieldKey = "id_funcionario";
        
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }

    public boolean salvar(Funcionario funcionario) {
        String[] valores = {
            funcionario.getNome(),
            funcionario.getCpf(),
            funcionario.getFuncao().getValorDb(),
            funcionario.getTelefone(),
            funcionario.getEmail()
        };
        return dbQuery.insert(valores, editableFieldsName) > 0;
    }
    
    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("");

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setFuncao(FuncaoFuncionario.fromDb(rs.getString("funcao")));
                f.setTelefone(rs.getString("telefone"));
                f.setEmail(rs.getString("email"));
                f.setAtivo(rs.getBoolean("ativo"));

                funcionarios.add(f);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }

    public boolean atualizar(Funcionario funcionario) {
        String[] valores = {
            String.valueOf(funcionario.getId()),
            funcionario.getNome(),
            funcionario.getCpf(),
            funcionario.getFuncao().name(),
            funcionario.getTelefone(),
            funcionario.getEmail(),
            funcionario.isAtivo() ? "1" : "0"
        };
        return dbQuery.update(valores) > 0;
    }
    
    public boolean ativar(Funcionario funcionario) {
	    if (funcionario == null || funcionario.getId() <= 0) {
	        throw new IllegalArgumentException("Funcionário inválido para aativar.");
	    }

	    String sql = "UPDATE " + dbQuery.getTableName() +
	                 " SET ativo = 1 WHERE " + dbQuery.getFieldKey() + " = " + funcionario.getId();

	    int rowsAffected = dbQuery.execute(sql);
	    return rowsAffected > 0;
	}

    public boolean desativar(Funcionario funcionario) {
        if (funcionario == null || funcionario.getId() <= 0) {
            throw new IllegalArgumentException("Funcionário inválido para desativar.");
        }

        String sql = "UPDATE " + dbQuery.getTableName() +
                     " SET ativo = 0 WHERE " + dbQuery.getFieldKey() + " = " + funcionario.getId();

        int rowsAffected = dbQuery.execute(sql);
        return rowsAffected > 0;
    }
    
    public boolean excluir(Funcionario funcionario) {
    	String[] valores = {
                String.valueOf(funcionario.getId()),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getFuncao().name(),
                funcionario.getTelefone(),
                funcionario.getEmail(),
                funcionario.isAtivo() ? "1" : "0"
            };

	    // Retorna true se a operação de exclusão for bem-sucedida
	    return dbQuery.delete(valores) > 0;
	}

    public List<Funcionario> listarApenasAtivos() {
        List<Funcionario> funcionarios = new ArrayList<>();

        try {
            ResultSet rs = dbQuery.select("ativo = 1");

            while (rs.next()) {
                Funcionario f = new Funcionario();
                f.setId(rs.getInt("id_funcionario"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setFuncao(FuncaoFuncionario.fromDb(rs.getString("funcao")));
                f.setTelefone(rs.getString("telefone"));
                f.setEmail(rs.getString("email"));
                f.setAtivo(rs.getBoolean("ativo"));

                funcionarios.add(f);
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionarios;
    }
}
