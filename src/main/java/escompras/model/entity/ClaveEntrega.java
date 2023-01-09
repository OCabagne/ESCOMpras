package escompras.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Accessors(chain = true)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class ClaveEntrega
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idClaveEntrega;
	@ManyToOne(optional = false) @JoinColumn(nullable = false)
	private Orden orden;
	private String clave;
}
