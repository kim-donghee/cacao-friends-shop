package cacao.friends.shop.modules.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cacao.friends.shop.modules.account.Account;
import cacao.friends.shop.modules.account.UserAccount;
import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.category.CategoryRepository;
import cacao.friends.shop.modules.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;


/**
 * @author KIM
 * 고객 화면에서 요청을 하면 카테고리와 알림 정보를 담아서 응답한다.
 *
 */
@Component
@RequiredArgsConstructor
public class AccountInterceptor implements HandlerInterceptor {
	
	private final CategoryRepository categoryRepository;
	
	private final NotificationRepository notificationRepository;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null)
			return;
		
		if(MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()))
			return;
		
		if(isRedirectView(modelAndView) || !isAccountView(modelAndView)) 
			return;
		
		List<Category> categoryLsit = categoryRepository.findByParentCategoryIsNull();
		modelAndView.addObject("categoryList", categoryLsit);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !(authentication.getPrincipal() instanceof UserAccount))
			return;
		
		Account currentAccount = ((UserAccount) authentication.getPrincipal()).getAccount();
		
		Long count = notificationRepository.countByAccountAndChecked(currentAccount, false);
		
		if(count > 0) {
			modelAndView.addObject("notificationNumber", count);
			modelAndView.addObject("hasNotification", true);
		}
		
	}
	
	private boolean isRedirectView(ModelAndView modelAndView) {
		return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
	}
	
	private boolean isAccountView(ModelAndView modelAndView) {
		return !(modelAndView.getViewName().startsWith("manager") || modelAndView.getViewName().startsWith("admin"));
	}

}
