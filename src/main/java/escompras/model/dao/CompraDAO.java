package escompras.model.dao;

import escompras.model.dto.CompraDTO;
import escompras.model.entity.Compra;
import escompras.model.entity.Orden;
import escompras.model.entity.Producto;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompraDAO extends BaseDAO
{
	private CompraDAO() {}
	
	public static Compra Create(Compra compra)
	{
		return DoTransaction(
			(session) -> compra.setIdCompra((Integer) session.save(compra)),
			"CreateCompra"
		);
	}
	
	public static Compra Read(Orden orden, Producto producto)
	{
		return DoTransaction(s -> {
			Query<Compra> query = s.createQuery(
				"from Compra c left join fetch c.producto " +
				"where c.orden = :orden and c.producto = :producto",
				Compra.class
			);
			query.setParameter("orden", orden);
			query.setParameter("producto", producto);
			return query.list().stream().findFirst().orElse(null);
		},"ReadAllCompra");
	}
	public static Compra Read(Compra compra)
	{
		return DoTransaction(
			(session) -> session.get(compra.getClass(), compra.getIdCompra()),
			"ReadCompra"
		);
	}
	
	public static void Update(Compra compra)
	{
		DoTransaction(
			(session) -> { session.update(compra); return null; },
			"UpdateCompra"
		);
	}
	
	public static void Delete(Compra compra)
	{
		Compra o = Read(compra);
		DoTransaction(
			(session) -> { session.delete(o); return null; },
			"DeleteCompra"
		);
	}
	
	public static List<Compra> ReadAll()
	{
		return DoTransaction(
			(s) -> s.createQuery("from Compra c", Compra.class).list(),
			"ReadAllCompra"
		);
	}
	
	public static List<Compra> ReadAllFrom(Orden orden)
	{
		return DoTransaction(s -> {
			Query<Compra> query = s.createQuery(
				"from Compra c where c.orden = :orden",
				Compra.class
			);
			query.setParameter("orden", orden);
			return query.list();
		},"ReadAllCompra");
	}
	
	public static List<CompraDTO> ReadAllDTOFrom(Orden orden)
	{
		return DoTransaction(s -> {
			Query<Compra> query = s.createQuery(
				"from Compra c left join fetch c.producto where c.orden = :orden",
				Compra.class
			);
			query.setParameter("orden", orden);
			return query.list().stream().map(CompraDTO::new)
				.collect(Collectors.toCollection(ArrayList::new));
		},"ReadAllDTOCompra");
	}
}
