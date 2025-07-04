package control;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DBQuery;
import model.Cliente;
import model.enums.TipoCliente;
import model.enums.Uf;

public class ClienteDAO {
	private DBQuery dbQuery;
	private String editableFieldsName = "nome, cpf_cnpj, tipo_cliente, telefone, "
			+ "email, endereco, numero, complemento, "
			+ "bairro, cidade, uf, cep";
	
	public ClienteDAO() {
        String tableName = "tb_cliente";
        String fieldNames = "id_cliente, nome, cpf_cnpj, tipo_cliente, "
        		+ "telefone, email, endereco, numero, complemento, "
        		+ "bairro, cidade, uf, cep, ativo";
        String fieldKey = "id_cliente";
        
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	
	public boolean salvar(Cliente cliente) {
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
        
        return dbQuery.insert(values, editableFieldsName) > 0;
    }
	
	public List<Cliente> listarTodos(){
		List<Cliente> clientes = new ArrayList<>();
		
		try {
	        ResultSet rs = dbQuery.select("");

	        while (rs.next()) {
	            Cliente cliente = new Cliente();
	            cliente.setId(rs.getInt("id_cliente"));
	            cliente.setNome(rs.getString("nome"));
	            cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
	            cliente.setTipo(TipoCliente.fromDb(rs.getString("tipo_cliente")));
	            cliente.setTelefone(rs.getString("telefone"));
	            cliente.setEmail(rs.getString("email"));
	            cliente.setEndereco(rs.getString("endereco"));
	            cliente.setNumero(rs.getString("numero"));
	            cliente.setComplemento(rs.getString("complemento"));
	            cliente.setBairro(rs.getString("bairro"));
	            cliente.setCidade(rs.getString("cidade"));
	            cliente.setUf(Uf.fromDb(rs.getString("uf")));
	            cliente.setCep(rs.getString("cep"));
	            cliente.setAtivo(rs.getBoolean("ativo"));

	            clientes.add(cliente);
	        }

	        rs.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

		return clientes;
	}
	
	public boolean atualizar(Cliente cliente) {
		String[] values = {
			String.valueOf(cliente.getId()),
			cliente.getNome(),
			cliente.getCpfCnpj(),
			cliente.getTipo().name(),
			cliente.getTelefone(),
			cliente.getEmail(),
			cliente.getEndereco(),
			cliente.getNumero(),
			cliente.getComplemento(),
			cliente.getBairro(),
			cliente.getCidade(),
			cliente.getUf().name(),
			cliente.getCep(),
			cliente.isAtivo() ? "1" : "0"
		};
		
		return dbQuery.update(values) > 0;
	}
	
	public boolean desativar(Cliente cliente) {
	    if (cliente == null || cliente.getId() <= 0) {
	        throw new IllegalArgumentException("Cliente inválido para desativar.");
	    }

	    String sql = "UPDATE " + dbQuery.getTableName() +
	                 " SET ativo = 0 WHERE " + dbQuery.getFieldKey() + " = " + cliente.getId();

	    int rowsAffected = dbQuery.execute(sql);
	    return rowsAffected > 0;
	}
	
	public List<Cliente> listarApenasAtivos() {
	    List<Cliente> clientes = new ArrayList<>();

	    try {
	        ResultSet rs = dbQuery.select("ativo = 1");

	        while (rs.next()) {
	            Cliente cliente = new Cliente();
	            cliente.setId(rs.getInt("id_cliente"));
	            cliente.setNome(rs.getString("nome"));
	            cliente.setCpfCnpj(rs.getString("cpf_cnpj"));
	            cliente.setTipo(TipoCliente.fromDb(rs.getString("tipo_cliente")));
	            cliente.setTelefone(rs.getString("telefone"));
	            cliente.setEmail(rs.getString("email"));
	            cliente.setEndereco(rs.getString("endereco"));
	            cliente.setNumero(rs.getString("numero"));
	            cliente.setComplemento(rs.getString("complemento"));
	            cliente.setBairro(rs.getString("bairro"));
	            cliente.setCidade(rs.getString("cidade"));
	            cliente.setUf(Uf.fromDb(rs.getString("uf")));
	            cliente.setCep(rs.getString("cep"));
	            cliente.setAtivo(rs.getBoolean("ativo"));

	            clientes.add(cliente);
	        }

	        rs.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return clientes;
	}
}
