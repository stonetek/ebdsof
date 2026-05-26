package stonetek.com.ebdsoft.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.request.IgrejaRequest;
import stonetek.com.ebdsoft.dto.response.IgrejaPublicaResponse;
import stonetek.com.ebdsoft.dto.response.IgrejaResponse;
import stonetek.com.ebdsoft.model.Igreja;


public class IgrejaMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static IgrejaResponse converter(Igreja igreja) {
        return mapper.map(igreja, IgrejaResponse.class);
    }
	
	public static Igreja converter(IgrejaRequest request) {
        return mapper.map(request, Igreja.class);
    }
	
	public static Igreja converter(IgrejaResponse response) {
        return mapper.map(response, Igreja.class);
    }
	
	public static List<IgrejaResponse> converter(List<Igreja> igrejas) {
        return igrejas.stream().map(IgrejaMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(IgrejaRequest request, Igreja igreja) {
        mapper.map(request, igreja);
    }

    public static IgrejaPublicaResponse converterPublico(Igreja igreja) {
        return new IgrejaPublicaResponse(igreja.getId(), igreja.getNome(), igreja.getArea());
    }

    public static List<IgrejaPublicaResponse> converterPublico(List<Igreja> igrejas) {
        return igrejas.stream()
                .map(IgrejaMapper::converterPublico)
                .collect(Collectors.toList());
    }
    
}