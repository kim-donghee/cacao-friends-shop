package cacao.friends.shop.modules.item.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.infra.config.AppProperties;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
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
	
	private final JavaMailSender javaMailSender;
	
	private final AppProperties appProperties;
	
	@EventListener
	public void handlerItemPublishEvent(ItemPublishEvent event) {
		Item item = itemRepository.findById(event.getItem().getId()).get();
		CharacterKind character = item.getCharacter();
		List<Member> members = memberRepository.findByPickCharacter(character);
		
		String host = appProperties.getHost();
		
		members.forEach(a -> {
			if(a.isItemCreatedByEmail()) {
				SimpleMailMessage mailMessage = new SimpleMailMessage();
				mailMessage.setTo(a.getEmail());
				mailMessage.setSubject("새로운 상품이 출시했습니다." + item.getName());
				mailMessage.setText(item.getShortDescript() + " - " + (host));
				javaMailSender.send(mailMessage);
			}
			
			if(a.isItemCreatedByWeb()) {
				notificationService.createNotification(a, "새로운 상품이 출시했습니다." + item.getName(), 
						item.getShortDescript(), "/search/" + item.getId());
			}
		});
	}
}