package cacao.friends.shop.modules.item;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cacao.friends.shop.modules.item.form.ItemSearchForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ItemSearchHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return  ItemSearchForm.class.equals(parameter.getParameterType());
	}

	@Override
	public ItemSearchForm resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		ItemSearchForm itemSearchForm = new ItemSearchForm();
		
		String sortProperty = webRequest.getParameter("sortProperty");
		String keyword = webRequest.getParameter("keyword");
		String itemSatus = webRequest.getParameter("itemSatus");
		String characterId = webRequest.getParameter("characterId");
		String categoryId = webRequest.getParameter("categoryId");
		String subCategoryId = webRequest.getParameter("subCategoryId");
		
		if(sortProperty == null) sortProperty = "NEW";
		if(itemSatus == null) itemSatus = "PUBLISHED";
		
		itemSearchForm.setSortProperty(sortProperty);
		itemSearchForm.setKeyword(keyword);
		itemSearchForm.setItemSatus(itemSatus);
		
		if(characterId != null && !"".equals(characterId))
			itemSearchForm.setCharacterId(Long.parseLong(characterId));
			
		if(categoryId != null && !"".equals(categoryId))
			itemSearchForm.setCategoryId(Long.parseLong(categoryId));
		
		if(subCategoryId != null && !"".equals(subCategoryId))
			itemSearchForm.setSubCategoryId(Long.parseLong(subCategoryId));
		
		log.info(String.format("sortProperty = %s, keyword = %s, itemSatus = %s, character = %s, category = %s, category = %s,", 
				sortProperty, keyword, itemSatus, characterId, categoryId, subCategoryId));
		
		return itemSearchForm;
	}

}
