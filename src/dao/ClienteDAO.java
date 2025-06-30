package dao;

import database.DBQuery;
import model.Cliente;

public class ClienteDAO {
	private DBQuery dbQuery;
	
	public ClienteDAO() {
        String tableName = "tb_cliente";
        String fieldNames = "id_cliente, nome, cpf_cnpj, tipo_cliente, telefone, email, endereco, numero, bairro, cidade, uf, cep, complemento, ativo";
        String fieldKey = "id_cliente";
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	
	public boolean salvar(Cliente cliente) {
        String[] values = {
	        cliente.getNome(),
	        cliente.getCpfCnpj(),
	        cliente.getTipo().name(),
	        cliente.getTelefone(),
	        cliente.getEmail(),
	        cliente.getEndereco(),
	        cliente.getNumero(),
	        cliente.getBairro(),
	        cliente.getCidade(),
	        cliente.getUf().getValorDb(),
	        cliente.getCep(),
	        cliente.getComplemento(),
	        cliente.isAtivo() ? "1" : "0"
        };
        
        return dbQuery.insertWithoutId(values) > 0;
    }
}
