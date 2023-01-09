package escompras.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
public class Compra implements Serializable
{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCompra;
	private int cantidad;
	private String detalles;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(nullable = false)
	private Orden orden;
	@ManyToOne() @JoinColumn()
	private Producto producto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Tienda tienda;
	@Column(nullable = false)
	private String nombreProducto;
	private int precioProducto;
	private String descripcionProducto;
	@Column(nullable = false)
	private String promocion;
	@Column(nullable = false)
	private String unidad;
	
	public Compra(Producto producto, int cantidad, String detalles)
	{
		setCantidad(cantidad);
		setDetalles(detalles);
		setProducto(producto);
		setTienda(producto.getTienda());
		setNombreProducto(producto.getNombre());
		setPrecioProducto(producto.getPrecio());
		setDescripcionProducto(producto.getDescripcion());
		setPromocion(producto.getPromocion());
		setUnidad(producto.getUnidad());
	}
	
	public Compra(int id) { setIdCompra(id); }
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		if (!(o instanceof Compra)) return false;
		Compra compra = (Compra) o;
		return getIdCompra() == compra.getIdCompra();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(idCompra);
	}
}