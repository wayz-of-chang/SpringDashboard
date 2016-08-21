package urim.config;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import urim.server.service.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService detailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/", "/csrf", "/ping", "/system", "/script", "/start", "/stop", "/test", "/pong", "/mocha", "/fonts/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users/create", "/users/login").permitAll()
                .antMatchers(HttpMethod.POST, "/users/get", "/users/update_settings", "/users/export", "/users/import").hasAnyAuthority("R_USER","RW_USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/dashboards/get").hasAnyAuthority("R_USER","RW_USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/dashboards/create", "/dashboards/copy", "/dashboards/edit", "/dashboards/delete").hasAnyAuthority("RW_USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/monitors/get").hasAnyAuthority("R_USER","RW_USER","ADMIN")
                .antMatchers(HttpMethod.POST, "/monitors/create", "/monitors/copy", "/monitors/update_settings", "/monitors/delete").hasAnyAuthority("RW_USER","ADMIN")
                .anyRequest().fullyAuthenticated()
                .and()
                .logout()
                .permitAll();

        http.addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("R_USER");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(detailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
