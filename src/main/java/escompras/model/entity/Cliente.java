package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Cliente implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCliente;
	@Column(nullable = false, length = 30)
	private String nombre;
	@Column(nullable = false, length = 30)
	private String apellidos;
	@Column(nullable = false, length = 30)
	private String password;
	@Column(nullable = false, unique = true, length = 30)
	private String correo;
	@ToString.Exclude
	@JoinColumn @ManyToOne(fetch = FetchType.LAZY)
	private Escuela escuela;
	private int calificacion;
	private boolean activo;
	@ToString.Exclude
	@ManyToMany
	private Set<Producto> favoritos = new LinkedHashSet<>();
	
	public Cliente(int id) { setIdCliente(id); }
	
	public Cliente(String nombre, String apellidos, String password, String correo, Escuela escuela)
	{
		setNombre(nombre);
		setApellidos(apellidos);
		setPassword(password);
		setCorreo(correo);
		setEscuela(escuela);
		setCalificacion(0);
		setActivo(true);
	}
	
	public Cliente setId(int id) { setIdCliente(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o) || !(o instanceof Cliente))
			return false;
		return idCliente == ((Cliente) o).idCliente;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
