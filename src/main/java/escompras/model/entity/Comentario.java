package escompras.model.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Comentario implements Serializable
{
	@EmbeddedId
	private ComentarioId idComentario;
	@Column(nullable = false)
	private String comentario;
	
	public Comentario(Cliente idP1, Tienda idP2) { idComentario = new ComentarioId(idP1, idP2); }
	
	public Comentario(Cliente idP1, Tienda idP2, String contenido)
	{
		idComentario = new ComentarioId(idP1, idP2);
		comentario = contenido;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Comentario that = (Comentario) o;
		return idComentario != null && Objects.equals(idComentario, that.idComentario);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(idComentario);
	}
	
	public Cliente getCliente() { return getIdComentario().getCliente(); }
}

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
class ComentarioId implements Serializable
{
	@OneToOne(optional = false) @JoinColumn(nullable = false)
	private Cliente cliente;
	@OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(nullable = false)
	private Tienda tienda;
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		ComentarioId that = (ComentarioId) o;
		return cliente != null && Objects.equals(cliente, that.cliente) &&
		       tienda != null && Objects.equals(tienda, that.tienda);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(cliente, tienda);
	}
}