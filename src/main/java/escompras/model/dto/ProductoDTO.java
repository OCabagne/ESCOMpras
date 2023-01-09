package escompras.model.dto;

import escompras.model.entity.Producto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @ToString
public class ProductoDTO
{
	private final int id;
	private final String nombre;
	private final float precio;
	private final String descripcion;
	private final int cantidad;
	private final String promocion;
	private final String unidad;
	private String tienda;
	@Setter private boolean like;
	
	public ProductoDTO(Producto producto)
	{
		id = producto.getIdProducto();
		nombre = producto.getNombre();
		precio = producto.getPrecio() / 100f;
		descripcion = producto.getDescripcion();
		cantidad = producto.getCantidad();
		promocion = producto.getPromocion();
		unidad = producto.getUnidad();
	}
	
	public ProductoDTO(Producto producto, String tiendaNombre)
	{
		id = producto.getIdProducto();
		nombre = producto.getNombre();
		precio = producto.getPrecio() / 100f;
		descripcion = producto.getDescripcion();
		cantidad = producto.getCantidad();
		promocion = producto.getPromocion();
		unidad = producto.getUnidad();
		tienda = tiendaNombre;
	}
	
	public ProductoDTO(JsonObject json)
	{
		int id = -1;
		String nombre = null;
		float precio = -1;
		String descripcion = null;
		int cantidad = -1;
		String promocion = null;
		String unidad = null;
		try { id = json.getInt("id"); } catch (Exception ignored) {}
		try { nombre = json.getString("nombre"); } catch (Exception ignored) {}
		try { precio = Float.parseFloat(json.getString("precio")); } catch (Exception ignored) {}
		try { descripcion = json.getString("descripcion"); } catch (Exception ignored) {}
		try { cantidad = Integer.parseInt(json.getString("cantidad")); } catch (Exception ignored) {}
		try { promocion = json.getString("promocion"); } catch (Exception ignored) {}
		try { unidad = json.getString("unidad"); } catch (Exception ignored) {}
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.promocion = promocion;
		this.unidad = unidad;
		this.tienda = null;
	}
	
	public static List<ProductoDTO> ToDTO(List<Producto> from)
	{
		return from.stream().map(ProductoDTO::new).collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Producto ToProducto()
	{
		return new Producto(
			getId(),
			null,
			getNombre(),
			Math.round(getPrecio() * 100),
			getDescripcion(),
			getCantidad(),
			getPromocion(),
			getUnidad()
		);
	}
}
