package escompras.model.dto;

import escompras.model.entity.Escuela;
import escompras.model.entity.HoraServicio;
import escompras.model.entity.Tienda;
import lombok.Getter;

import javax.json.JsonObject;

@Getter
public class HoraServicioDTO
{
	private final int id;
	private final int day;
	private final String horaEntrega;	//Reloj de 24 h ("--:--").
	
	public HoraServicioDTO(HoraServicio horaServicio)
	{
		id = horaServicio.getIdHoraServicio();
		day = horaServicio.getDay();
		horaEntrega = HoraServicio.MinutosToHora(horaServicio.getHoraEntrega());
	}
	
	public HoraServicioDTO(JsonObject json)
	{
		int id = -1;
		int day = -1;
		String ent = null;
		try { id = json.getInt("id"); } catch (Exception ignored) {}
		try { day = Integer.parseInt(json.getString("day")); } catch (Exception ignored) {}
		try { ent = json.getString("entrega"); } catch (Exception ignored) {}
		this.id = id;
		this.day = day;
		this.horaEntrega = ent;
	}
	
	public HoraServicio ToHoraServicio(Escuela escuela, Tienda tienda)
	{
		return new HoraServicio(
			id,
			escuela,
			tienda,
			(byte) day,
			horaEntrega
		);
	}
	
	public String toString()
	{
		return HoraServicio.days[day - 1] + " " + horaEntrega;
	}
}
