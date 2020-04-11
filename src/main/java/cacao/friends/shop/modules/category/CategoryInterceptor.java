package cacao.friends.shop.modules.category;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CategoryInterceptor implements HandlerInterceptor {
	
	private final CategoryRepository categoryRepository;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
			return;
		}
		
		if(!isRedirectView(modelAndView) && isAccountView(modelAndView)) {
			List<Category> categoryLsit = categoryRepository.findByParentCategoryIsNull();
			modelAndView.addObject("categoryList", categoryLsit);
		}
	}
	
	private boolean isRedirectView(ModelAndView modelAndView) {
		return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
	}
	
	private boolean isAccountView(ModelAndView modelAndView) {
		return !(modelAndView.getViewName().startsWith("manager") || modelAndView.getViewName().startsWith("admin"));
	}

}
