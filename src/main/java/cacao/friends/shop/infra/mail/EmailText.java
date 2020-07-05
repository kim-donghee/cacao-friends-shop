package cacao.friends.shop.infra.mail;

import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import cacao.friends.shop.infra.config.AppProperties;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailText {
	
	private final TemplateEngine templateEngine;
	
	private final AppProperties appProperties;

	public String create(String username, String link, String linkName, String message) {
		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("link", link);
		context.setVariable("linkName", linkName);
		context.setVariable("message", message);
		context.setVariable("host", appProperties.getHost());
		return templateEngine.process("member/mail/simple-link", context);	
	}

}
