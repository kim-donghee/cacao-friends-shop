package cacao.friends.shop.modules.member.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import cacao.friends.shop.modules.cart.CartRepository;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.UserMember;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CartInterceptor extends MemberInterceptor {
	
	private final CartRepository cartRepository;

	@Override
	protected void postHandleProcess(ModelAndView modelAndView) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof UserMember) {
			Member currentMember = ((UserMember) authentication.getPrincipal()).getMember();
			
			Long cartNumber = cartRepository.countCartItem(currentMember);
			if(cartNumber > 0) {
				modelAndView.addObject("cartNumber", cartNumber);
				modelAndView.addObject("hasCart", true);
			}
		}
	}
}
