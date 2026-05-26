package stonetek.com.ebdsoft.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import stonetek.com.ebdsoft.dto.request.EbdRequest;
import stonetek.com.ebdsoft.dto.response.EbdResponse;
import stonetek.com.ebdsoft.model.Ebd;

public class EbdMapper {

    private final static ModelMapper mapper = new ModelMapper();
    
	public static EbdResponse converter(Ebd ebd) {
        return mapper.map(ebd, EbdResponse.class);
    }
	
	public static Ebd converter(EbdRequest request) {
        return mapper.map(request, Ebd.class);
    }
	
	public static Ebd converter(EbdResponse response) {
        return mapper.map(response, Ebd.class);
    }
	
	public static List<EbdResponse> converter(List<Ebd> ebds) {
        return ebds.stream().map(EbdMapper::converter).collect(Collectors.toList());
    }
	
	public static void copyToProperties(EbdRequest request, Ebd ebd) {
        mapper.map(request, ebd);
    }

}
