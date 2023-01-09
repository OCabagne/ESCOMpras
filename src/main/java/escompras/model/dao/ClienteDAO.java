package escompras.model.dao;

import escompras.model.entity.Cliente;
import escompras.util.BaseDAO;

import java.util.ArrayList;

public class ClienteDAO extends BaseDAO
{
	private ClienteDAO() {}
	
	public static Cliente Create(Cliente cliente)
	{
		return DoTransaction(
			(session) -> cliente.setId((Integer) session.save(cliente)),
			"CreateCliente"
		);
	}
	
	public static Cliente Read(int clienteID) { return Read(new Cliente(clienteID)); }
	public static Cliente Read(Cliente cliente)
	{
		return DoTransaction(
			(session) -> session.get(cliente.getClass(), cliente.getIdCliente()),
			"ReadCliente"
		);
	}
	
	public static void Update(Cliente cliente)
	{
		DoTransaction(
			(session) -> { session.update(cliente); return null; },
			"UpdateCliente"
		);
	}
	
	public static void Delete(int clienteID) { Delete(new Cliente(clienteID));}
	public static void Delete(Cliente cliente)
	{
		Cliente c = Read(cliente);
		DoTransaction( (session) -> { session.delete(c); return null; }, "DeleteCliente" );
	}
	
	public static ArrayList<Cliente> ReadAll()
	{
		return (ArrayList<Cliente>) DoTransaction(
			(s) -> s.createQuery("from Cliente c order by c.nombre", Cliente.class).list(),
			"ReadAllCliente"
		);
	}
	
	public static Cliente ReadConEscuela(int id)
	{
		return DoTransaction(s -> s.createQuery(
			"select c from Cliente c left join fetch c.escuela where c.id = :cid",
			Cliente.class
		).setParameter("cid", id).getSingleResult(), "EagerReadCliente");
	}
	
	public static Cliente ReadConLikes(Cliente cliente)
	{
		return DoTransaction(s -> s.createQuery(
			"select c from Cliente c left join fetch c.favoritos where c = :cliente",
			Cliente.class
		).setParameter("cliente", cliente).getSingleResult(), "EagerReadCliente");
	}
}
