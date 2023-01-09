package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Tipo implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTipo;
	@Column(nullable = false)
	private String nombre;
	
	public Tipo(int id) { setIdTipo(id); }
	
	public Tipo setId(int id) { setIdTipo(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		return idTipo == ((Tipo) o).idTipo;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
