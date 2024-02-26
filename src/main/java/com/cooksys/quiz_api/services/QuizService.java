package com.cooksys.quiz_api.services;

import java.util.List;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;

public interface QuizService {
	
	// Generate using forward engineering

	List<QuizResponseDto> getAllQuizzes();

  	QuizResponseDto createQuiz(QuizRequestDto quizRequestDto);

  	QuizResponseDto deleteQuiz(Long id);

  	QuizResponseDto renameQuiz(Long id, QuizRequestDto quizRequestDto);

  	QuestionResponseDto randomQuestion(Long id);

  	QuizResponseDto addQuestion(Long id, QuestionRequestDto questionRequestDto);

	QuizResponseDto deleteQuestion(Long id, QuestionRequestDto questionRequestDto);




}
