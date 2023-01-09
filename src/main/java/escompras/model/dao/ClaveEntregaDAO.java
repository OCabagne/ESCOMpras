package escompras.model.dao;

import escompras.model.entity.ClaveEntrega;
import escompras.model.entity.Orden;
import escompras.util.BaseDAO;

import java.util.List;

public class ClaveEntregaDAO extends BaseDAO
{
	private ClaveEntregaDAO() {}
	
	public static void Create(ClaveEntrega ClaveEntrega)
	{
		DoTransaction((session) -> session.save(ClaveEntrega), "CreateClaveEntrega");
	}
	
	public static ClaveEntrega Read(Orden orden)
	{
		return DoTransaction(s -> {
			List<ClaveEntrega> list = s.createQuery(
				"select c from ClaveEntrega c where c.orden.id = :ordenID",
				ClaveEntrega.class
			).setParameter("ordenID", orden.getIdOrden()).list();
			if (list.isEmpty()) return null;
			return list.get(0);
		}, "ReadClaveEntrega");
	}
	
	public static void Delete(Orden orden)
	{
		ClaveEntrega ce = Read(orden);
		DoTransaction(
			(session) -> { session.delete(ce); return null; },
			"DeleteClaveEntrega"
		);
	}
}
