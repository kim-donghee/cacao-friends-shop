package cacao.friends.shop.modules.characterKind;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import cacao.friends.shop.modules.characterKind.repository.CharacterKindRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manager/character")
@RequiredArgsConstructor
public class CharacterKindControllerManager {
	
	private final CharacterKindRepository characterRepository;
	
	private final CharacterKindService characterService;
	
	private String redirectView = "redirect:/manager/character";
	
	private String validMessage = " 1글자에서 50글자의 이름과 이미지를 반드시 필요합니다.";
	
	@GetMapping
	public String characterView(Model model) {
		model.addAttribute("characterList", characterRepository.findAll(Sort.by(Order.desc("id"))));
		return "manager/character/index";
	}
	
	@PostMapping("/save")
	public String createTag(@Valid CharacterForm form, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			addRedirectMessage(attributes, "error", "캐릭터 저장에 실패했습니다." + validMessage);
			return redirectView;
		}
		characterService.createCharacter(form);
		addRedirectMessage(attributes, "message", "캐릭터를 저장했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{id}/update")
	public String updateTag(@PathVariable Long id, @Valid CharacterForm form, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			addRedirectMessage(attributes, "error", "캐릭터 수정에 실패했습니다." + validMessage);
			return redirectView;
		}
		
		CharacterKind character = findById(id);
		characterService.updateCharacter(character, form);
		addRedirectMessage(attributes, "message", "캐릭터를 수정했습니다.");
		return redirectView;
	}

	@PostMapping("/{id}/remove")
	public String updateTag(@PathVariable Long  id, RedirectAttributes attributes) {
		CharacterKind character = findById(id);
		characterService.removeCharacter(character);
		addRedirectMessage(attributes, "message", "캐릭터를 삭제했습니다.");
		return redirectView;
	}
	
	private CharacterKind findById(Long id) {
		return characterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 캐릭터가 없습니다."));
	}
	
	private void addRedirectMessage(RedirectAttributes attributes, String messageName, String message) {
		attributes.addFlashAttribute(messageName, message);
	}

}
