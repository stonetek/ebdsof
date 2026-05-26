package stonetek.com.ebdsoft.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import stonetek.com.ebdsoft.util.Area;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IgrejaPublicaResponse {
    private Long id;
    
    private String nome;

    private Area area;
}
