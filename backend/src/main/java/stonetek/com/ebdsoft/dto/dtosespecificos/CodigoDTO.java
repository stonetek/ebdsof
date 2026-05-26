package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CodigoDTO {
    private String email;
    private String code;

    public CodigoDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }


}

