package cacao.friends.shop.modules.question;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manager/question")
public class QuestionControllerManager {
	
	private final QuestionService questionService;
	
	private final QuestionRepository questionRepository;
	
	@GetMapping
	public String questionsView(
			@PageableDefault(size = 20, page = 0, direction = Direction.DESC, sort = "createdDateAt") Pageable pageable,
			@RequestParam(defaultValue = "") String title, Model model) {
		model.addAttribute("title", title);
		model.addAttribute("questionPage", questionRepository.findWithQuestionerAndAnswerByTitleContaining(title, pageable));
		return "manager/question/list";
	}
	
	@GetMapping("/{id}")
	public String questionView(@PathVariable Long id, Model model) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		model.addAttribute(question);
		return "manager/question/question";
	}
	
	@PostMapping("/remove/{id}")
	public String questionRemove(@PathVariable Long id, RedirectAttributes attributes) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		questionService.removeQuestion(question);
		attributes.addFlashAttribute("message", "질문을 삭제했습니다.");
		return "redirect:/manager/question";
	}
	
	@PostMapping("/{id}/answer")
	public String answer(@PathVariable Long id, String detail, RedirectAttributes attributes) {
		Question question = questionRepository.findWithQuestionerAndAnswerById(id);
		questionService.answer(question, detail);
		attributes.addFlashAttribute("message", "응답을 작성했습니다.");
		return "redirect:/manager/question";
	}

}
