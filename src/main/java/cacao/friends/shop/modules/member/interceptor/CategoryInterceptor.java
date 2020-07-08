package cacao.friends.shop.modules.member.interceptor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.category.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryInterceptor extends MemberInterceptor {
	
	private final CategoryRepository categoryRepository;
	
	@Override
	protected void postHandleProcess(ModelAndView modelAndView) {
		List<Category> categoryLsit = categoryRepository.findByParentCategoryIsNull();
		modelAndView.addObject("categoryList", categoryLsit);
	}

}
