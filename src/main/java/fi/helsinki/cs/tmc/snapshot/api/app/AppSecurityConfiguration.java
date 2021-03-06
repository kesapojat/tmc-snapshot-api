package fi.helsinki.cs.tmc.snapshot.api.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity
public class AppSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${security.basic.realm}")
    private String basicRealm;

    @Value("${security.basic.path}")
    private String basicPath;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity security) throws Exception {

        security.antMatcher(basicPath)
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .httpBasic()
                    .realmName(basicRealm);
    }

    @Override
    public void configure(final AuthenticationManagerBuilder managerBuilder) throws Exception {

        managerBuilder.userDetailsService(userDetailsService)
                      .passwordEncoder(new BCryptPasswordEncoder());
    }
}
