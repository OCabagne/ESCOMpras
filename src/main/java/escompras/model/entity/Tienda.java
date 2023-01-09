package escompras.model.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Tienda implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idTienda;
	@ManyToOne() @JoinColumn()
	private Tipo tipo;
	@Column(nullable = false, unique = true, length = 30)
	private String nombre;
	@Column(nullable = false, length = 30)
	private String password;
	@Column(nullable = false, unique = true, length = 30)
	private String correo;
	@Column(nullable = false, length = 50)
	private String ubicacion;
	
	public Tienda(int id) { setIdTienda(id); }
	
	public Tienda(String correo, String password)
	{
		setCorreo(correo);
		setPassword(password);
	}
	
	public Tienda setId(int id) { setIdTienda(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o) || !(o instanceof Tienda))
			return false;
		return idTienda == ((Tienda) o).idTienda;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
