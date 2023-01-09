package escompras.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioDTO
{
	private int id;
	private String nombre;
	private String correo;
	private boolean isCliente;
}
