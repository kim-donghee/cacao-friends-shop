package cacao.friends.shop.modules.item.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemService;
import cacao.friends.shop.modules.item.form.ItemForm;
import cacao.friends.shop.modules.item.repository.ItemRepository;
import cacao.friends.shop.modules.item.search.ItemCondition;
import cacao.friends.shop.modules.item.search.ItemSearchDefault;
import cacao.friends.shop.modules.item.search.ItemSearchForm;
import cacao.friends.shop.modules.item.search.ItemStatus;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/item")
public class ItemControllerManager {
	
	private final ItemRepository itemRepository;
	
	private final ItemService itemService;
	
	// 상품 목록
	@GetMapping
	public String itemsView(
			@ItemSearchDefault(itemStatus = ItemStatus.ALL) ItemSearchForm itemSearchForm, Model model) {
		ItemCondition condition = new ItemCondition(itemSearchForm);
		model.addAttribute("keyword", itemSearchForm.getKeyword());
		model.addAttribute("itemSatus", itemSearchForm.getItemStatus());
		model.addAttribute("itemPage", itemRepository.findByCondition(condition));
		return "manager/item/list";
	}
	
	// 미공개 전체 삭제
	@PostMapping("/undisclosed/remove")
	public String undisclosedRemove() {
		itemService.undisclosedRemove();
		return "redirect:/manager/item";
	}
	
	// 상품 등록
	@GetMapping("/new")
	public String saveItemForm(Model model) {
		model.addAttribute(new ItemForm());
		return "manager/item/form";
	}
	
	@PostMapping("/new")
	public String saveItem(@Valid ItemForm itemForm, Errors errors, Model model) {
		if(errors.hasErrors()) {
			return "manager/item/form";
		}
		
		Item newItem = itemService.createItem(itemForm);
		return "redirect:/manager/item/" + newItem.getId() + "/banner";
	}
	
}
