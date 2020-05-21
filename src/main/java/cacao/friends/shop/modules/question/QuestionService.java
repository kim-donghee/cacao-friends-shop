package cacao.friends.shop.modules.question;

import javax.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;
import cacao.friends.shop.modules.notification.NotificationService;
import cacao.friends.shop.modules.question.form.QuestionForm;
import lombok.RequiredArgsConstructor;

@Controller
@Transactional
@RequiredArgsConstructor
public class QuestionService {
	
	private final EntityManager em;
	
	private final QuestionRepository questionRepository;
	
	private final NotificationService notificationService;
	
	private final ModelMapper modelMapper;
	
	public void createQuestion(Member questioner, QuestionForm form) {
		Question question = modelMapper.map(form, Question.class);
		question.setQuestioner(questioner);
		questionRepository.save(question);
	}
	
	public void removeQuestion(Member currentMember, Question question) {
		if(!question.questionerEq(currentMember))
			throw new RuntimeException("삭제 권한이 없습니다.");
		questionRepository.delete(question);
	}
	
	public void removeQuestion(Question question) {
		questionRepository.delete(question);
	}
	
	public void editQuestion(Member currentMember, Question question, QuestionForm form) {
		if(!question.questionerEq(currentMember))
			throw new RuntimeException("수정 권한이 없습니다.");
		if(question.isAnswered())
			throw new RuntimeException("응답 받은 질문은 수정할 수 없습니다.");
		modelMapper.map(form, question);
	}
	
	public void answer(Question question, String detail) {
		Answer answer = question.getAnswer();
		if(!question.isAnswered()) {
			answer = Answer.builder()
			.question(question)
			.build();
		}
		answer.setDetail(detail);
		em.persist(answer);
		question.setAnswer(answer);
		question.setAnswered(true);
		notificationService.createNotification(question.getQuestioner(), 
				"질문에 응답이 왔습니다.", question.getTitle(), "/question/" + question.getId());
	}
	
}
