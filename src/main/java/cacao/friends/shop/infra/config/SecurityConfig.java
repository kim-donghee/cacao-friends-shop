package cacao.friends.shop.infra.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
	
	@Order(1)
	@Configuration
	@RequiredArgsConstructor
	public static class SecurityConfigManager extends WebSecurityConfigurerAdapter {
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/manager/**")
				.authorizeRequests()
					.mvcMatchers("/manager/login").anonymous()
					.mvcMatchers("/manager/**").hasRole("MANAGER")
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.and()
				.formLogin()
					.loginPage("/manager/login")
					.loginProcessingUrl("/manager/login")
					.failureUrl("/manager/login?error")
					.defaultSuccessUrl("/manager", true)
					.and()
				.logout()
					.logoutUrl("/manager/logout")
					.deleteCookies("JSESSIONID")
					.logoutSuccessUrl("/manager")
	                .and()
	            .httpBasic()
					.and()
				.anonymous()
					.principal("anonymouseUser")
					.authorities("anonymouse")
			;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("cacaomanager").password("{noop}manager1234").roles("MANAGER");
		}
		
	}
	
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
					.mvcMatchers("/member/login", "/member/join").anonymous()
					.mvcMatchers("/member/settings/**", "/member/logout", "/cart/**", "/order/**").hasRole("MANAGER")
					.mvcMatchers("/", "/member/**", "/search/**", "/item/**").permitAll()
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.and()
				.formLogin()
					.loginPage("/member/login")
					.loginProcessingUrl("/member/login")
					.failureUrl("/member/login?error")
					.defaultSuccessUrl("/", true)
					.and()
				.logout()
					.logoutUrl("/member/logout")
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
