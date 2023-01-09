package escompras.servlet;

import escompras.util.BaseServlet;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "Image", value = "/Imagen/*")
public class ImageServlet extends BaseServlet
{
	@Override
	public void init(ServletConfig ignored)
	{
		ConfigureMapping(this::DefaultBehaviour, this::DefaultBehaviour,
			"cliente", (HttpAction) (req, resp, user) ->
				resp.sendRedirect(GetImage(TipoImagen.CLIENTE, req.getParameter("id"))),
			"tienda", (HttpAction) (req, resp, user) ->
				resp.sendRedirect(GetImage(TipoImagen.TIENDA, req.getParameter("id"))),
			"producto", (HttpAction) (req, resp, user) ->
				resp.sendRedirect(GetImage(TipoImagen.PRODUCTO, req.getParameter("id"))),
			"clienteIcon", (HttpAction) (req, resp, user) ->
				resp.sendRedirect(GetImage(TipoImagen.CLIENTE_ICON, req.getParameter("id"))),
			"tiendaIcon", (HttpAction) (req, resp, user) ->
				resp.sendRedirect(GetImage(TipoImagen.TIENDA_ICON, req.getParameter("id")))
		);
		setSilent(true);
		setNoUser(true);
	}
	
	private String GetImage(TipoImagen tipo, String id)
	{
		int idParsed;
		try {
			idParsed = Integer.parseInt(id);
		} catch (NumberFormatException | NullPointerException e) {
			return "https://res.cloudinary.com/hjcrwl0wp/image/upload/Productos/not_found.jpg";
		}
		if (tipo == TipoImagen.CLIENTE) return ImageURL.getCliente(idParsed);
		if (tipo == TipoImagen.TIENDA) return ImageURL.getTienda(idParsed);
		if (tipo == TipoImagen.PRODUCTO) return ImageURL.getProducto(idParsed);
		if (tipo == TipoImagen.CLIENTE_ICON) return ImageURL.getClienteIcon(idParsed);
		if (tipo == TipoImagen.TIENDA_ICON) return ImageURL.getTiendaIcon(idParsed);
		return "https://res.cloudinary.com/hjcrwl0wp/image/upload/Productos/not_found.jpg";
	}
	
	private void DefaultBehaviour(HttpServletRequest req, HttpServletResponse resp, Object ignored)
		throws IOException
	{
		resp.sendRedirect("https://res.cloudinary.com/hjcrwl0wp/image/upload/Productos/not_found.jpg");
	}
}

enum TipoImagen { CLIENTE, TIENDA, PRODUCTO, CLIENTE_ICON, TIENDA_ICON }

class ImageURL
{
	private static final String baseURL = "https://res.cloudinary.com/hjcrwl0wp/image/upload/";
	
	public static String getCliente(int id)
	{
		return baseURL + "d_Productos:not_found.jpg/Clientes/" + id + getRandom();
	}
	
	public static String getClienteIcon(int id)
	{
		return baseURL + "w_25,h_25,c_fill,r_max/Clientes/" + id + ".png" + getRandom();
	}
	public static String getTienda(int id)
	{
		return baseURL + "d_Productos:not_found.jpg/Tiendas/" + id + getRandom();
	}
	
	public static String getTiendaIcon(int id)
	{
		return baseURL + "w_25,h_25,c_fill,r_max/Tiendas/" + id + getRandom();
	}
	
	public static String getProducto(int id)
	{
		return baseURL + "h_275/d_Productos:not_found.jpg/Productos/" + id + getRandom();
	}
	
	public static String getProductoFullSize(int id)
	{
		return baseURL + "Productos/" + id + getRandom();
	}
	
	private static String getRandom()
	{
		return "?" + Math.abs(new Random().nextInt());
	}
}