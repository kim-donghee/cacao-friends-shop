package cacao.friends.shop.infra.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cacao.friends.shop.modules.item.search.ItemSearchHandlerMethodArgumentResolver;
import cacao.friends.shop.modules.member.MemberInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final MemberInterceptor memberInterceptor;
	
	private final ItemSearchHandlerMethodArgumentResolver itemSearchHandlerMethodArgumentResolver;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
				.flatMap(StaticResourceLocation::getPatterns)
				.collect(Collectors.toList());
		staticResourcesPath.add("/node_modules/**");
		
		registry.addInterceptor(memberInterceptor).excludePathPatterns(staticResourcesPath);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(itemSearchHandlerMethodArgumentResolver);
	}

}
