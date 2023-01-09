package escompras.model.dao;

import escompras.model.dto.ProductoDTO;
import escompras.model.entity.Cliente;
import escompras.model.entity.Producto;
import escompras.model.entity.Tienda;
import escompras.model.entity.Tipo;
import escompras.util.BaseDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductoDAO extends BaseDAO
{
	private ProductoDAO() {}
	
	public static Producto Create(Producto producto)
	{
		return DoTransaction(
			(session) -> producto.setId((Integer) session.save(producto)),
			"CreateProducto"
		);
	}
	
	public static Producto Read(int productoID) { return Read(new Producto(productoID)); }
	public static Producto Read(Producto producto)
	{
		return DoTransaction(
			(session) -> session.get(producto.getClass(), producto.getIdProducto()),
			"ReadProducto"
		);
	}
	public static Producto Read(int productoID, Tienda tienda)
	{
		Producto producto = new Producto(productoID);
		return DoTransaction(
			(session) -> {
				Producto p = session.get(producto.getClass(), producto.getIdProducto());
				return p.getTienda().equals(tienda) ? p : null;
			}, "ReadProducto"
		);
	}
	
	public static void Update(Producto producto)
	{
		DoTransaction(
			(session) -> { session.update(producto); return null; },
			"UpdateProducto"
		);
	}
	
	public static void Delete(int productoID) { Delete(new Producto(productoID));}
	public static void Delete(Producto producto)
	{
		Producto o = Read(producto);
		DoTransaction( (session) -> { session.delete(o); return null; }, "DeleteProducto" );
	}
	
	public static List<ProductoDTO> ReadAll()
	{
		return DoTransaction(s -> s.createQuery(
				"select p from Producto p left join fetch p.tienda order by p.nombre"
				, Producto.class
			).setMaxResults(30).list(), "ReadAllProducto").stream()
			.map(p -> new ProductoDTO(p, p.getTienda().getNombre()))
			.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static List<Producto> ReadAll(Tienda tienda)
	{
		return DoTransaction((s) -> s.createQuery(
			"from Producto p where p.tienda = :tienda order by p.nombre",
			Producto.class
		).setParameter("tienda", tienda).list(), "ReadAllProducto");
	}
	
	public static List<ProductoDTO> ReadAll(Cliente cliente)
	{
		return DoTransaction((s) -> s.createQuery(
			//"select p from Producto p where p in :favoritos",
			"select p from Cliente c inner join c.favoritos p inner join fetch p.tienda " +
			"where c =:cliente",
			Producto.class
		).setParameter("cliente", cliente).list(), "ReadAllProductoFavoritos")
			.stream().map(p -> new ProductoDTO(p, p.getTienda().getNombre()))
			.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static List<ProductoDTO> ReadAll(Tipo tipo)
	{
		return tipo == null ? ReadAll() : DoTransaction(s -> s.createQuery(
				"select p from Producto p left join fetch p.tienda " +
				"where p.tienda.tipo = :tipo order by p.nombre", Producto.class
			).setParameter("tipo", tipo).setMaxResults(30).list(),
			"ReadAllProducto"
		).stream().map(p -> new ProductoDTO(p, p.getTienda().getNombre()))
		.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public static List<ProductoDTO> Search(List<String> keyList)
	{
		if (keyList == null || keyList.isEmpty()) return new ArrayList<>();
		@AllArgsConstructor class Tuple { @Getter int id, value; };
		List<Integer> resultados = DoTransaction(s -> s.createQuery(
			"select new map(p.id as id, "
			+ "    concat(p.nombre, ' ', p.descripcion) as text"
			+ ") from Producto p", Map.class
		).list(), "SearchProductoText").stream().map(map -> {
			final String[] resText = { map.get("text").toString().toLowerCase() };
			keyList.forEach(s -> resText[0] = resText[0].replace(s, "×"));
			return new Tuple((Integer) map.get("id"),
				resText[0].replaceAll("(×\\S)|(\\S×)|([^×])", "").length()
			);
		}).filter(tuple -> tuple.value > 0)
			.sorted(Comparator.comparingInt(Tuple::getValue).reversed())
			.limit(30).map(tuple -> tuple.id).collect(Collectors.toList());
		Map<Integer, ProductoDTO> prods = new HashMap<>();
		DoTransaction(s -> s.createQuery(
			"select p from Producto p left join fetch p.tienda " +
			"where p.id in :found", Producto.class
		).setParameter("found", resultados).list(), "SearchProducto")
			.stream().map(p -> new ProductoDTO(p, p.getTienda().getNombre()))
			.forEach(pdto -> prods.put(pdto.getId(), pdto));
		return resultados.stream().map(prods::get).collect(Collectors.toList());
	}
	
	public static Producto EagerRead(int id)
	{
		return DoTransaction(s -> s.createQuery(
			"select p from Producto p left join fetch p.tienda where p.id = :prodID",
			Producto.class
		).setParameter("prodID", id).getSingleResult(),
		"EagerReadProducto");
	}
	
	public static List<ProductoDTO> FillLikes(List<ProductoDTO> lista, Cliente cliente)
	{
		if (cliente == null) return lista;
		Map<Integer, Integer> likes = DoTransaction(
			s -> s.createQuery(
				"select p.id from Cliente c inner join c.favoritos p " +
				"where p.id in :ids and c = :cliente", Integer.class
			).setParameter("ids",
				lista.stream().map(ProductoDTO::getId).collect(Collectors.toList())
			).setParameter("cliente", cliente).list(),
			"FillProductoLikes"
		).stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
		for (ProductoDTO pdto : lista) pdto.setLike(likes.containsKey(pdto.getId()));
		return lista;
	}
}