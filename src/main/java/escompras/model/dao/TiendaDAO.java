package escompras.model.dao;

import escompras.model.entity.Tienda;
import escompras.util.BaseDAO;

import java.util.ArrayList;

public class TiendaDAO extends BaseDAO
{
	private TiendaDAO() {}
	
	public static Tienda Create(Tienda tienda)
	{
		return DoTransaction(
			(session) -> tienda.setId((Integer) session.save(tienda)),
			"CreateTienda"
		);
	}
	
	public static Tienda Read(int tiendaID) { return Read(new Tienda(tiendaID)); }
	public static Tienda Read(Tienda tienda)
	{
		return DoTransaction(
			(session) -> session.get(tienda.getClass(), tienda.getIdTienda()),
			"ReadTienda"
		);
	}
	
	public static void Update(Tienda tienda)
	{
		DoTransaction(
			(session) -> { session.update(tienda); return null; },
			"UpdateTienda"
		);
	}
	
	public static void Delete(int tiendaID) { Delete(new Tienda(tiendaID));}
	public static void Delete(Tienda tienda)
	{
		Tienda o = Read(tienda);
		DoTransaction( (session) -> { session.delete(o); return null; }, "DeleteTienda" );
	}
	
	public static ArrayList<Tienda> ReadAll()
	{
		return (ArrayList<Tienda>) DoTransaction(
			(s) -> s.createQuery("from Tienda c order by c.nombre", Tienda.class).list(),
			"ReadAllTienda"
		);
	}
	
	public static Tienda Read(String nombre)
	{
		return  DoTransaction((s) -> s.createQuery(
			"from Tienda t where t.nombre = :nombre", Tienda.class
			).setParameter("nombre", nombre).list().get(0),
			"ReadAllTienda"
		);
	}
}