package cacao.friends.shop.infra.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final DataSource dataSource;
	
	@Order(2)
	@Configuration
	@RequiredArgsConstructor
	public static class SecurityConfigUser extends WebSecurityConfigurerAdapter {
		
		private final UserDetailsService userDetailsService;
		
		private final PersistentTokenRepository tokenRepository;
		
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
				.rememberMe()
					.userDetailsService(userDetailsService)
					.tokenRepository(tokenRepository)
					.and()
				.userDetailsService(userDetailsService);
			;
		}
	}
	
	@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		return jdbcTokenRepositoryImpl;
	}
	
}
