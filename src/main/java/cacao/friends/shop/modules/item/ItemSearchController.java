package cacao.friends.shop.modules.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class ItemSearchController {
	
	private final ItemRepository itemRepository;
	
	@GetMapping
	public String itemSearchView(Model model) {
		model.addAttribute("itemList", itemRepository.findAll());
		return "account/item/search";
	}

}
