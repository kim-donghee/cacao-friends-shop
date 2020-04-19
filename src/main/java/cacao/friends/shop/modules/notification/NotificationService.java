package cacao.friends.shop.modules.notification;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.account.Account;
import cacao.friends.shop.modules.item.Item;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
	
	private final NotificationRepository notificationRepository;
	
	public void createNotification(Account account, String title, String message, String link) {
		Notification notification = 
				Notification.builder()
				.title(title)
				.message(message)
				.link(link)
				.account(account)
				.createdDateTime(LocalDateTime.now())
				.build();
		notificationRepository.save(notification);
	}
	
	public void read(List<Notification> notifications) {
		notifications.forEach(n -> {
			n.read();
		});
	}
}
