package spring.practice.oct25.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.practice.oct25.securityservices.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class SecurityConfigDev extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public SecurityConfigDev(UserDetailsServiceImpl userDetailsService) {
        System.out.println("loading Dev profiles for SecurityConfig");
        this.userDetailsService = userDetailsService;
    }

    @Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
                    .antMatchers("/journalV3-api/health-check").permitAll()
                    .antMatchers("/journalV3-api/**").authenticated()
                    .antMatchers("/user-api/**").authenticated()
                    .antMatchers("/**/health-check").permitAll()
                    .antMatchers("/admin-api/**").hasRole("ADMIN")
                    .anyRequest().permitAll()
                    .and().httpBasic();
                    //.and().formLogin();
					//.antMatchers("/welcome").hasAnyRole("USER", "ADMIN")
					//.antMatchers("/getEmployees").hasAnyRole("USER", "ADMIN")
					//.antMatchers("/addNewEmployee").hasAnyRole("ADMIN").anyRequest().authenticated()
					//.and().formLogin().loginPage("/login").permitAll()   ///--------------------> this is line to create custom login page
					//.and().logout().permitAll();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable();
			//http.csrf().disable();
		}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        super.configure(auth);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
