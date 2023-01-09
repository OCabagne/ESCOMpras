package escompras.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Clave
{
	@Id
	private String key;
	private String value;
	@Temporal(TemporalType.TIMESTAMP) @Column(nullable = false)
	private Date fecha;
	
	public Clave(String key, String value)
	{
		this.key = key;
		this.value = value;
		this.fecha = Timestamp.from(Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant());
	}
	
	public Clave setFecha()
	{
		this.fecha = Timestamp.from(Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant());
		return this;
	}
}
