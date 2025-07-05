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
	private String editableFieldsName = 
		"nome, cpf_cnpj, tipo_cliente, telefone, "
		+ "email, endereco, numero, complemento, "
		+ "bairro, cidade, uf, cep";
	
	public ClienteDAO() {
        String tableName = "tb_cliente";
        String fieldNames = 
    		"id_cliente, nome, cpf_cnpj, tipo_cliente, "
    		+ "telefone, email, endereco, numero, complemento, "
    		+ "bairro, cidade, uf, cep, ativo";
        String fieldKey = "id_cliente";
        
        dbQuery = new DBQuery(tableName, fieldNames, fieldKey);
    }
	
	public boolean salvar(Cliente cliente) {
        String[] valores = {
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
        
        return dbQuery.insert(valores, editableFieldsName) > 0;
    }
	
	public List<Cliente> listarTodos(){
		List<Cliente> clientes = new ArrayList<>();
		
		try {
	        ResultSet rs = dbQuery.select("");

	        while (rs.next()) {
	            Cliente c = new Cliente();
	            c.setId(rs.getInt("id_cliente"));
	            c.setNome(rs.getString("nome"));
	            c.setCpfCnpj(rs.getString("cpf_cnpj"));
	            c.setTipo(TipoCliente.fromDb(rs.getString("tipo_cliente")));
	            c.setTelefone(rs.getString("telefone"));
	            c.setEmail(rs.getString("email"));
	            c.setEndereco(rs.getString("endereco"));
	            c.setNumero(rs.getString("numero"));
	            c.setComplemento(rs.getString("complemento"));
	            c.setBairro(rs.getString("bairro"));
	            c.setCidade(rs.getString("cidade"));
	            c.setUf(Uf.fromDb(rs.getString("uf")));
	            c.setCep(rs.getString("cep"));
	            c.setAtivo(rs.getBoolean("ativo"));

	            clientes.add(c);
	        }

	        rs.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

		return clientes;
	}
	
	public boolean atualizar(Cliente cliente) {
		String[] valores = {
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
		
		return dbQuery.update(valores) > 0;
	}
	
	public boolean ativar(Cliente cliente) {
	    if (cliente == null || cliente.getId() <= 0) {
	        throw new IllegalArgumentException("Cliente inválido para aativar.");
	    }

	    String sql = "UPDATE " + dbQuery.getTableName() +
	                 " SET ativo = 1 WHERE " + dbQuery.getFieldKey() + " = " + cliente.getId();

	    int rowsAffected = dbQuery.execute(sql);
	    return rowsAffected > 0;
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
	
	public boolean excluir(Cliente cliente) {
		String[] valores = {
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

	    // Retorna true se a operação de exclusão for bem-sucedida
	    return dbQuery.delete(valores) > 0;
	}
	
	public List<Cliente> listarApenasAtivos() {
	    List<Cliente> clientes = new ArrayList<>();

	    try {
	        ResultSet rs = dbQuery.select("ativo = 1");

	        while (rs.next()) {
	            Cliente c = new Cliente();
	            c.setId(rs.getInt("id_cliente"));
	            c.setNome(rs.getString("nome"));
	            c.setCpfCnpj(rs.getString("cpf_cnpj"));
	            c.setTipo(TipoCliente.fromDb(rs.getString("tipo_cliente")));
	            c.setTelefone(rs.getString("telefone"));
	            c.setEmail(rs.getString("email"));
	            c.setEndereco(rs.getString("endereco"));
	            c.setNumero(rs.getString("numero"));
	            c.setComplemento(rs.getString("complemento"));
	            c.setBairro(rs.getString("bairro"));
	            c.setCidade(rs.getString("cidade"));
	            c.setUf(Uf.fromDb(rs.getString("uf")));
	            c.setCep(rs.getString("cep"));
	            c.setAtivo(rs.getBoolean("ativo"));

	            clientes.add(c);
	        }

	        rs.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return clientes;
	}
}
