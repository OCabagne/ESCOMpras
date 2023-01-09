package escompras.model.dao;

import escompras.model.entity.Clave;
import escompras.util.BaseDAO;

public class ClaveDAO extends BaseDAO
{
	private ClaveDAO() {}
	
	public static void Create(Clave clave)
	{
		DoTransaction((session) -> session.save(clave), "CreateClave");
	}
	
	public static Clave Read(String correo)
	{
		return DoTransaction((session) -> session.get(Clave.class, correo), "ReadClave");
	}
	
	public static void Update(Clave clave)
	{
		DoTransaction((s) -> { s.update(clave.setFecha()); return null; }, "UpdateClave");
	}
	
	public static void Delete(String correo)
	{
		Clave c = Read(correo);
		DoTransaction( (session) -> { session.delete(c); return null; }, "DeleteClave" );
	}
}
