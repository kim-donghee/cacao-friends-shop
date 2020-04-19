package cacao.friends.shop.infra.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
public class SecurityConfig {
	
	@Order(2)
	@Configuration
	@RequiredArgsConstructor
	public static class SecurityConfigUser extends WebSecurityConfigurerAdapter {
		private final UserDetailsService userDetailsService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
					.mvcMatchers("/account/login", "/account/join").anonymous()
					.mvcMatchers("/account/settings/**").authenticated()
					.mvcMatchers("/", "/account/**", "/search/**", "/item/**").permitAll()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.and()
				.formLogin()
					.loginPage("/account/login")
					.loginProcessingUrl("/account/login")
					.failureUrl("/account/login?error")
					.defaultSuccessUrl("/", true)
					.and()
				.logout()
					.logoutUrl("/account/logout")
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/")
	                .and()
				.httpBasic()
					.and()
				.anonymous()
					.principal("anonymouseUser")
					.authorities("anonymouse")
					.and()
				.userDetailsService(userDetailsService);
			;
		}
	}
	
}
