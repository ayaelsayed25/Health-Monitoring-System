package Controllers;

import Services.Query;
import Services.Response;
import Services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Deprecated
class MyConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedHeaders("*");
            }
        };
    }

}
@RestController
class Controller {
    UserService userService;
    public Controller() {
        userService = new UserService();
    }

    @CrossOrigin
    @GetMapping("/query")
    public Response getInfo(@RequestParam(value = "startDate")@RequestBody Query query) {
        System.out.println("hi");
        return userService.processQuery(query);
    }

}