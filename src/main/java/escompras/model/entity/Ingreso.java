package escompras.model.entity;

import escompras.model.dto.UsuarioDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor
@Getter @Setter
@ToString
@Entity
public class Ingreso implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String ip;
	private boolean isCliente;
	private int idUsuario;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	private String browserInfo;
	private String device;
	private String browser;
	
	public Ingreso(HttpServletRequest req, UsuarioDTO user)
	{
		this.ip = req.getRemoteAddr();
		this.isCliente = user.isCliente();
		this.idUsuario = user.getId();
		this.fecha = Timestamp.from(Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant());
		ReadHeader(req.getHeader("User-Agent"));
	}
	
	public Ingreso setId(int id)
	{
		this.id = id;
		return this;
	}
	
	private void ReadHeader(String header)
	{
		if (!header.startsWith("Mozilla/5.0")) {
			this.browserInfo = header;
			return;
		}
		header = header.substring(12);
		
		if (!header.startsWith("(") || !header.contains(")")) {
			this.browserInfo = header;
			return;
		}
		this.device = header.substring(1, header.indexOf(')'));
		header = header.substring(header.indexOf(')') + 2);
		
		if (!header.contains("/") || !header.contains(" ")) {
			this.browser = "???";
			return;
		}
		String version = header.substring(header.lastIndexOf("/") + 1);
		if (header.contains("OPR/")) this.browser = "Opera " + version;
		else if (header.contains("Edg/")) this.browser = "Edge " + version;
		else if (header.contains("Firefox/")) this.browser = "Firefox " + version;
		else if (header.contains("Chrome/"))
			this.browser = header.substring(
				header.lastIndexOf("Chrome"),
				header.indexOf(" ", header.lastIndexOf("Chrome"))
			);
		else
			this.browser = header.substring(header.lastIndexOf(" ") + 1)
				.replace('/', ' ');
		this.browserInfo = null;
	}
}
