package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Reporte implements Serializable
{
	@EmbeddedId
	@Setter private ReporteId idReporte;
	@Column(nullable = false)
	private String descripcion;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	public Reporte(Cliente idP1, Tienda idP2) { setIdReporte(new ReporteId(idP1, idP2)); }
	
	public Reporte(Cliente idP1, Tienda idP2, String reporte)
	{
		setIdReporte(new ReporteId(idP1, idP2));
		this.descripcion = reporte;
		this.fecha = Timestamp.from(Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant());
	}
	
	public Reporte setDescripcion(String reporte)
	{
		this.descripcion = reporte;
		this.fecha = Timestamp.from(Instant.now().atZone(ZoneId.of("America/Mexico_City")).toInstant());
		return this;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Reporte reporte = (Reporte) o;
		return idReporte != null && Objects.equals(idReporte, reporte.idReporte);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(idReporte);
	}
}

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
class ReporteId implements Serializable
{
	@OneToOne(optional = false) @JoinColumn(nullable = false)
	private Cliente cliente;
	@OneToOne(optional = false) @JoinColumn(nullable = false)
	private Tienda tienda;
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ReporteId reporteId = (ReporteId) o;
		return cliente != null && Objects.equals(cliente, reporteId.cliente) &&
		       tienda != null && Objects.equals(tienda, reporteId.tienda);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(cliente, tienda);
	}
}