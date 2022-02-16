package com.salesianos.dam.model.dto.Peticion;

import lombok.*;

import javax.persistence.Id;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetPeticionDto {
    @Id
    private Long idSolicitud;
    private UUID idSeguidor;
    private UUID idSeguido;
}
