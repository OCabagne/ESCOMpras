package escompras.servlet;

import escompras.model.dao.*;
import escompras.model.dto.UsuarioDTO;
import escompras.model.entity.*;
import escompras.util.BaseServlet;
import escompras.util.ESCOMPrasUtil;
import escompras.util.EstadoUtil;

import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

import static escompras.util.ESCOMPrasUtil.*;

@WebServlet(name = "Usuario", value = "/Usuario/*")
@MultipartConfig(maxFileSize = 1024 * 1024) //1 MiB
public class UsuarioServlet extends BaseServlet
{
	public static final int MAX_IDLE_TIME = 30/*min*/ * 60;
	
	@Override
	public void init(ServletConfig ignored)
	{
		ConfigureMapping(
			(req, resp, user) -> SendIndexError(req, resp,
				"Acción \"" +
				req.getParameter("action") +
				"\" no reconocida por el Servlet Usuario."
			),
			this::DatosUsuario,
			"register",	(HttpAction) this::Register,
			"login",		(HttpAction) this::LogIn,
			"logout",		(HttpAction) this::LogOut,
			"cambiarDatos",		(HttpAction) this::DatosUsuario,
			"cambiarPassword",	(HttpAction) this::CambiarPassword,
			"listarPedidos",	(HttpAction) this::listarPedidos,
			"verPedidosJSON",	(HttpAction) this::VerPedidosJSON,
			"verPedido",		(HttpAction) this::VerPedido,
			"cambiarEstadoPedido",	(HttpAction) this::CambiarEstadoPedido,
			"reenviarClave",	(HttpAction) this::ReenviarClave,
			"cambiarImagen",	(HttpAction) this::CambiarImagen
		);
	}
	
