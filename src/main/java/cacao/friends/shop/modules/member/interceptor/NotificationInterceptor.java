package cacao.friends.shop.modules.member.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.UserMember;
import cacao.friends.shop.modules.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationInterceptor extends MemberInterceptor {
	
	private final NotificationRepository notificationRepository;
	
	@Override
	protected void postHandleProcess(ModelAndView modelAndView) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof UserMember) {
			Member currentMember = ((UserMember) authentication.getPrincipal()).getMember();
			
			Long notificationNumber = notificationRepository.countByMemberAndChecked(currentMember, false);
			if(notificationNumber > 0) {
				modelAndView.addObject("notificationNumber", notificationNumber);
				modelAndView.addObject("hasNotification", true);
			}
		}
	}
}
