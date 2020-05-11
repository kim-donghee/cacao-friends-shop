package cacao.friends.shop.infra.error;

import java.util.Arrays;
import java.util.List;
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
		List<String> pathSplit = Arrays.asList(path.split("/"));
		errorAttributes.put("redirectPath", "/");
		if(pathSplit.size() < 2) 
			return;
		if(pathSplit.get(1).equals("manager")) 
			errorAttributes.put("redirectPath", "/manager");
	}
	
}
