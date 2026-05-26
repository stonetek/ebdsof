package stonetek.com.ebdsoft.securingweb;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(@SuppressWarnings("null") ViewControllerRegistry registry) {
        registry.addViewController("/igrejas").setViewName("igrejas");
        registry.addViewController("/aulas").setViewName("aulas");
        registry.addViewController("/alunos").setViewName("alunos");
        registry.addViewController("/login").setViewName("login");
    }

}