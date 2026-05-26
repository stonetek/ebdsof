package stonetek.com.ebdsoft.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class IgrejaResponse {

    private Long id;

    private String nome;

	private String endereco;

	private String complemento;

	private Long cnpj;
	
	private Long cep;

	private String bairro;

	private String cidade;

	private String area;
    
}
