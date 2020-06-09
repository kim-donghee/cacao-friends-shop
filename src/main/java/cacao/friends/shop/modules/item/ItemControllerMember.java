package cacao.friends.shop.modules.item;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cacao.friends.shop.modules.category.CategoryRepository;
import cacao.friends.shop.modules.characterKind.CharacterKindRepository;
import cacao.friends.shop.modules.item.search.ItemCondition;
import cacao.friends.shop.modules.item.search.ItemSearchDefault;
import cacao.friends.shop.modules.item.search.ItemSearchForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemControllerMember {
	
	private final ItemRepository itemRepository;
	
	private final CategoryRepository categoryRepository;
	
	private final CharacterKindRepository characterKindRepository;
	
	@GetMapping("/search")
	public String itemSearchView(@ItemSearchDefault ItemSearchForm itemSearchForm, Model model) {
		ItemCondition condition = new ItemCondition(itemSearchForm);
		model.addAttribute("sortProperty", itemSearchForm.getSortProperty().toString());
		model.addAttribute("characterId", itemSearchForm.getCharacterId());
		model.addAttribute("keyword", itemSearchForm.getKeyword());
		model.addAttribute("itemPage", itemRepository.findByCondition(condition));
		model.addAttribute("characterList", characterKindRepository.findAll());
		return "member/search";
	}
	
	@GetMapping("/items")
	public String itemsView(@ItemSearchDefault ItemSearchForm itemSearchForm, Model model) {
		ItemCondition condition = new ItemCondition(itemSearchForm);
		model.addAttribute("sortProperty", itemSearchForm.getSortProperty());
		model.addAttribute("characterId", itemSearchForm.getCharacterId());
		model.addAttribute("categoryId", itemSearchForm.getCategoryId());
		model.addAttribute("subCategoryId", itemSearchForm.getSubCategoryId());
		model.addAttribute("subCategoryList", categoryRepository.findByParentCategoryId(itemSearchForm.getCategoryId()));
		model.addAttribute("itemPage", itemRepository.findByCondition(condition));
		model.addAttribute("characterList", characterKindRepository.findAll());
		return "member/items";
	}
	
	@GetMapping("/item/{id}")
	public String itemView(@PathVariable Long id, Model model) {
		Item item = itemRepository.findWithBannersById(id);
		model.addAttribute(item);
		return "member/item";
	}
}
