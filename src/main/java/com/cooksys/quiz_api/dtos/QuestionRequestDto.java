package com.cooksys.quiz_api.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class QuestionRequestDto {
	
	  private Long id;

	  private String text;

	  private List<AnswerRequestDto> answers;

}
