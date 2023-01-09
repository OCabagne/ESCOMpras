package escompras.model.dto;

import escompras.model.entity.Compra;
import lombok.Getter;

@Getter
public class CompraDTO
{
	private final String producto;
	private final int cantidad;
	private final String detalles;
	
	public CompraDTO(Compra compra)
	{
		producto = compra.getProducto().getNombre();
		cantidad = compra.getCantidad();
		detalles = compra.getDetalles();
	}
}
