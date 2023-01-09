package escompras.util;

import escompras.model.dao.ClienteDAO;
import escompras.model.dao.TiendaDAO;
import lombok.Setter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet
{
	@Setter private boolean silent = false;
	@Setter private boolean noUser = false;
	private Map<String, HttpAction> map;
	private HttpAction noMap;
	private HttpAction noAction;
	protected interface HttpAction
	{ void Do(HttpServletRequest req, HttpServletResponse resp, Object user) throws ServletException, IOException;}
	
	@Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) { doPost(req, resp); }
	@Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	{
		try {
			req.setCharacterEncoding("UTF-8");
			Fill(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void ConfigureMapping(HttpAction noMap, HttpAction noAction, Object... map)
	{
		this.noMap = noMap;
		this.noAction = noAction;
		Map<String, HttpAction> auxMap = new HashMap<>(map.length / 2);
		for (int i = 0; i < map.length; i += 2) auxMap.put((String) map[i], (HttpAction) map[i + 1]);
		this.map = auxMap;
	}
	
	private void Fill(HttpServletRequest req, HttpServletResponse resp)
		throws IOException
	{
		try {
			String action;
			if (req.getParameter("action") != null) action = req.getParameter("action");
			else if (req.getPathInfo() != null) action = req.getPathInfo().replace("/", "");
			else action = null;
			if (!silent) System.out.println(
				"Servlet: " + req.getServletPath() +
				(action != null && !action.isEmpty() ? ", action: " + action : "")
			);
			if (action != null && !action.isEmpty())
				map.getOrDefault(action, noMap).Do(req, resp, noUser ? null : GetUser(req));
			else noAction.Do(req, resp, GetUser(req));
		} catch (Exception e) {
			SendIndexError(req, resp, "Excepción: " + e.getMessage());
		}
	}
	
	private Object GetUser(HttpServletRequest req)
	{
		Integer id = (Integer) req.getSession().getAttribute("id");
		if (id == null) return null;
		else return "tienda".equals(req.getSession().getAttribute("tipoUsuario")) ?
			TiendaDAO.Read(id) : ClienteDAO.Read(id);
	}
	
	protected void SendInfoMessage(HttpServletRequest req, HttpServletResponse resp, String message)
		throws IOException
	{
		req.getSession().setAttribute("infoMessage", message);
		resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
	}
	
	protected void SendIndexError(HttpServletRequest req, HttpServletResponse resp, String error)
		throws IOException
	{
		req.getSession().setAttribute("error", error);
		resp.sendRedirect(req.getContextPath().isEmpty()? "/" : req.getContextPath());
	}
	
	protected void SendError(HttpServletResponse resp, String error) throws IOException
	{
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Send(resp, error);
	}
	
	protected void Send(HttpServletResponse resp, String msg) throws IOException
	{
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println(msg);
	}
}
