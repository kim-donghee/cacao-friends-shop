package cacao.friends.shop.modules.item.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cacao.friends.shop.modules.category.Category;
import cacao.friends.shop.modules.category.CategoryRepository;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import cacao.friends.shop.modules.item.Item;
import cacao.friends.shop.modules.item.ItemBanner;
import cacao.friends.shop.modules.item.ItemCategory;
import cacao.friends.shop.modules.item.ItemService;
import cacao.friends.shop.modules.item.form.ItemForm;
import cacao.friends.shop.modules.item.repository.ItemBannerRepository;
import cacao.friends.shop.modules.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/item")
public class SettingsItemControllerManager {
	
	private final ItemRepository itemRepository;
	
	private final ItemService itemService;
	
	private final ItemBannerRepository itemBannerRepository;
	
	private final CharacterKindRepository characterKindRepository;
	
	private final CategoryRepository categoryRepository;
	
	private final ModelMapper modelMapper;
	
	private final ObjectMapper objectMapper;

	// 상품 정보 수정
	@GetMapping("/{id}/info")
	public String updateItemForm(@PathVariable Long id, Model model) {
		Item item = findById(id);
		model.addAttribute(id);
		model.addAttribute(modelMapper.map(item, ItemForm.class));
		return "manager/item/info";
	}

	@PostMapping("/{id}/info")
	public String updateItem(@PathVariable Long id, @Valid ItemForm itemForm, Errors errors, Model model,
			RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			model.addAttribute(id);
			return "manager/item/info";
		}
		Item item = findById(id);
		itemService.updateItem(item, itemForm);
		attributes.addFlashAttribute("message", "상품 정보를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/info";
	}

	// 상품 배너 등록
	@GetMapping("/{id}/banner")
	public String updateBannerForm(@PathVariable Long id, Model model) {
		Item item = itemRepository.findWithBannersById(id);
		model.addAttribute(id);
		model.addAttribute(item);
		return "manager/item/banner";
	}

	@PostMapping("/{id}/banner")
	public String addBanner(@PathVariable Long id, String image, Model model, RedirectAttributes attributes) {
		Item item = itemRepository.findWithBannersById(id);
		itemService.addBanner(item, image);
		attributes.addFlashAttribute("message", "상품 배너를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/banner";
	}

	// 상품 배너 삭제
	@PostMapping("/{id}/banner/remove")
	public String removeBanner(@PathVariable Long id, Long bannerId, Model model, RedirectAttributes attributes) {
		Item item = itemRepository.findWithBannersById(id);
		ItemBanner banner = itemBannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("해당하는 배너가 없습니다."));
		itemService.removeBanner(item, banner);
		attributes.addFlashAttribute("message", "상품 배너를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/banner";
	}

	// 상품 메인 배너
	@PostMapping("/{id}/banner/main-banner-image")
	public String updateMainBanner(@PathVariable Long id, Long bannerId, Model model, RedirectAttributes attributes) {
		Item item = itemRepository.findWithBannersById(id);
		ItemBanner banner = itemBannerRepository.findById(bannerId)
				.orElseThrow(() -> new IllegalArgumentException("해당하는 배너가 없습니다."));
		itemService.updateMainBanner(item, banner);
		attributes.addFlashAttribute("message", "상품 메인 배너를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/banner";
	}

	// 상품 캐릭터 수정
	@GetMapping("/{id}/character")
	public String updateCharacterForm(@PathVariable Long id, Model model) {
		Item item = findById(id);
		List<CharacterKind> characterList = characterKindRepository.findAll();
		model.addAttribute(id);
		model.addAttribute(item);
		model.addAttribute("characterList", characterList);
		return "manager/item/character";
	}

	@PostMapping("/{id}/character")
	public String updateCharacter(@PathVariable Long id, Long characterId, Model model, RedirectAttributes attributes) {
		Item item = findById(id);
		CharacterKind character = characterKindRepository.findById(characterId)
				.orElseThrow(() -> new IllegalArgumentException("해당하는 캐릭터가 없습니다."));
		itemService.updateCharacter(item, character);
		attributes.addFlashAttribute("message", "상품의 캐릭터 종류를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/character";
	}

	// 상품 카테고리 수정
	@GetMapping("/{id}/category")
	public String updateCategoryForm(@PathVariable Long id, Model model) throws JsonProcessingException {
		Item item = itemRepository.findWithCategoriesById(id);
		List<Category> categoryList = categoryRepository.findAllWithChildBy();
		List<Long> currentCategoryList = item.getItemCategories().stream().map(ItemCategory::getCategory)
				.map(Category::getId).collect(Collectors.toList());
		model.addAttribute(id);
		model.addAttribute(item);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("currentCategoryList", objectMapper.writeValueAsString(currentCategoryList));
		return "manager/item/category";
	}

	@PostMapping("/{id}/category")
	public String updateCategory(@PathVariable Long id, @RequestParam(required = false) List<Long> categorys,
			Model model, RedirectAttributes attributes) {
		if (categorys == null || categorys.isEmpty()) {
			attributes.addFlashAttribute("error", "상품의 카테고리를 선택하세요.");
			return "redirect:/manager/item/" + id + "/category";
		}

		Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
		List<Category> categoryList = categoryRepository.findAllById(categorys);
		itemService.updateCategorys(item, categoryList);
		attributes.addFlashAttribute("message", "상품의 카테고리를 수정했습니다.");
		return "redirect:/manager/item/" + id + "/category";
	}

	// 상품 상태 수정
	@GetMapping("/{id}/item")
	public String updateStatusForm(@PathVariable Long id, Model model) {
		Item item = findById(id);
		model.addAttribute(id);
		model.addAttribute(item);
		return "manager/item/item";
	}

	@PostMapping("/{id}/publish")
	public String publishItem(@PathVariable Long id, RedirectAttributes attributes) {
		Item item = findById(id);
		itemService.publish(item);
		attributes.addFlashAttribute("message", "상품을 공개했습니다.");
		return "redirect:/manager/item/" + id + "/item";
	}

	@PostMapping("/{id}/close")
	public String closeItem(@PathVariable Long id, RedirectAttributes attributes) {
		Item item = findById(id);
		itemService.close(item);
		attributes.addFlashAttribute("message", "상품을 판매 종료했습니다.");
		return "redirect:/manager/item/" + id + "/item";
	}

	@PostMapping("/{id}/pause")
	public String pauseItem(@PathVariable Long id, RedirectAttributes attributes) {
		Item item = findById(id);
		itemService.pause(item);
		attributes.addFlashAttribute("message", "상품을 판매 정지했습니다.");
		return "redirect:/manager/item/" + id + "/item";
	}

	@PostMapping("/{id}/resume")
	public String resumeItem(@PathVariable Long id, RedirectAttributes attributes) {
		Item item = findById(id);
		itemService.resum(item);
		attributes.addFlashAttribute("message", "상품을 다시 공개했습니다.");
		return "redirect:/manager/item/" + id + "/item";
	}

	private Item findById(Long id) {
		return itemRepository.findById(id).orElseThrow(() 
				-> new IllegalArgumentException("해당하는 상품이 없습니다."));
	}

}
