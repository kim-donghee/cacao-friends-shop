package cacao.friends.shop.modules.item;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import cacao.friends.shop.modules.category.CategoryRepository;
import cacao.friends.shop.modules.characterKind.CharacterKindRepository;
import cacao.friends.shop.modules.item.form.ItemSearchForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemControllerMember {
	
	private final ItemRepository itemRepository;
	
	private final CategoryRepository categoryRepository;
	
	private final CharacterKindRepository characterKindRepository;
	
	private final ModelMapper modelMapper;
	
	@GetMapping("/search")
	public String itemSearchView(ItemSearchForm itemSearchForm, @RequestParam(defaultValue = "0") int page, Model model) {
		ItemCondition condition = modelMapper.map(itemSearchForm, ItemCondition.class);
		condition.settingItemStatus(itemSearchForm.getItemStatus());
		Pageable pageable = createPageable(itemSearchForm.getSortProperty(), page, 9);
		
		model.addAttribute("sortProperty", itemSearchForm.getSortProperty());
		model.addAttribute("characterId", itemSearchForm.getCharacterId());
		model.addAttribute("keyword", itemSearchForm.getKeyword());
		model.addAttribute("itemPage", itemRepository.findByCondition(condition, pageable));
		model.addAttribute("characterList", characterKindRepository.findAll());
		return "member/search";
	}
	
	@GetMapping("/items")
	public String itemsView(ItemSearchForm itemSearchForm, @RequestParam(defaultValue = "0") int page, Model model) {
		ItemCondition condition = modelMapper.map(itemSearchForm, ItemCondition.class);
		condition.settingItemStatus(itemSearchForm.getItemStatus());
		Pageable pageable = createPageable(itemSearchForm.getSortProperty(), page, 9);
		
		model.addAttribute("sortProperty", itemSearchForm.getSortProperty());
		model.addAttribute("characterId", itemSearchForm.getCharacterId());
		model.addAttribute("categoryId", itemSearchForm.getCategoryId());
		model.addAttribute("subCategoryId", itemSearchForm.getSubCategoryId());
		model.addAttribute("subCategoryList", categoryRepository.findByParentCategoryId(itemSearchForm.getCategoryId()));
		model.addAttribute("itemPage", itemRepository.findByCondition(condition, pageable));
		model.addAttribute("characterList", characterKindRepository.findAll());
		return "member/items";
	}
	
	@GetMapping("/item/{id}")
	public String itemView(@PathVariable Long id, Model model) {
		Item item = itemRepository.findWithBannersById(id);
		model.addAttribute(item);
		return "member/item";
	}
	
	private Pageable createPageable(String sortProperty, int page, int size) {
		Sort sort = Sort.unsorted();
		// NEW, PRICE_ASC, PRICE_DESC
		switch (sortProperty) {
		case "NEW":
			sort = Sort.by(Order.desc("id"));
			break;
		case "PRICE_ASC":
			sort = Sort.by(Order.asc("price"));
			break;
		case "PRICE_DESC":
			sort = Sort.by(Order.desc("price"));
			break;
		}
		return PageRequest.of(page, size, sort);
	}

}
