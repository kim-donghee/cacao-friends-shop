package cacao.friends.shop.modules.characterKind;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/character")
public class CharacterKindControllerManager {
	
	private final CharacterKindService characterService;
	
	private String redirectView = "redirect:/manager/character";
	
	private String validMessage = " 1글자에서 50글자의 이름과 이미지를 반드시 필요합니다.";
	
	@GetMapping
	public String characterView(Model model) {
		model.addAttribute("characterList", characterService.findAll());
		return "manager/character/index";
	}
	
	@PostMapping("/save")
	public String createCharacterKind(@Valid CharacterForm form, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			attributes.addFlashAttribute("error", "캐릭터 저장에 실패했습니다." + validMessage);
			return redirectView;
		}
		characterService.createCharacter(form);
		attributes.addFlashAttribute("message", "캐릭터를 저장했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{id}/update")
	public String updateCharacterKind(@PathVariable Long id, @Valid CharacterForm form, Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			attributes.addFlashAttribute("error", "캐릭터 수정에 실패했습니다." + validMessage);
			return redirectView;
		}
		characterService.updateCharacter(id, form);
		attributes.addFlashAttribute("message", "캐릭터를 수정했습니다.");
		return redirectView;
	}

	@PostMapping("/{id}/remove")
	public String updateCharacterKind(@PathVariable Long  id, RedirectAttributes attributes) {
		characterService.removeCharacter(id);
		attributes.addFlashAttribute("message", "캐릭터를 삭제했습니다.");
		return redirectView;
	}
	
}
