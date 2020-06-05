package cacao.friends.shop.modules.item.search;

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
		
		String keyword = webRequest.getParameter("keyword");
		String characterId = webRequest.getParameter("characterId");
		String categoryId = webRequest.getParameter("categoryId");
		String subCategoryId = webRequest.getParameter("subCategoryId");
		String page = webRequest.getParameter("page");
		
		ItemSortProperty sortProperty = getSortProperty(parameter, webRequest);
		ItemStatus itemStatus = getItemStatus(parameter, webRequest);
		
		itemSearchForm.setItemStatus(itemStatus);
		itemSearchForm.setSortProperty(sortProperty);
		itemSearchForm.setCharacterId(numberParse(characterId));
		itemSearchForm.setCategoryId(numberParse(categoryId));
		itemSearchForm.setSubCategoryId(numberParse(subCategoryId));
		itemSearchForm.setPage(getPageNumber(page));
			
		log.info(String.format("sortProperty = %s, keyword = %s, itemStatus = %s, character = %s, category = %s, category = %s,", 
				sortProperty, keyword, itemStatus, characterId, categoryId, subCategoryId));
		
		return itemSearchForm;
	}
	
	protected int getPageNumber(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	protected Long numberParse(String s) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	protected ItemSortProperty getSortProperty(MethodParameter parameter, NativeWebRequest webRequest) {
		ItemSearchDefault itemSearchDefault = parameter.getParameterAnnotation(ItemSearchDefault.class);
		String paramSortProperty = webRequest.getParameter("sortProperty");
		
		if(paramSortProperty == null || "".equals(paramSortProperty)) {
			return itemSearchDefault.sortProperty();
		}
			
		return ItemSortProperty.valueOf(paramSortProperty);
	}
	
	protected ItemStatus getItemStatus(MethodParameter parameter, NativeWebRequest webRequest) {
		ItemSearchDefault itemSearchDefault = parameter.getParameterAnnotation(ItemSearchDefault.class);
		String paramItemStatus = webRequest.getParameter("itemStatus");
		
		if(paramItemStatus == null || "".equals(paramItemStatus)) {
			return itemSearchDefault.itemStatus();
		}
		
		return ItemStatus.valueOf(paramItemStatus);
	}

}
