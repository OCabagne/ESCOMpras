package escompras.model.dao;

import escompras.model.entity.Ingreso;
import escompras.util.BaseDAO;

import java.util.ArrayList;

public class IngresoDAO extends BaseDAO
{
	private IngresoDAO() {}
	
	public static Ingreso Create(Ingreso ingreso)
	{
		return DoTransaction(
			(session) -> ingreso.setId((Integer) session.save(ingreso)),
			"CreateIngreso"
		);
	}
	
	public static ArrayList<Ingreso> ReadAll()
	{
		return (ArrayList<Ingreso>) DoTransaction(
			(s) -> s.createQuery("from Ingreso i order by i.id", Ingreso.class).list(),
			"ReadAllIngreso"
		);
	}
}
