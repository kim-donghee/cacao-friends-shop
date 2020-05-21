package cacao.friends.shop.modules.question;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cacao.friends.shop.modules.member.CurrentMember;
import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.question.form.QuestionForm;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionControllerMember {
	
	private final QuestionRepository questionRepository;
	
	private final QuestionService questionService;
	
	private final ModelMapper modelMapper;
	
	@GetMapping
	public String questionsView(
			@PageableDefault(size = 20, page = 0, direction = Direction.DESC, sort = "createdDateAt") Pageable pageable,
			@RequestParam(defaultValue = "") String title, @CurrentMember Member member, Model model) {
		model.addAttribute("currentMember", member);
		model.addAttribute("title", title);
		model.addAttribute("questionPage", questionRepository.findWithQuestionerAndAnswerByTitleContaining(title, pageable));
		return "member/question/list";
	}
	
	@GetMapping("/{id}")
	public String questionView(@PathVariable Long id, Model model) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		model.addAttribute("question", question);
		return "member/question/question";
	}
	
	@GetMapping("/me")
	public String questionMe(@CurrentMember Member member, Model model) {
		model.addAttribute("questionList",
				questionRepository.findWithQuestionerAndAnswerByQuestionerOrderByCreatedDateAtDesc(member));
		return "member/question/me";
	}
	
	@PostMapping("/remove/me/{id}")
	public String removeMe(@CurrentMember Member member, @PathVariable Long id, RedirectAttributes attributes) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		questionService.removeQuestion(member, question);
		attributes.addFlashAttribute("message", "질문을 삭제했습니다.");
		return "redirect:/question/me";
	}
	
	@GetMapping("/new")
	public String questionNewView(Model model) {
		model.addAttribute(new QuestionForm());
		return "member/question/form";
	}
	
	@PostMapping("/new")
	public String questionNew(@CurrentMember Member member, @Valid QuestionForm form, Errors errors, 
			RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "member/question/form";
		}
		questionService.createQuestion(member, form);
		attributes.addFlashAttribute("message", "질문을 작성했습니다. 응답을 기다려주세요.");
		return "redirect:/question";
	}
	
	@GetMapping("/edit/{id}")
	public String questionEditView(@PathVariable Long id, Model model) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		model.addAttribute("id", id);
		model.addAttribute(modelMapper.map(question, QuestionForm.class));
		return "member/question/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String questionEdit(@CurrentMember Member member, @PathVariable Long id, @Valid QuestionForm form, 
			Errors errors, RedirectAttributes attributes) {
		if(errors.hasErrors()) {
			return "member/question/edit";
		}
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		questionService.editQuestion(member, question, form);
		attributes.addFlashAttribute("message", "질문을 수정했습니다. 응답을 기다려주세요.");
		return "redirect:/question";
	}
	
	@PostMapping("/remove/{id}")
	public String remove(@CurrentMember Member member, @PathVariable Long id, RedirectAttributes attributes) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		questionService.removeQuestion(member, question);
		attributes.addFlashAttribute("message", "질문을 삭제했습니다.");
		return "redirect:/question";
	}

}
