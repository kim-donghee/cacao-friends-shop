package cacao.friends.shop.modules.item.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.infra.mail.EmailMessage;
import cacao.friends.shop.infra.mail.EmailService;
import cacao.friends.shop.infra.mail.EmailMessageCreator;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.repository.ItemRepository;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.member.MemberRepository;
import cacao.friends.shop.modules.notification.NotificationService;
import lombok.RequiredArgsConstructor;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class ItemEventListener {
	
	private final ItemRepository itemRepository;
	
	private final MemberRepository memberRepository;
	
	private final NotificationService notificationService;
	
	private final EmailService emailService;
	
	private final EmailMessageCreator emailMessageCreator;
	
	@EventListener
	public void handlerItemPublishEvent(ItemPublishEvent event) {
		Item item = itemRepository.findById(event.getItem().getId()).get();
		CharacterKind character = item.getCharacter();
		List<Member> members = memberRepository.findByPickCharacter(character);
		
		members.forEach(m -> {
			if(m.isItemCreatedByWeb()) {
				notificationService.createNotification(m, "새로운 상품이 출시했습니다.", item.getName(), 
						"/item/" + item.getId());
			}
			
			if(m.isItemCreatedByEmail()) {
				String username = m.getUsername();
				String email = m.getEmail();
				String subject = "새로운 상품이 출시했습니다. " + item.getName();
				String link = "/item/" + item.getId();
				String linkName = item.getName();
				String message = "새로운 상품 '" + item.getName() + "' 을 눌러서 확인하세요.";
				
				EmailMessage emailMessage = 
						emailMessageCreator.create(username, email, subject, link, linkName, message);
				emailService.sendEmail(emailMessage);
			}
		});
	}
}
