package cacao.friends.shop.modules.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	
	public void createNotification(Member member, String title, String message, String link) {
		Notification notification = 
				Notification.builder()
				.title(title)
				.message(message)
				.link(link)
				.member(member)
				.createdDateTime(LocalDateTime.now())
				.build();
		notificationRepository.save(notification);
	}
	
	public void read(List<Notification> notifications) {
		notifications.forEach(n -> {
			n.read();
		});
	}
	
	public void oldNotificationRemove(Member member) {
		notificationRepository.deleteByMemberAndChecked(member, true);
	}
	
}
