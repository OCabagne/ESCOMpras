package escompras.model.dao;

import escompras.model.dto.UsuarioDTO;
import escompras.model.entity.Cliente;
import escompras.model.entity.Tienda;
import escompras.util.BaseDAO;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

public class UsuarioDAO extends BaseDAO
{
	private UsuarioDAO() {}
	
	public static UsuarioDTO LogIn(String correo, String password)
	{
		if (correo == null || correo.isEmpty() || password == null || password.isEmpty())
			return null;
		return DoTransaction((session) -> {
			Query<Cliente> queryCliente = session.createQuery(
				"from Cliente c where c.correo = :correo and c.password = :pass",
				Cliente.class
			);
			queryCliente.setParameter("correo", correo);
			queryCliente.setParameter("pass", password);
			Cliente cliente;
			try { cliente = queryCliente.getSingleResult();}
			catch (NoResultException ignored) { cliente = null; }
			if (cliente != null) {
				return new UsuarioDTO(
					cliente.getIdCliente(),
					(cliente.getApellidos() != null)?
						cliente.getApellidos() + ", " + cliente.getNombre():
						cliente.getNombre(),
					cliente.getCorreo(),
					true);
			} else {
				Query<Tienda> queryTienda = session.createQuery(
					"from Tienda t where t.correo = :correo and t.password = :pass",
					Tienda.class
				);
				queryTienda.setParameter("correo", correo);
				queryTienda.setParameter("pass", password);
				Tienda tienda;
				try { tienda = queryTienda.getSingleResult(); }
				catch (NoResultException ignored) { return null; }
				if (tienda == null) return null;
				return new UsuarioDTO(
					tienda.getIdTienda(),
					tienda.getNombre(),
					tienda.getCorreo(),
					false
				);
			}
		}, "LogInUsuario");
	}
	
	public static boolean Exists(String correo)
	{
		if (correo == null || correo.isEmpty()) {
			return true;
		}
		return DoTransaction((session) -> {
			Query<Cliente> clienteQuery = session.createQuery(
				"from Cliente c where c.correo = :correo",
				Cliente.class
			);
			clienteQuery.setParameter("correo", correo);
			if (!clienteQuery.list().isEmpty()) return true;
			Query<Tienda> tiendaQuery = session.createQuery(
				"from Tienda t where t.correo = :correo",
				Tienda.class
			);
			tiendaQuery.setParameter("correo", correo);
			return !tiendaQuery.list().isEmpty();
		}, "ExistsUsuario");
	}
}
