package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;

public interface QuizService {
	
	// Generate using forward engineering

  List<QuizResponseDto> getAllQuizzes();

QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

}
