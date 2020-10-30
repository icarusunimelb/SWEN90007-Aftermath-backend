import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import security.CustomConfig;

public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
 
    public SecurityWebApplicationInitializer() {
        super(CustomConfig.class);
    }
}