package escompras.util;

import java.util.Random;

public class ImageURL
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
