import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import security.CustomCofig;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
 
    public SecurityWebApplicationInitializer() {
        super(CustomCofig.class);
    }
}