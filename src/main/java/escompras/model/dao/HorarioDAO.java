package escompras.model.dao;

import escompras.model.dto.HoraServicioDTO;
import escompras.model.dto.HorarioEscuelaDTO;
import escompras.model.entity.Escuela;
import escompras.model.entity.HoraServicio;
import escompras.model.entity.Tienda;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HorarioDAO extends BaseDAO
{
	private HorarioDAO() {}
	
	public static void CreateHorario(HoraServicio horaServicio)
	{
		if (horaServicio == null)
			return;
		DoTransaction(
			(session) -> horaServicio.setId((Integer) session.save(horaServicio)),
			"CreateHorario"
		);
	}
	
	public static HoraServicio ReadHorario(int horaServicioID)
	{
		return ReadHorario(
			new HoraServicio(horaServicioID)
		);
	}
	public static HoraServicio ReadHorario(HoraServicio hora)
	{
		return DoTransaction(
			(session) -> session.get(hora.getClass(), hora.getIdHoraServicio()),
			"ReadHoraServicio"
		);
	}
	
	public static void UpdateHorario(HoraServicio horaServicio)
	{
		DoTransaction((session) -> { session.update(horaServicio); return null; },"UpdateHorario");
	}
	
	public static void DeleteHorario(HoraServicio horaServicio)
	{
		HoraServicio hs = ReadHorario(horaServicio);
		DoTransaction( (session) -> { session.delete(hs); return null; }, "DeleteHorario" );
	}
	
	public static List<HoraServicioDTO> ReadAllHorario(Escuela escuela, Tienda tienda)
	{
		if (escuela == null || tienda == null)
			return null;
		return DoTransaction(
			session -> {
				Query<HoraServicio> query = session.createQuery(
					"from HoraServicio hs " +
					"where hs.escuela = :escuela " +
					"and hs.tienda = :tienda " +
					"order by hs.day, hs.horaEntrega",
					HoraServicio.class
				);
				query.setParameter("escuela", escuela);
				query.setParameter("tienda", tienda);
				ArrayList<HoraServicioDTO> lista = new ArrayList<>();
				for (HoraServicio hs: query.list())
					lista.add(new HoraServicioDTO(hs));
				return lista;
			},
			"ReadServicio"
		);
	}
	
	public static List<HorarioEscuelaDTO> ReadAllHorario(Tienda tienda)
	{
		if (tienda == null) return null;
		return DoTransaction(s -> {
			List<HoraServicio> horaServicios = s.createQuery(
				"from HoraServicio hs inner join fetch hs.escuela " +
				"where hs.tienda = :tienda " +
				"order by hs.day, hs.horaEntrega",
				HoraServicio.class
			).setParameter("tienda", tienda).list();
			Map<Integer, HorarioEscuelaDTO> map = new HashMap<>();
			for (HoraServicio hs: horaServicios) {
				if (!map.containsKey(hs.getEscuela().getIdEscuela())) map.put(
					hs.getEscuela().getIdEscuela(),
					new HorarioEscuelaDTO(hs.getEscuela().getNombre())
				);
				map.get(hs.getEscuela().getIdEscuela()).Add(hs);
			}
			return new ArrayList<>(map.values());
		}, "ReadServicio");
	}
}
