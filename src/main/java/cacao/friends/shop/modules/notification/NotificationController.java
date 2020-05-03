package cacao.friends.shop.modules.notification;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
	
	private final NotificationRepository notificationRepository;
	
	private final NotificationService notificationService;
	
	@GetMapping
	public String notificationView(@CurrentMember Member member, Model model) {
		List<Notification> notifications =
				notificationRepository.findByMemberAndCheckedOrderByCreatedDateTimeDesc(member, false);
		long oldNotificationCount = notificationRepository.countByMemberAndChecked(member, true);
		model.addAttribute("isNew", true);
		model.addAttribute("notifications", notifications);
		model.addAttribute("newNotificationCount", notifications.size());
		model.addAttribute("oldNotificationCount", oldNotificationCount);
		notificationService.read(notifications);
		return "member/notification";
	}

	@GetMapping("/old")
	public String oldNotificationView(@CurrentMember Member member, Model model) {
		List<Notification> notifications =
				notificationRepository.findByMemberAndCheckedOrderByCreatedDateTimeDesc(member, true);
		long newNotificationCount = notificationRepository.countByMemberAndChecked(member, false);
		model.addAttribute("isNew", false);
		model.addAttribute("notifications", notifications);
		model.addAttribute("oldNotificationCount", notifications.size());
		model.addAttribute("newNotificationCount", newNotificationCount);
		return "member/notification";
	}
	
	@PostMapping("/remove/old")
	public String deleteNotification(@CurrentMember Member member, Model model) {
		notificationService.oldNotificationRemove(member);		
		return "redirect:/notifications";
	}
	
}
