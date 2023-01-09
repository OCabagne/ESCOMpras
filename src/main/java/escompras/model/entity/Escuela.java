package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Escuela implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idEscuela = 0;
	@Column(nullable = false, unique = true)
	private String nombre;
	@Column(nullable = false)
	private String ubicacion;
	
	public Escuela(int id) { setId(id); }
	
	public Escuela(String name, String location) { setNombre(name);  setUbicacion(location); }
	
	public Escuela setId(int id) { setIdEscuela(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		return idEscuela == ((Escuela) o).idEscuela;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
