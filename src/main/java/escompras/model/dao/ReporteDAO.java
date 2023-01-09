package escompras.model.dao;

import escompras.model.entity.Cliente;
import escompras.model.entity.Reporte;
import escompras.model.entity.Tienda;
import escompras.util.BaseDAO;

import java.util.List;

public class ReporteDAO extends BaseDAO
{
	private ReporteDAO() {}
	
	public static Reporte Create(Reporte reporte)
	{
		return DoTransaction(
			(session) -> { session.save(reporte); return reporte; },
			"CreateReporte"
		);
	}
	
	public static Reporte Read(Cliente idP1, Tienda idP2) { return Read(new Reporte(idP1, idP2)); }
	public static Reporte Read(Reporte reporte)
	{
		return DoTransaction(
			(session) -> session.get(reporte.getClass(), reporte.getIdReporte()),
			"ReadReporte"
		);
	}
	
	public static void Update(Reporte reporte)
	{
		DoTransaction(
			(session) -> { session.update(reporte); return null; },
			"UpdateReporte"
		);
	}
	
	public static void Delete(Cliente idP1, Tienda idP2) { Delete(new Reporte(idP1, idP2)); }
	public static void Delete(Reporte reporte)
	{
		Reporte o = Read(reporte);
		DoTransaction(
			(session) -> { session.delete(o); return null; },
			"DeleteReporte"
		);
	}
	
	public static List<Reporte> ReadAll()
	{
		return DoTransaction(
			(s) -> s.createQuery("from Reporte c", Reporte.class).list(),
			"ReadAllReporte"
		);
	}
	
	public static List<String> ReadAll(Cliente cliente)
	{
		return DoTransaction((s) -> s.createQuery(
			"select c.descripcion from Reporte c where c.id.cliente = :cliente", String.class
			).setParameter("cliente", cliente).list(),
			"ReadAllReporteFromCliente"
		);
	}
}
