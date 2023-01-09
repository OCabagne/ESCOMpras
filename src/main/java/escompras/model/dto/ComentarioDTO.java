package escompras.model.dto;

import escompras.model.entity.Comentario;
import lombok.Getter;

@Getter
public class ComentarioDTO
{
	private final String cliente;
	private final String contenido;
	
	public ComentarioDTO(Comentario comentario)
	{
		cliente = comentario.getCliente().getNombre() + " " + comentario.getCliente().getApellidos();
		contenido = comentario.getComentario();
	}
}
