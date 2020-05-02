package cacao.friends.shop.modules.characterKind;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.characterKind.form.CharacterForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manager/character")
@RequiredArgsConstructor
public class CharacterKindController {
	
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
	public String createTag(CharacterForm form, RedirectAttributes attributes) {
		String failMessage = "캐릭터 저장에 실패했습니다.";
		
		if(!characterService.isValid(form)) {
			attributes.addFlashAttribute("error", failMessage + validMessage);
			return redirectView;
		}
		
		characterService.createCharacter(form);
		
		attributes.addFlashAttribute("message", "캐릭터를 저장했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{id}/update")
	public String updateTag(@PathVariable Long id, CharacterForm form, RedirectAttributes attributes) {
		String failMessage = "캐릭터 수정에 실패했습니다.";
		
		if(!characterService.isValid(form)) {
			attributes.addFlashAttribute("error", failMessage + validMessage);
			return redirectView;
		}
		
		CharacterKind character = characterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 캐릭터가 없습니다."));
		
		characterService.updateCharacter(character, form);
		
		attributes.addFlashAttribute("message", "캐릭터를 수정했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{id}/remove")
	public String updateTag(@PathVariable Long  id, RedirectAttributes attributes) {
		CharacterKind character = characterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당하는 캐릭터가 없습니다."));
		
		characterService.removeCharacter(character);
		
		attributes.addFlashAttribute("message", "캐릭터를 삭제했습니다.");
		return redirectView;
	}
	
//	@GetMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<List<Tag>> list() {
//		return ResponseEntity.ok(tagRepository.findAll());
//	}
//	
//	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE,
//			produces = MediaType.APPLICATION_JSON_VALUE)
//	public String tagAdd(@RequestBody @Valid TagForm tagForm, RedirectAttributes attributes) {
//		Tag tag = modelMapper.map(tagForm, Tag.class);
//		
//		tagRepository.save(tag);
//		
//		attributes.addFlashAttribute("message", "태그를 저장했습니다.");
//		return redirectView;
//	}

}
