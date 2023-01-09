package escompras.servlet;

import escompras.model.dao.*;
import escompras.model.dto.HoraServicioDTO;
import escompras.model.dto.ProductoDTO;
import escompras.model.entity.*;
import escompras.util.BaseServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static escompras.model.dto.ProductoConID.ToDTOConID;

@WebServlet(name = "Cliente", value = "/Cliente/*")
public class ClienteServlet extends BaseServlet
{
	@Override
	public void init(ServletConfig ignored)
	{
		ConfigureMapping(
			(req, resp, user) -> SendIndexError(req, resp,
				"Acción \"" +
				req.getParameter("action") +
				"\" no reconocida por el Servlet Cliente."
			),
			(req, resp, user) -> SendIndexError(req, resp,
				"El Servlet Cliente requiere de una acción."
			),
			"hacerPedido",	(HttpAction) this::HacerPedido,
			"guardarPedido",	(HttpAction) this::GuardarPedido,
			"leerPedidoAJAX",	(HttpAction) this::VerProductosEnPedido,
			"costoProducto",	(HttpAction) this::EnviarCostoProducto,
			"favoritos",		(HttpAction) this::Favoritos,
			"like",			(HttpAction) this::Like,
			"verTienda",		(HttpAction) this::VerTienda,
			"comentar",		(HttpAction) this::Comentar
		);
	}
	
