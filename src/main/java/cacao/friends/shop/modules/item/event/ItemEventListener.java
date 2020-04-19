package cacao.friends.shop.modules.item.event;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.infra.config.AppProperties;
import cacao.friends.shop.modules.account.Account;
import cacao.friends.shop.modules.account.AccountRepository;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Async
@Component
@Transactional
@RequiredArgsConstructor
public class ItemEventListener {
	
	private final ItemRepository itemRepository;
	
	private final AccountRepository accountRepository;
	
	private final JavaMailSender javaMailSender;
	
	private final AppProperties appProperties;
	
	@EventListener
	public void handlerItemPublishEvent(ItemPublishEvent event) {
		Item item = itemRepository.findById(event.getItem().getId()).get();
		CharacterKind character = item.getCharacter();
		List<Account> accountList = accountRepository.findByPickCharacter(character);
		
		String host = appProperties.getHost();
		
		accountList.forEach(a -> {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(a.getEmail());
			mailMessage.setSubject("새로운 상품이 출시했습니다." + item.getName());
			mailMessage.setText(item.getShortDescript() + " - " + (host));
			javaMailSender.send(mailMessage);
			
			javaMailSender.send(mailMessage);
		});
	}
}
