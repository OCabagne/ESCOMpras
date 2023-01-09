package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HoraServicio
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idHoraServicio;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(nullable = false)
	private Escuela escuela;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(nullable = false)
	private Tienda tienda;
	private byte day;
	private short horaEntrega;	//Minutos pasada la media noche.
	
	public HoraServicio(int horaServicioID) { setIdHoraServicio(horaServicioID); }
	
	public HoraServicio(int id, Escuela school, Tienda shop, byte day, String entrega)
	{
		setIdHoraServicio(id);
		setDay(day);
		setEscuela(school);
		setTienda(shop);
		setHoraEntrega((short) HoraToInt(entrega));
	}
	
	public HoraServicio setId(int id) { setIdHoraServicio(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		return idHoraServicio != ((HoraServicio) o).idHoraServicio;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
	
	public static String MinutosToHora(int time)
	{
		int horas = time / 60;
		int minutos = time % 60;
		return "" + (horas / 10) + (horas % 10) + ":" + (minutos / 10) + (minutos % 10);
	}
	
	private static int HoraToInt(String hora)
	{
		if (hora == null || hora.isEmpty())
			return 0;
		return Byte.parseByte(hora.substring(0, hora.indexOf(':'))) * 60 +
		       Byte.parseByte(hora.substring(hora.indexOf(':') + 1));
	}
	
	public static final String[] days = {
		"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
	};
	
	public String toString()
	{
		return HoraServicio.days[day - 1] + " " + MinutosToHora(horaEntrega);
	}
}
