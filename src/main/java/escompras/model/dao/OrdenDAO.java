package escompras.model.dao;

import escompras.model.entity.*;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import java.util.List;

public class OrdenDAO extends BaseDAO
{
	private OrdenDAO() {}
	
	public static Orden Create(Orden orden)
	{
		return DoTransaction(
			(session) -> orden.setId((Integer) session.save(orden)),
			"CreateOrden"
		);
	}
	
	public static Orden Create(Cliente cliente, Tienda tienda, List<Compra> productos, HoraServicio entrega)
	{
		Orden orden = new Orden(cliente, tienda)
			.setHoraEntrega(entrega)
			.setMontoTotal(productos.stream().mapToInt(c -> c.getCantidad() * c.getPrecioProducto()).sum());
		return DoTransaction(s -> {
			orden.setId((Integer) s.save(orden));
			productos.forEach(producto -> s.save(producto.setOrden(orden)));
			return orden;
		}, "CreateOrdenConCompras");
	}
	
	public static Orden Read(int ordenID) { return Read(new Orden(ordenID)); }
	public static Orden Read(Orden orden)
	{
		return DoTransaction(
			(session) -> session.get(orden.getClass(), orden.getIdOrden()),
			"ReadOrden"
		);
	}
	
	public static void Update(Orden orden)
	{
		DoTransaction(
			(session) -> { session.update(orden); return null; },
			"UpdateOrden"
		);
	}
	
	public static void Delete(int ordenID) { Delete(new Orden(ordenID));}
	public static void Delete(Orden orden)
	{
		Orden o = Read(orden);
		DoTransaction( (session) -> { session.delete(o); return null; }, "DeleteOrden" );
	}
	
	public static List<Orden> ReadAll()
	{
		return DoTransaction(
			(s) -> s.createQuery("from Orden c", Orden.class).list(),
			"ReadAllOrden"
		);
	}
	
	public static List<Orden> ReadAllFrom(Cliente cliente)
	{
		return DoTransaction((s) -> {
			Query<Orden> query = s.createQuery(
				"from Orden c where c.cliente = :cliente " +
				"order by fecha desc, horaEntrega.day, horaEntrega.horaEntrega",
				Orden.class
			);
			query.setParameter("cliente", cliente);
			return query.list();
		},"ReadAllOrden");
	}
	
	public static List<Orden> ReadAllFrom(Tienda tienda)
	{
		return DoTransaction((s) -> {
			Query<Orden> query = s.createQuery(
				"from Orden c where c.tienda = :tienda " +
				"order by fecha desc, horaEntrega.day, horaEntrega.horaEntrega",
				Orden.class
			);
			query.setParameter("tienda", tienda);
			return query.list();
		},"ReadAllOrden");
	}
}