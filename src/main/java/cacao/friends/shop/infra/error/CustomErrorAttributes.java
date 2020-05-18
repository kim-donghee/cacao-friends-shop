package cacao.friends.shop.infra.error;

import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
	
	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		log.info("CustomErrorAttributes.getErrorAttributes");
		Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
		addRedirectPath(errorAttributes);
		return errorAttributes;
	}
	
	private void addRedirectPath(Map<String, Object> errorAttributes) {
		String path = (String) errorAttributes.get("path");
		if(path.startsWith("/manager"))
			errorAttributes.put("redirectPath", "/manager");
		else 
			errorAttributes.put("redirectPath", "/");
	}
	
}
