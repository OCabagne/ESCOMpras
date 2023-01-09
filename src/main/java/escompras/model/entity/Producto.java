package escompras.model.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Producto implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idProducto;
	@ToString.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	private Tienda tienda;
	@Column(length = 30, nullable = false) private String nombre;
	private int precio;
	@Column(length = 50) private String descripcion;
	private int cantidad;
	@Column(length = 50, nullable = false) private String promocion;
	@Column(length = 15, nullable = false) private String unidad;
	
	public Producto(int id) { setIdProducto(id); }
	
	public Producto setId(int id) { setIdProducto(id); return this; }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		return idProducto == ((Producto) o).idProducto;
	}
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode();
	}
}
