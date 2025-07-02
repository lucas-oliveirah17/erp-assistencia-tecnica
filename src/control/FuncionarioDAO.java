package control;

import database.DBQuery;
import model.Funcionario;

public class FuncionarioDAO {
	private DBQuery dbQuery;
	
	public FuncionarioDAO() {
        String tableName = "tb_funcionario";
        String fieldNames = "id_funcionario, nome, cpf, funcao, "
        		+ "telefone, email, ativo";
        String fieldKey = "id_funcionario";
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	
	public boolean salvar(Funcionario funcionario) {
		String fieldsNameInsert = "nome, cpf, funcao, telefone, email";
		
        String[] values = {
        	funcionario.getNome(),
	        funcionario.getCpf(),
	        funcionario.getFuncao().getValorDb(),
	        funcionario.getTelefone(),
	        funcionario.getEmail()
        };
        
        return dbQuery.insert(values, fieldsNameInsert) > 0;
    }
}
