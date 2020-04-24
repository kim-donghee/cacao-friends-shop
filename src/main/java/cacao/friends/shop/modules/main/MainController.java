package cacao.friends.shop.modules.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String home() {
		return "member/index";
	}
	
	@GetMapping("/manager")
	public String managerHome() {
		return "manager/index";
	}

}
