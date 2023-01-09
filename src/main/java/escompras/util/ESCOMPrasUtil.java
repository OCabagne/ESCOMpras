package escompras.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

public class ESCOMPrasUtil
{
	private static Cloudinary getCloudinary()
	{
		return new Cloudinary(ObjectUtils.asMap(
			"cloud_name", "hjcrwl0wp",
			"api_key", "158353527843681",
			"api_secret", "x-mqwHYtK8kj9UCLy979ApEOz3g",
			"secure", "true"
		));
	}
	
	public static String GuardarImagenCliente(Part file, int id)
	{
		return GuardarImagen(file, id, "Clientes");
	}
	public static String GuardarImagenTienda(Part file, int id)
	{
		return GuardarImagen(file, id, "Tiendas");
	}
	public static String GuardarImagenProducto(Part file, int id)
	{
		return GuardarImagen(file, id, "Productos");
	}
	
	private static String GuardarImagen(Part file, int id, String folder)
	{
		if (file == null) return null;
		try {
			return getCloudinary().uploader().uploadLarge(file.getInputStream(), ObjectUtils.asMap(
				"public_id", String.valueOf(id),
				"folder", folder,
				"overwrite", true,
				"resource_type", "image",
				"async", false,
				"invalidate", true
			)).get("url").toString();
		} catch (IOException e) {
			System.out.println("Excepción en Cloudinary: " + e);
			return null;
		}
	}
	
	public static String BorrarImagen(String id)
	{
		try {
			return getCloudinary().uploader().destroy(id, ObjectUtils.asMap(
				"resource_type", "image",
				"invalidate", true
			)).get("result").toString();
		} catch (IOException e) {
			return "Error: " + e;
		}
	}
	
	public static void EnviarCorreo(String destinatario, String asunto, String mensaje)
		throws MessagingException
	{
		//final String CORREO = "AyudaESCOMpras@gmail.com";
		final String CORREO = "correoparapruebasmatr@gmail.com";
		//final String PW = "NJ9sUUb5qDi3Ke!";
		final String PW = "tqhqfnnuweitjljv";
		Properties p = new Properties();
		p.setProperty("mail.smtp.host", "smtp.gmail.com");
		p.setProperty("mail.smtp.starttls.enable", "true");
		p.setProperty("mail.smtp.port", "587");
		p.setProperty("mail.smpt.user", CORREO);
		p.setProperty("mail.smtp.auth", "true");
		Session s = Session.getInstance(p);
		MimeMessage elMensaje = new MimeMessage(s);
		try (Transport t = s.getTransport("smtp")) {
			elMensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			elMensaje.setSubject(asunto);
			elMensaje.setText(mensaje);
			t.connect(CORREO, PW);
			t.sendMessage(elMensaje, elMensaje.getAllRecipients());
		}
	}
	
	public static String GenerarClave(int length)
	{
		return new Random().ints('0', 'z' + 1)
			.filter(i -> !(i > '9' && i < 'A') && !(i > 'Z' && i < 'a'))
			.limit(length)
			.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
			.toString();
	}
	
	public static String EscapeHTML(String string)
	{
		return string.chars().mapToObj(
			c -> c > 127 || c == '"' || c == '\'' || c == '<' || c == '>' || c == '&' ?
				"&#" + c + ';' : "" + (char) c
		).collect(Collectors.joining());
	}
}
