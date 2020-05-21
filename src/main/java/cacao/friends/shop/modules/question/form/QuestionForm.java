package cacao.friends.shop.modules.question.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class QuestionForm {
	
	@Length(min = 1, max = 50)
	private String title;
	
	@NotBlank
	private String detail;
	
	@NotNull
	private Boolean published;
	
}
