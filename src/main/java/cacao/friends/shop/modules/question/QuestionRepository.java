package cacao.friends.shop.modules.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.member.Member;

@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	@EntityGraph(attributePaths = { "questioner", "answer" })
	Page<Question> findWithQuestionerAndAnswerByTitleContaining(String title, Pageable pageable);
	
	@EntityGraph(attributePaths = { "questioner", "answer" })
	List<Question> findWithQuestionerAndAnswerByQuestionerOrderByCreatedDateAtDesc(Member questioner);

	@EntityGraph(attributePaths = { "questioner", "answer" })
	Question findWithQuestionerAndAnswerById(Long id);
	
	Long countByAnswered(boolean answered);
	
}
