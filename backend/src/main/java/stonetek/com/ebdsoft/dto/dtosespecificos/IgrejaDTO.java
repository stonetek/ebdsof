package stonetek.com.ebdsoft.dto.dtosespecificos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaDTO {

    private Long id;

    private String nome;

	private String endereco;

	private String complemento;

	private Long cnpj;
	
	private Long cep;

	private String bairro;

	private String cidade;
}
