package cacao.friends.shop.modules.tag;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.tag.form.TagForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/manager/tag")
@RequiredArgsConstructor
public class TagController {
	
	private final TagRepository tagRepository;
	
	private final TagService tagService;
	
	private String redirectView = "redirect:/manager/tag";
	
	private String validMessage = " 2글자에서 50글자의 이름과 이미지를 반드시 필요합니다.";
	
	private String duplicateMessage = " 캐릭터 이름이 이미 사용중입니다.";
	
	@GetMapping
	public String tagView(Model model) {
		model.addAttribute("tagList", tagRepository.findAll(Sort.by(Order.desc("id"))));
		return "manager/tag/index";
	}
	
	@PostMapping("/add")
	public String createTag(TagForm tagForm, RedirectAttributes attributes) {
		String failMessage = "태그 저장에 실패했습니다.";
		
		if(!tagService.isValid(tagForm)) {
			attributes.addFlashAttribute("error", failMessage + validMessage);
			return redirectView;
		}
		
		if(tagRepository.existsByName(tagForm.getName())) {
			attributes.addFlashAttribute("error", failMessage + duplicateMessage);
			return redirectView;
		}
		
		tagService.createTag(tagForm);
		
		attributes.addFlashAttribute("message", "태그를 저장했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{name}/update")
	public String updateTag(@PathVariable String name, TagForm tagForm, RedirectAttributes attributes) {
		String failMessage = "태그 저장에 실패했습니다.";
		
		if(!tagService.isValid(tagForm)) {
			attributes.addFlashAttribute("error", failMessage + validMessage);
			return redirectView;
		}
		
		Tag tag = tagRepository.findByName(name);
		
		if(tag == null) {
			attributes.addFlashAttribute("error", failMessage);
			return redirectView;
		}
		
		if(tagRepository.existsByName(tagForm.getName())) {
			if(!name.equals(tagForm.getName())) {
				attributes.addFlashAttribute("error", failMessage + duplicateMessage);
				return redirectView;
			}
		}
		
		tagService.updateTag(tag, tagForm);
		
		attributes.addFlashAttribute("message", "태그를 수정했습니다.");
		return redirectView;
	}
	
	@PostMapping("/{name}/remove")
	public String updateTag(@PathVariable String name, RedirectAttributes attributes) {
		Tag tag = tagRepository.findByName(name);
		
		if(tag == null) {
			attributes.addFlashAttribute("error", "태그 삭제에 실패했습니다.");
			return redirectView;
		}
		
		tagService.removeTag(tag);
		
		attributes.addFlashAttribute("message", "태그를 삭제했습니다.");
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
