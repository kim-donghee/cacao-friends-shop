package cacao.friends.shop.modules.member;

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

import cacao.friends.shop.modules.cart.CartRepository;
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
public class MemberInterceptor implements HandlerInterceptor {
	
	private final CategoryRepository categoryRepository;
	
	private final NotificationRepository notificationRepository;
	
	private final CartRepository cartRepository;
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if(modelAndView == null)
			return;
		if(MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()))
			return;
		if(isRedirectView(modelAndView) || isManagerView(modelAndView))
			return;
		
		// 카테고리 
		List<Category> categoryLsit = categoryRepository.findByParentCategoryIsNull();
		modelAndView.addObject("categoryList", categoryLsit);
		
		// 알림
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof UserMember) {
			Member currentMember = ((UserMember) authentication.getPrincipal()).getMember();
			
			Long notificationNumber = notificationRepository.countByMemberAndChecked(currentMember, false);
			if(notificationNumber > 0) {
				modelAndView.addObject("notificationNumber", notificationNumber);
				modelAndView.addObject("hasNotification", true);
			}
			
			// 카트 알림
			Long cartNumber = cartRepository.countByMember(currentMember);
			if(cartNumber > 0) {
				modelAndView.addObject("cartNumber", cartNumber);
				modelAndView.addObject("hasCart", true);
			}
		}
	}
	
	private boolean isRedirectView(ModelAndView modelAndView) {
		return modelAndView.getViewName().startsWith("redirect:") || modelAndView.getView() instanceof RedirectView;
	}
	
	private boolean isManagerView(ModelAndView modelAndView) {
		return modelAndView.getViewName().startsWith("manager");
	}

}
