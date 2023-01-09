package escompras.model.dao;

import escompras.model.entity.Tipo;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TipoDAO extends BaseDAO
{
	private TipoDAO() {}
	
	public static Tipo Create(Tipo tipo)
	{
		return DoTransaction(
			(session) -> tipo.setId((Integer) session.save(tipo)),
			"CreateTipo"
		);
	}
	
	public static Tipo Read(int tipoID) { return Read(new Tipo(tipoID)); }
	public static Tipo Read(Tipo tipo)
	{
		return DoTransaction(
			(session) -> session.get(tipo.getClass(), tipo.getIdTipo()),
			"ReadTipo"
		);
	}
	
	public static void Update(Tipo tipo)
	{
		DoTransaction(
			(session) -> { session.update(tipo); return null; },
			"UpdateTipo"
		);
	}
	
	public static void Delete(int tipoID) { Delete(new Tipo(tipoID));}
	public static void Delete(Tipo tipo)
	{
		Tipo o = Read(tipo);
		DoTransaction( (session) -> { session.delete(o); return null; }, "DeleteTipo" );
	}
	
	public static ArrayList<Tipo> ReadAll()
	{
		return (ArrayList<Tipo>) DoTransaction(
			(s) -> s.createQuery("from Tipo t order by t.nombre", Tipo.class).list(),
			"ReadAllTipo"
		);
	}
	
	public static ArrayList<String> ReadAllAsString()
	{
		return ReadAll().stream().map(Tipo::getNombre).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static Tipo ReadByName(String tipoTienda)
	{
		if (tipoTienda == null)
			return null;
		return DoTransaction(session -> {
			Query<Tipo> query = session.createQuery(
				"from Tipo t where t.nombre = :nombre",
				Tipo.class
			);
			query.setParameter("nombre", tipoTienda);
			try { return query.getSingleResult(); }
			catch(NoResultException ignored) { return null; }
		}, "ReadTipoByName");
	}
}