package escompras.servlet;

import escompras.model.dao.ClaveDAO;
import escompras.model.dao.EscuelaDAO;
import escompras.model.dao.ProductoDAO;
import escompras.model.dao.TipoDAO;
import escompras.model.entity.Clave;
import escompras.model.entity.Cliente;
import escompras.model.entity.Tipo;
import escompras.util.BaseServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("")
public class ESCOMprasServlet extends BaseServlet
{
	@Override
	public void init(ServletConfig ignored)
	{
		ConfigureMapping(
			(req, resp, user) -> {
				req.getSession().setAttribute("error",
					"Acción \"" + req.getParameter("action")
					+ "\" no reconocida por el Servlet ESCOMpras."
				);
				DefaultBehavior(req, resp, user);
			},
			this::DefaultBehavior,
			"registro",	(HttpAction) this::Registro,
			"main",			(HttpAction) this::MainPage,
			"terminosYCondiciones",	(HttpAction) this::TerminosYCondiciones,
			"confirmarClave",	(HttpAction) this::ConfirmarClave
		);
	}
	
	private void DefaultBehavior(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		if (user == null)
			Registro(req, resp, null);
		else
			MainPage(req, resp, user);
	}
	
	private void Registro(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws ServletException, IOException
	{
		req.setAttribute("escuelas", EscuelaDAO.ReadAllNames());
		req.setAttribute("tiposTienda", TipoDAO.ReadAllAsString());
		req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
	}
	
	private void MainPage(HttpServletRequest req, HttpServletResponse resp, Object user)
		throws ServletException, IOException
	{
		Cliente cliente = user instanceof Cliente ? (Cliente) user : null;
		String tipoId = req.getParameter("cat");
		String search = req.getParameter("search");
		if (tipoId != null) {
			try {
				Tipo tipo = TipoDAO.Read(Integer.parseInt(tipoId));
				req.setAttribute("tipo", tipo);
				req.setAttribute("productos",
					ProductoDAO.FillLikes(ProductoDAO.ReadAll(tipo), cliente)
				);
			} catch (NumberFormatException ignored2) {
				req.setAttribute("productos",
					ProductoDAO.FillLikes(ProductoDAO.ReadAll(), cliente)
				);
			}
		} else if (search != null) {
			List<String> keyList = Arrays.stream(
				//search.split("(^\\S{0,2})?\\s+(\\S{0,2}\\s+)*(\\S{0,2}$)?")
				search.split("\\s+")
			).filter(s -> !s.isEmpty()).sorted(Comparator.comparingInt(String::length).reversed())
				.map(String::toLowerCase).collect(Collectors.toList());
			req.setAttribute("search", search);
			req.setAttribute("productos",
				ProductoDAO.FillLikes(ProductoDAO.Search(keyList), cliente)
			);
		} else {
			req.setAttribute("productos", ProductoDAO.FillLikes(ProductoDAO.ReadAll(), cliente));
		}
		req.getRequestDispatcher("/WEB-INF/main.jsp").forward(req, resp);
	}
	
	private void TerminosYCondiciones(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws ServletException, IOException
	{
		req.getRequestDispatcher("/WEB-INF/terminosYCondiciones.jsp").forward(req, resp);
	}
	//AJAX
	private void ConfirmarClave(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException
	{
		Clave clave = ClaveDAO.Read(req.getSession().getAttribute("correo").toString());
		String intento = req.getParameter("clave");
		if (intento == null || !intento.equals(clave.getValue())) {
			Send(resp, "NO");
			return;
		}
		req.getSession().setAttribute("correoNoConfirmado", false);
		ClaveDAO.Delete(req.getSession().getAttribute("correo").toString());
		Send(resp, "OK");
		//String jsstr = req.getReader().lines().collect(Collectors.joining());
	}
}