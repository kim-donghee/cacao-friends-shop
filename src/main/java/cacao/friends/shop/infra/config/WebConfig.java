package cacao.friends.shop.infra.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cacao.friends.shop.modules.item.search.ItemSearchHandlerMethodArgumentResolver;
import cacao.friends.shop.modules.member.interceptor.MemberInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final ItemSearchHandlerMethodArgumentResolver itemSearchHandlerMethodArgumentResolver;
	
	private final ApplicationContext applicationContext;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
				.flatMap(StaticResourceLocation::getPatterns)
				.collect(Collectors.toList());
		staticResourcesPath.add("/node_modules/**");
		
		addMemberInterceptor(registry);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(itemSearchHandlerMethodArgumentResolver);
	}
	
	private void addMemberInterceptor(InterceptorRegistry registry) {
		applicationContext.getBeansOfType(MemberInterceptor.class)
			.forEach((k, v) -> {
				registry.addInterceptor(v);
				log.info(String.format("add member interceptor type : %s", v.getClass().toString()));
			});
	}

}
