package security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CustomConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"*").permitAll()
            .and().addFilter(new CorsFilter())
            .addFilterAfter(
                new AuthenFilter(), CorsFilter.class)
            .addFilterAfter(
                new AuthorFilter(), AuthenFilter.class);
    }
}
