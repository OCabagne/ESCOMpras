package escompras.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Orden implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idOrden;
	@ManyToOne(optional = false) @JoinColumn(nullable = false)
	private Cliente cliente;
	@ManyToOne(optional = false) @JoinColumn(nullable = false)
	private Tienda tienda;
	@ManyToOne(optional = false) @JoinColumn(nullable = false)
	private Escuela escuela;
	@Column(nullable = false)
	private Date fecha;
	private int montoTotal;
	@Enumerated(EnumType.STRING) @Column(nullable = false)
	private Estado estado;
	@ManyToOne(optional = false) @JoinColumn(nullable = false)
	private HoraServicio horaEntrega;
	
	public Orden(int id) { setId(id); }
	
	public Orden(Cliente cliente, Tienda tienda) {
		setCliente(cliente);
		setTienda(tienda);
		setEscuela(cliente.getEscuela());
		setFecha(Date.valueOf(LocalDate.now()));
		setEstado(Estado.SOLICITADO);
	}
	
	public Orden setId(int id) { idOrden = id; return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		if (o instanceof Orden) return idOrden == ((Orden) o).idOrden;
		return false;
	}
	
	public Estado getEstado()
	{
		return estado != null ? estado : Estado.RECHAZADO;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
	
	public enum Estado {
		SOLICITADO, APROBADO, ENVIADO, RECIBIDO, RECHAZADO,
		CANCELADO
	}
}