	private void VerProductosEnPedido(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws ServletException, IOException
	{
		Orden orden;
		try {
			orden = OrdenDAO.Read(Integer.parseInt(req.getParameter("id")));
		} catch (Exception e) {
			SendIndexError(req, resp, "Error leyendo la orden: " + e);
			return;
		}
		req.setAttribute("productos", CompraDAO.ReadAllDTOFrom(orden));
		req.getRequestDispatcher("/WEB-INF/JSPF/pedido.jsp").forward(req, resp);
	}
	
	private void HacerPedido(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Cliente)) {
			SendIndexError(req, resp, "Solo los clientes pueden comprar.");
			return;
		}
		Cliente cliente = (Cliente) user;
		Escuela escuela = ClienteDAO.ReadConEscuela(cliente.getIdCliente()).getEscuela();
		Producto producto;
		try {
			producto = ProductoDAO.EagerRead(Integer.parseInt(req.getParameter("id")));
		} catch (Exception e) {
			SendIndexError(req, resp,
				"Error leyendo producto " + req.getParameter("id") + ": " + e
			);
			return;
		}
		Tienda tienda = producto.getTienda();
		List<HoraServicioDTO> horarios = HorarioDAO.ReadAllHorario(escuela, tienda);
		if (horarios.isEmpty()) {
			SendIndexError(req, resp,
				tienda.getNombre() + " no hace entregas a " + escuela.getNombre() + "."
			);
			return;
		}
		req.setAttribute("tienda", tienda);
		req.setAttribute("escuela", escuela.getNombre());
		req.setAttribute("horarios", horarios);
		req.setAttribute("productoId", producto.getIdProducto());
		String fecha = LocalDateTime.now().toString().replace("T", " ");
		req.setAttribute("fecha", fecha.substring(0, fecha.lastIndexOf(":")));
		req.setAttribute("listaProductos", ToDTOConID(ProductoDAO.ReadAll(tienda)));
		req.setAttribute("comentarios", ComentarioDAO.ReadAll(tienda));
		req.getRequestDispatcher("/WEB-INF/hacerPedido.jsp").forward(req, resp);
	}
	
	private void EnviarCostoProducto(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException
	{
		try {
			Producto producto = ProductoDAO.Read(Integer.parseInt(req.getParameter("id")));
			int id = producto.getIdProducto();
			Send(resp,
				"<td>" + producto.getNombre() + "</td>" +
				"<td><input type='number' form='pedido' id='cantidad" + id + "' "
				+ "value='0' min='0' class='form-control' name='cantidad" + id + "' "
				+ "onchange='ContarTotal()'/>" +
				"</td>" +
				"<td id='precio" + id + "'>"
				+ producto.getPrecio() / 100f +
				"</td>"
			);
		} catch (Exception e) {
			SendError(resp, "Excepción al leer producto. " + e);
		}
	}
	
	private void GuardarPedido(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException
	{
		if (!(user instanceof Cliente)) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		Cliente cliente = (Cliente) user;
		Tienda tienda;
		try {
			tienda = TiendaDAO.Read(Integer.parseInt(req.getParameter("tienda")));
			if (tienda == null) throw new NullPointerException();
		} catch (Exception e) {
			SendIndexError(req, resp, "Error leyendo la tienda del pedido: " + e);
			return;
		}
		HoraServicio hora;
		try {
			hora = HorarioDAO.ReadHorario(Integer.parseInt(req.getParameter("horario")));
			if (hora == null) throw new NullPointerException();
		} catch (Exception e) {
			SendIndexError(req, resp, "Error leyendo el horario del pedido: " + e);
			return;
		}
		ArrayList<Compra> productos = new ArrayList<>();
		try {
			for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
				String key = entry.getKey(), cantidad = entry.getValue()[0];
				if (!key.startsWith("cantidad")) continue;
				String id = key.substring(8);
				Producto producto = ProductoDAO.EagerRead(Integer.parseInt(id));
				if (!producto.getTienda().equals(tienda)) {
					SendIndexError(req, resp,
						"El producto " + producto.getIdProducto() +
						" no pertenece a la tienda " + tienda.getIdTienda() + '.'
					);
					return;
				}
				Compra compra = new Compra(
					producto,
					Integer.parseInt(cantidad),
					producto.getDescripcion()
				);
				productos.add(compra);
			}
		} catch (Exception e) {
			SendIndexError(req, resp, "Error leyendo productos: " + e);
			return;
		}
		//Guardar
		OrdenDAO.Create(cliente, tienda, productos, hora);
		SendInfoMessage(req, resp, "Pedido registrado.");
	}
	
	private void Favoritos(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Cliente)) {
			SendIndexError(req, resp, "Solo los clientes pueden tener favoritos.");
			return;
		}
		Cliente cliente = ClienteDAO.ReadConLikes((Cliente) user);
		req.setAttribute("productos", ProductoDAO.FillLikes(ProductoDAO.ReadAll(cliente), cliente));
		req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
	}
	
	private void Like(HttpServletRequest req, HttpServletResponse resp, Object user) throws IOException
	{
		if (!(user instanceof Cliente)) {
			SendIndexError(req, resp, "Solo los clientes pueden tener favoritos.");
			return;
		}
		Cliente cliente = ClienteDAO.ReadConLikes((Cliente) user);
		Producto producto = ProductoDAO.Read(Integer.parseInt(req.getParameter("id")));
		if (producto == null) {
			SendError(resp, "Producto no encontrado.");
			return;
		}
		if (req.getParameter("remove") != null)
			cliente.getFavoritos().remove(producto);
		else
			cliente.getFavoritos().add(producto);
		ClienteDAO.Update(cliente);
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	private void VerTienda(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException, ServletException
	{
		Cliente cliente = user instanceof Cliente ? ClienteDAO.ReadConLikes((Cliente) user) : null;
		Tienda tienda = TiendaDAO.Read(req.getParameter("tienda"));
		if (tienda == null) {
			SendIndexError(req, resp,
				"No se puedo encontrar la tienda" + req.getParameter("id") + "."
				
			);
			return;
		}
		req.setAttribute("tienda", tienda);
		req.setAttribute("productos", cliente != null ?
			ProductoDAO.FillLikes(ProductoDTO.ToDTO(ProductoDAO.ReadAll(tienda)), cliente)
			: ProductoDTO.ToDTO(ProductoDAO.ReadAll(tienda))
		);
		req.setAttribute("escuelas", HorarioDAO.ReadAllHorario(tienda));
		req.getRequestDispatcher("/WEB-INF/tienda.jsp").forward(req, resp);
	}
	
	private void Comentar(HttpServletRequest req, HttpServletResponse resp, Object user) throws IOException
	{
		if (!(user instanceof Cliente)) {
			SendError(resp, "Solo los clientes pueden comentar tiendas.");
			return;
		}
		Cliente cliente = (Cliente) user;
		Tienda tienda = TiendaDAO.Read(req.getParameter("id"));
		if (tienda == null) {
			SendError(resp, "Tienda no reconocida.");
			return;
		}
		Comentario comentario = ComentarioDAO.Read(cliente, tienda);
		if (comentario == null)
			ComentarioDAO.Create(new Comentario(cliente, tienda, req.getParameter("comentario")));
		else
			ComentarioDAO.Update(comentario.setComentario(req.getParameter("comentario")));
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}
