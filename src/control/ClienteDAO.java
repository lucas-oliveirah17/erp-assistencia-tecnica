package control;

import database.DBQuery;
import model.Cliente;

public class ClienteDAO {
	private DBQuery dbQuery;
	
	public ClienteDAO() {
        String tableName = "tb_cliente";
        String fieldNames = "id_cliente, nome, cpf_cnpj, tipo_cliente, "
        		+ "telefone, email, endereco, numero, complemento, "
        		+ "bairro, cidade, uf, cep, ativo";
        String fieldKey = "id_cliente";
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	
	public boolean salvar(Cliente cliente) {
		String fieldsNameInsert = "nome, cpf_cnpj, tipo_cliente, telefone, "
				+ "email, endereco, numero, complemento, "
				+ "bairro, cidade, uf, cep";
		
        String[] values = {
	        cliente.getNome(),
	        cliente.getCpfCnpj(),
	        cliente.getTipo().getValorDb(),
	        cliente.getTelefone(),
	        cliente.getEmail(),
	        cliente.getEndereco(),
	        cliente.getNumero(),
	        cliente.getComplemento(),
	        cliente.getBairro(),
	        cliente.getCidade(),
	        cliente.getUf().getValorDb(),
	        cliente.getCep()
        };
        
        return dbQuery.insert(values, fieldsNameInsert) > 0;
    }
}
