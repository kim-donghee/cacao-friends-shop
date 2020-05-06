package cacao.friends.shop.modules.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import cacao.friends.shop.modules.item.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final ItemRepository itemRepository;
	
	@GetMapping("/")
	public String home() {
		
//		itemRepository.find
		
		return "member/index";
	}
	
	@GetMapping("/manager")
	public String managerHome() {
		return "manager/index";
	}
	
	@GetMapping("/manager/login")
	public String login() {
		return "manager/login";
	}

}