	private void DatosUsuario(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException, ServletException
	{
		req.getRequestDispatcher("/WEB-INF/cambiarDatos.jsp").forward(req, resp);
	}
	
	private void Register(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException, ServletException
	{
		for (String param: new String[] {"nombre", "correo", "password", "tipo"}) {
			if (req.getParameter(param) == null || req.getParameter(param).isEmpty()) {
				SendIndexError(req, resp, "Parámetro vacío: " + param + ".");
				return;
			}
		}
		String correo = req.getParameter("correo");
		if (UsuarioDAO.Exists(correo)) {
			SendIndexError(req, resp, "El correo " + correo + " ya se encuentra registrado.");
			return;
		}
		if (req.getParameter("tipo").equals("tienda")) {
			Tipo tipoTienda = TipoDAO.ReadByName(req.getParameter("tipoTienda"));
			if (tipoTienda == null) {
				SendIndexError(req, resp,
					"Error cargando el tipo de tienda \""
					+ req.getParameter("tipoTienda")
					+ "\"."
				);
				return;
			}
			Tienda tienda = TiendaDAO.Create(new Tienda(
				0,
				tipoTienda,
				req.getParameter("nombre"),
				req.getParameter("password"),
				correo,
				req.getParameter("ubicacion")
			));
			System.out.println("Tienda: " + tienda);
			if (tienda == null) {
				SendIndexError(req, resp, "No se pudo crear la tienda.");
				return;
			}
			if (GuardarImagenTienda(req.getPart("imagen"), tienda.getIdTienda()) == null)
				req.getSession().setAttribute("error", "Error al cargar la imagen.");
			SetUser(
				req,
				UsuarioDAO.LogIn(tienda.getCorreo(), tienda.getPassword())
			);
		} else {
			Escuela escuela = EscuelaDAO.ReadByName(req.getParameter("escuela"));
			if (escuela == null) {
				SendIndexError(req, resp,
					"Error cargando la escuela \""
					+ req.getParameter("escuela")
					+ "\"."
				);
				return;
			}
			Cliente cliente = ClienteDAO.Create(new Cliente(
				0,
				req.getParameter("nombre"),
				req.getParameter("apellidos"),
				req.getParameter("password"),
				correo,
				escuela,
				0,
				true,
				null
			));
			if (cliente == null) {
				SendIndexError(req, resp, "No se pudo crear el cliente.");
				return;
			}
			if (GuardarImagenCliente(req.getPart("imagen"), cliente.getIdCliente()) == null)
				req.getSession().setAttribute("error", "Error al cargar la imagen.");
			SetUser(
				req,
				UsuarioDAO.LogIn(cliente.getCorreo(), cliente.getPassword())
			);
		}
		req.getSession().setAttribute("correo", correo);
		String clave = GenerarClave(10);
		ClaveDAO.Create(new Clave(correo, clave));
		req.getSession().setAttribute("correoNoConfirmado", true);
		try {
			EnviarCorreo(correo, "Código de ESCOMpras", clave);
		} catch (MessagingException e) {
			SendError(resp, "Error al enviar correo, reintente después.");
			return;
		}
		resp.sendRedirect(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
	}
	
	private void ReenviarClave(HttpServletRequest req, HttpServletResponse resp, Object user) throws IOException
	{
		if (user == null) {
			SendError(resp, "Error de sesión.");
			return;
		}
		Clave clave;
		if (user instanceof Cliente) {
			clave = ClaveDAO.Read(((Cliente)user).getCorreo());
		} else if (user instanceof Tienda) {
			clave = ClaveDAO.Read(((Tienda)user).getCorreo());
		} else {
			SendError(resp, "Error de sesión.");
			return;
		}
		if (clave.getFecha().after(Timestamp.from(
			Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant()
				.minus(5, ChronoUnit.MINUTES)
		))) {
			SendError(resp, "Envío denegado, espere cinco minutos.");
			return;
		}
		ClaveDAO.Update(clave);
		try {
			EnviarCorreo(clave.getKey(), "Código de ESCOMpras", clave.getValue());
		} catch (MessagingException e) {
			SendError(resp, "Error al enviar correo: " + e);
			return;
		}
		Send(resp, "Reenvío exitoso");
	}
	
	private void LogIn(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException
	{
		UsuarioDTO usuario = UsuarioDAO.LogIn(
			req.getParameter("login_usuario"),
			req.getParameter("login_password")
		);
		if (usuario != null) {
			SetUser(req, usuario);
			resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
		} else {
			SendIndexError(req, resp, "Error de credenciales al iniciar sesión.");
		}
	}
	
	private void LogOut(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException
	{
		req.getSession().invalidate();
		resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
	}
	
	private void CambiarPassword(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException
	{
		UsuarioDTO usuario = UsuarioDAO.LogIn(
			req.getSession().getAttribute("correo").toString(),
			req.getParameter("password")
		);
		if (usuario == null) {
			req.getSession().invalidate();
			SendIndexError(req, resp, "Contraseña incorrecta. Cambio no realizado.");
			return;
		}
		if (user instanceof Tienda) {
			Tienda tienda = (Tienda) user;
			tienda.setPassword(req.getParameter("newPassword"));
			TiendaDAO.Update(tienda);
		} else if (user instanceof Cliente) {
			Cliente cliente = (Cliente) user;
			cliente.setPassword(req.getParameter("newPassword"));
			ClienteDAO.Update(cliente);
		}
		resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
	}
	
	private void CambiarImagen(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException, ServletException
	{
		Cliente cliente = null;
		Tienda tienda = null;
		if (user instanceof Cliente) cliente = (Cliente) user;
		if (user instanceof Tienda) tienda = (Tienda) user;
		if (cliente == null && tienda == null) {
			UsuarioDTO usuario = UsuarioDAO.LogIn(
				req.getParameter("login_usuario"),
				req.getParameter("login_password")
			);
			if (usuario != null) {
				if (usuario.isCliente()) cliente = ClienteDAO.Read(usuario.getId());
				if (!usuario.isCliente()) tienda = TiendaDAO.Read(usuario.getId());
			}
		}
		if (cliente == null && tienda == null) {
			SendIndexError(req, resp, "Necesita iniciar sesión.");
			return;
		}
		if (tienda != null) {
			if (GuardarImagenTienda(req.getPart("imagen"), tienda.getIdTienda()) == null) {
				SendIndexError(req, resp, "Error cargando la imagen.");
				return;
			}
		} else {
			if (GuardarImagenCliente(req.getPart("imagen"), cliente.getIdCliente()) == null) {
				SendIndexError(req, resp, "Error cargando la imagen.");
				return;
			}
		}
		SendInfoMessage(req, resp, "Imagen cambiada con éxito.");
		//resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
	}
	
	private void SetUser(HttpServletRequest req, UsuarioDTO usuario)
	{
		req.getSession().setMaxInactiveInterval(MAX_IDLE_TIME);
		req.getSession().setAttribute("id", usuario.getId());
		req.getSession().setAttribute("usuario", ESCOMPrasUtil.EscapeHTML(usuario.getNombre()));
		req.getSession().setAttribute("correo", usuario.getCorreo());
		req.getSession().setAttribute("tipoUsuario", usuario.isCliente()? "cliente": "tienda");
		IngresoDAO.Create(new Ingreso(req, usuario));
		if (ClaveDAO.Read(usuario.getCorreo()) != null)
			req.getSession().setAttribute("correoNoConfirmado", true);
		System.out.println(
			"\t{\t\tid: " + usuario.getId()
			+ "\tusuario: " + usuario.getNombre()
			+ "\tcorreo: " + usuario.getCorreo()
			+ "\ttipo: " + req.getSession().getAttribute("tipoUsuario")
			+ "\t}"
		);
	}
	
	private void listarPedidos(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (user == null) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		req.getRequestDispatcher("/WEB-INF/listaPedidos.jsp").forward(req, resp);
	}
	
	private void VerPedidosJSON(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (user == null) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		List<Orden> pedidos;
		if (user instanceof Cliente) {
			pedidos = OrdenDAO.ReadAllFrom((Cliente) user);
		} else {
			pedidos = OrdenDAO.ReadAllFrom((Tienda) user);
		}
		req.setAttribute("pedidos", pedidos);
		req.getRequestDispatcher("/WEB-INF/JSON/pedido.jsp").forward(req, resp);
	}
	
	private void VerPedido(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException, ServletException
	{
		if (user == null) {
			SendIndexError(req, resp, "Error de sesión.");
			return;
		}
		Orden pedido;
		try {
			pedido = OrdenDAO.Read(Integer.parseInt(req.getParameter("pedido")));
		} catch (Exception e) {
			SendIndexError(req, resp, "Error de pedido.");
			return;
		}
		if (user instanceof Cliente) {
			Cliente cliente = (Cliente) user;
			if (!pedido.getCliente().equals(cliente)) {
				SendIndexError(req, resp, "Ese pedido no pertenece a este cliente.");
				return;
			}
			req.setAttribute("tienda", pedido.getTienda());
			req.setAttribute("comentarios", ComentarioDAO.ReadAll(pedido.getTienda()));
		} else {
			Tienda tienda = (Tienda) user;
			if (!pedido.getTienda().equals(tienda)) {
				SendIndexError(req, resp, "Ese pedido no pertenece a esta tienda.");
				return;
			}
			req.setAttribute("cliente", pedido.getCliente());
			req.setAttribute("reportes", ReporteDAO.ReadAll(pedido.getCliente()));
		}
		req.setAttribute("pedido", pedido);
		req.setAttribute("productos", CompraDAO.ReadAllFrom(pedido));
		ClaveEntrega claveEntrega = ClaveEntregaDAO.Read(pedido);
		req.setAttribute("pedirClave", claveEntrega != null && user instanceof Tienda);
		req.setAttribute("clavePedido", claveEntrega != null && user instanceof Cliente ?
			claveEntrega.getClave() : ""
		);
		req.getRequestDispatcher("/WEB-INF/verPedido.jsp").forward(req, resp);
	}
	
	private void CambiarEstadoPedido(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws IOException
	{
		Cliente cliente = user instanceof Cliente ? (Cliente) user : null;
		Tienda tienda = user instanceof Tienda ? (Tienda) user : null;
		if (user == null || cliente == null && tienda == null) {
			SendError(resp, "Se requiere iniciar sesión.");
			return;
		}
		boolean isCliente = cliente != null;
		Orden pedido = OrdenDAO.Read(Integer.parseInt(req.getParameter("pedido")));
		if (isCliente && !pedido.getCliente().equals(cliente)) {
			SendError(resp, "El pedido no pertenece a este cliente.");
			return;
		}
		if (!isCliente && !pedido.getTienda().equals(tienda)) {
			SendError(resp, "El pedido no pertenece a esta tienda.");
			return;
		}
		Orden.Estado sigEstado;
		try {
			sigEstado = Orden.Estado.valueOf(req.getParameter("estado").toUpperCase());
		} catch (IllegalArgumentException e) {
			SendError(resp, "El estado indicado no existe.");
			return;
		}
		if (!EstadoUtil.EsSiguiente(pedido.getEstado(), sigEstado, isCliente)) {
			SendError(resp,
				"No es posible para "
				+ (isCliente ? "un cliente" : "una tienda")
				+ " mover un pedido de " + pedido.getEstado()
				+ " a " + sigEstado + "."
			);
			return;
		}
		Send(resp, "Pedido movido de " + pedido.getEstado() + " a " + sigEstado + ".");
		OrdenDAO.Update(pedido.setEstado(sigEstado));
		if (sigEstado == Orden.Estado.ENVIADO) ClaveEntregaDAO.Create(
			new ClaveEntrega().setOrden(pedido).setClave(String.valueOf(new Random().nextInt() % 1000000))
		);
	}
}