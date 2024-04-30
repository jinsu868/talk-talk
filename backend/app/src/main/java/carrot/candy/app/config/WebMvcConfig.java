package carrot.candy.app.config;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import carrot.candy.app.common.interceptor.AuthenticationInterceptor;
import carrot.candy.app.common.resolver.AuthResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final AuthResolver authResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders(LOCATION, SET_COOKIE);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .excludePathPatterns("/api/v1/login");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authResolver);
    }
}
