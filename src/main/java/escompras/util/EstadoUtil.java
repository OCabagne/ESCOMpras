package escompras.util;

import escompras.model.entity.Orden;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadoUtil
{
	@Getter(lazy = true, value = AccessLevel.PRIVATE)
	private static final Map<Orden.Estado, Siguiente> estadoMap = RellenarEstados();
	
	private static Map<Orden.Estado, Siguiente> RellenarEstados()
	{
		Map<Orden.Estado, Siguiente> map = new HashMap<>(6);
		map.put(Orden.Estado.SOLICITADO, new Siguiente(
			new Orden.Estado[] {Orden.Estado.CANCELADO},
			new Orden.Estado[] {Orden.Estado.RECHAZADO, Orden.Estado.APROBADO}
		));
		map.put(Orden.Estado.APROBADO, new Siguiente(
			new Orden.Estado[] {Orden.Estado.CANCELADO},
			new Orden.Estado[] {Orden.Estado.ENVIADO}
		));
		map.put(Orden.Estado.ENVIADO, new Siguiente(
			new Orden.Estado[] {Orden.Estado.RECIBIDO},
			new Orden.Estado[] {}
		));
		map.put(Orden.Estado.RECIBIDO, new Siguiente(
			new Orden.Estado[] {},
			new Orden.Estado[] {}
		));
		map.put(Orden.Estado.RECHAZADO, new Siguiente(
			new Orden.Estado[] {},
			new Orden.Estado[] {}
		));
		map.put(Orden.Estado.CANCELADO, new Siguiente(
			new Orden.Estado[] {},
			new Orden.Estado[] {}
		));
		return map;
	}
	
	public static boolean EsSiguiente(Orden.Estado actual, Orden.Estado siguiente, boolean isCliente)
	{
		if (actual == null) return false;
		return getEstadoMap().get(actual).SigueA(siguiente, isCliente);
	}
	
	public static List<String> getEstadoValues()
	{
		return Arrays.stream(Orden.Estado.values()).map(Enum::toString).collect(Collectors.toList());
	}
}

@AllArgsConstructor
@Getter
class Siguiente
{
	private final Orden.Estado[] sigIfCliente;
	private final Orden.Estado[] sigIfTienda;
	
	public Orden.Estado[] of(boolean isCliente)
	{
		return isCliente ? sigIfCliente : sigIfTienda;
	}
	
	public boolean SigueA(Orden.Estado estado, boolean isCliente)
	{
		return Arrays.stream(isCliente ? sigIfCliente : sigIfTienda)
			.anyMatch(otro -> otro == estado);
	}
}