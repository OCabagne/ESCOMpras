package escompras.model.dto;

import escompras.model.entity.Producto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductoConID
{
	private final String nombre;
	private final int id;
	
	public ProductoConID(Producto producto)
	{
		nombre = producto.getNombre();
		id = producto.getIdProducto();
	}
	
	public static ArrayList<ProductoConID> ToDTOConID(List<Producto> productos)
	{
		return productos.stream().map(ProductoConID::new)
			.collect(Collectors.toCollection(ArrayList::new));
	}
}
