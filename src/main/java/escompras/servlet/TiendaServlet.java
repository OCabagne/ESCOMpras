package escompras.servlet;

import escompras.model.dao.*;
import escompras.model.dto.HoraServicioDTO;
import escompras.model.dto.ProductoDTO;
import escompras.model.entity.*;
import escompras.util.BaseServlet;

import javax.json.Json;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static escompras.util.ESCOMPrasUtil.GuardarImagenProducto;

@WebServlet(name = "Tienda", value = "/Tienda/*")
@MultipartConfig(maxFileSize = 2 * 1024 * 1024) //2 MiB
public class TiendaServlet extends BaseServlet
{
	@Override
	public void init(ServletConfig ignored)
	{
		ConfigureMapping(
			(req, resp, user) -> SendIndexError(req, resp,
				"Acción \"" +
				req.getParameter("action") +
				"\" no reconocida por el Servlet Tienda."
			),
			(req, resp, user) -> SendIndexError(req, resp,
				"El Servlet Tienda requiere de una acción."
			),
			"horarios",	(HttpAction) this::MostrarHorarios,
			"horariosAJAX",		(HttpAction) this::ActualizarHorarios,
			"listarProductos",	(HttpAction) this::ListarProductos,
			"productosAJAX",	(HttpAction) this::ActualizarProducto,
			"setImagenProducto",	(HttpAction) this::SetImagenProducto,
			"reportar",		(HttpAction) this::Reportar,
			"cargarClave", 		(HttpAction) this::BorrarClavePedido
		);
	}
	
	private void MostrarHorarios(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Tienda)) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		req.setAttribute("escuelas", EscuelaDAO.ReadAllNames());
		req.getRequestDispatcher("/WEB-INF/horarioTienda.jsp").forward(req, resp);
	}
	
	private void ActualizarHorarios(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Tienda)) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		Tienda tienda = (Tienda) user;
		String action = req.getParameter("horarioAction");
		Escuela escuela = EscuelaDAO.ReadByName(req.getParameter("escuela"));
		if (action == null || action.isEmpty()) {
			req.setAttribute("horarios", HorarioDAO.ReadAllHorario(escuela, tienda));
			req.getRequestDispatcher("/WEB-INF/JSPF/horarioTabla.jsp")
				.forward(req, resp);
			return;
		}
		try {
			HoraServicioDTO dto = new HoraServicioDTO(Json.createReader(req.getReader()).readObject());
			if (action.equals("delete")) {
				HorarioDAO.DeleteHorario(new HoraServicio(dto.getId()));
			} else if (action.equals("update")) {
				if (dto.getId() > 0) {
					HorarioDAO.UpdateHorario(dto.ToHoraServicio(escuela, tienda));
					resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return;
				}
				HorarioDAO.CreateHorario(dto.ToHoraServicio(escuela, tienda));
			}
			req.setAttribute("horarios", HorarioDAO.ReadAllHorario(escuela, tienda));
			req.getRequestDispatcher("/WEB-INF/JSPF/horarioTabla.jsp").forward(req, resp);
		} catch (Exception e) {
			SendError(resp, "Excepción inesperada en horario: " + e);
		}
	}
	
	private void ListarProductos(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Tienda)) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		req.getRequestDispatcher("/WEB-INF/productosEnTienda.jsp").forward(req, resp);
	}
	
	private void ActualizarProducto(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (!(user instanceof Tienda)) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		Tienda tienda = (Tienda) user;
		String action = req.getParameter("productoAction");
		if (action == null || action.isEmpty()) {
			req.setAttribute("productos", ProductoDTO.ToDTO(ProductoDAO.ReadAll(tienda)));
			req.getRequestDispatcher("/WEB-INF/JSPF/productosTabla.jsp")
				.forward(req, resp);
			return;
		}
		try {
			ProductoDTO dto = new ProductoDTO(Json.createReader(req.getReader()).readObject());
			if (action.equals("delete")) {
				ProductoDAO.Delete(dto.getId());
			} else if (action.equals("update")) {
				if (dto.getId() > 0) {
					ProductoDAO.Update(dto.ToProducto().setTienda(tienda));
					resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return;
				}
				ProductoDAO.Create(dto.ToProducto().setTienda(tienda));
			}
			req.setAttribute("productos", ProductoDTO.ToDTO(ProductoDAO.ReadAll(tienda)));
			req.getRequestDispatcher("/WEB-INF/JSPF/productosTabla.jsp").forward(req, resp);
		} catch (Exception e) {
			SendError(resp, "Excepción inesperada en producto: " + e);
		}
	}
	
	private void SetImagenProducto(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException
	{
		if (!(user instanceof Tienda)) {
			SendError(resp,"Los clientes no pueden modificar imágenes de productos.");
			return;
		}
		Tienda tienda = (Tienda) user;
		String id = req.getParameter("id");
		if (id == null || id.isEmpty()) {
			SendError(resp, "ID de producto no encontrado.");
			return;
		}
		Producto producto = ProductoDAO.Read(Integer.parseInt(id), tienda);
		if (producto == null) {
			SendError(resp, "El producto no existe o no pertenece a esta tienda.");
			return;
		}
		try {
			if (req.getPart("imagen").getSize() >= 2 * 1024 * 1024) {
				SendError(resp, "La imagen es muy grande.");
				return;
			}
			if (GuardarImagenProducto(req.getPart("imagen"), producto.getIdProducto()) == null) {
				SendError(resp, "No fue posible cargar la imagen. Razones desconocidas.");
				return;
			}
		} catch (Exception e) {
			SendError(resp, "Error al cargar la imagen.\n" + e);
		}
		Send(resp, "Imagen cargada con éxito.");
	}
	
	private void Reportar(HttpServletRequest req, HttpServletResponse resp, Object user) throws IOException
	{
		if (!(user instanceof Tienda)) {
			SendError(resp, "Solo las tiendas pueden reportar clientes.");
			return;
		}
		Tienda tienda = (Tienda) user;
		Cliente cliente = null;
		try {
			cliente = ClienteDAO.Read(Integer.parseInt(req.getParameter("id")));
		} catch (NumberFormatException ignored) {}
		if (cliente == null) {
			SendError(resp, "Cliente no reconocido.");
			return;
		}
		Reporte reporte = ReporteDAO.Read(cliente, tienda);
		if (reporte == null)
			ReporteDAO.Create(new Reporte(cliente, tienda, req.getParameter("reporte")));
		else
			ReporteDAO.Update(reporte.setDescripcion(req.getParameter("reporte")));
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	
	private void BorrarClavePedido(HttpServletRequest req, HttpServletResponse resp, Object user) throws IOException
	{
		String clave = req.getParameter("clavePedido");
		if (clave == null) {
			SendError(resp, "Clave no encontrada.");
			return;
		}
		Orden pedido;
		try {
			pedido = OrdenDAO.Read(Integer.parseInt(req.getParameter("id")));
		} catch (NullPointerException | NumberFormatException e) {
			SendError(resp, "Error de pedido.");
			return;
		}
		ClaveEntrega claveReal = ClaveEntregaDAO.Read(pedido);
		if (!claveReal.getClave().equals(clave)) {
			SendError(resp, "Clave incorrecta.");
			return;
		}
		ClaveEntregaDAO.Delete(pedido);
		resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}
