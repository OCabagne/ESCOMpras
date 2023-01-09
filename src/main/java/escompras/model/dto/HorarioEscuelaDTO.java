package escompras.model.dto;

import escompras.model.entity.HoraServicio;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class HorarioEscuelaDTO
{
	private final String escuela;
	private final List<HoraServicioDTO> horarios = new ArrayList<>();
	
	public void Add(HoraServicio hs) { horarios.add(new HoraServicioDTO(hs)); }
}
