package escompras.model.dao;

import escompras.model.dto.ComentarioDTO;
import escompras.model.entity.Cliente;
import escompras.model.entity.Comentario;
import escompras.model.entity.Tienda;
import escompras.util.BaseDAO;

import java.util.List;
import java.util.stream.Collectors;

public class ComentarioDAO extends BaseDAO
{
	private ComentarioDAO() {}
	
	public static Comentario Create(Comentario comentario)
	{
		return DoTransaction(
			(session) -> { session.save(comentario); return comentario; },
			"CreateComentario"
		);
	}
	
	public static Comentario Read(Cliente idP1, Tienda idP2) { return Read(new Comentario(idP1, idP2)); }
	public static Comentario Read(Comentario comentario)
	{
		return DoTransaction(
			(session) -> session.get(comentario.getClass(), comentario.getIdComentario()),
			"ReadComentario"
		);
	}
	
	public static void Update(Comentario comentario)
	{
		DoTransaction(
			(session) -> { session.update(comentario); return null; },
			"UpdateComentario"
		);
	}
	
	public static void Delete(Cliente idP1, Tienda idP2) { Delete(new Comentario(idP1, idP2)); }
	public static void Delete(Comentario comentario)
	{
		Comentario o = Read(comentario);
		DoTransaction(
			(session) -> { session.delete(o); return null; },
			"DeleteComentario"
		);
	}
	
	public static List<Comentario> ReadAll()
	{
		return DoTransaction(
			(s) -> s.createQuery("from Comentario c", Comentario.class).list(),
			"ReadAllComentario"
		);
	}
	
	public static List<ComentarioDTO> ReadAll(Tienda tienda)
	{
		return DoTransaction((s) -> s.createQuery(
			"from Comentario c where c.id.tienda = :tienda", Comentario.class
			).setParameter("tienda", tienda).list(), "ReadAllComentarioFromTienda"
		).stream().map(ComentarioDTO::new).collect(Collectors.toList());
	}
}
