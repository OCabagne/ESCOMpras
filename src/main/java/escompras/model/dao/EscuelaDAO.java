package escompras.model.dao;

import escompras.model.entity.Escuela;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EscuelaDAO extends BaseDAO
{
	private EscuelaDAO() {}
	
	public static Escuela Create(Escuela escuela)
	{
		return DoTransaction(
			(session) -> escuela.setId((Integer) session.save(escuela)),
			"CreateEscuela"
		);
	}
	
	public static Escuela Read(int escuelaID) { return Read(new Escuela(escuelaID)); }
	public static Escuela Read(Escuela escuela)
	{
		return DoTransaction(
			(session) -> session.get(escuela.getClass(), escuela.getIdEscuela()),
			"ReadEscuela"
		);
	}
	
	public static void Update(Escuela escuela)
	{
		DoTransaction(
			(session) -> { session.update(escuela); return null; },
			"UpdateEscuela"
		);
	}
	
	public static void Delete(int escuelaID) { Delete(new Escuela(escuelaID));}
	public static void Delete(Escuela escuela)
	{
		Escuela o = Read(escuela);
		DoTransaction(
			(session) -> { session.delete(o); return null; },
			"DeleteEscuela"
		);
	}
	
	public static ArrayList<Escuela> ReadAll()
	{
		return (ArrayList<Escuela>) DoTransaction(
			(s) -> s.createQuery("from Escuela e order by e.nombre", Escuela.class).list(),
			"ReadAllEscuela"
		);
	}
	
	public static List<String> ReadAllNames()
	{
		return ReadAll().stream().map(Escuela::getNombre).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static Escuela ReadByName(String name)
	{
		return DoTransaction(
			(session) -> {
				Query<Escuela> query =  session.createQuery(
					"from Escuela e where e.nombre = :name",
					Escuela.class
				);
				query.setParameter("name", name);
				List<Escuela> list = query.list();
				if (list.isEmpty())
					return null;
				else
					return list.get(0);
			}, "ReadByNameEscuela"
		);
	}
}