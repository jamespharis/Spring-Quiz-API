package com.cooksys.quiz_api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.services.QuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

	private final QuizService quizService;

	@GetMapping // Returns the collection of 'Quiz' elements
	public List<QuizResponseDto> getAllQuizzes() {
		return quizService.getAllQuizzes();
	}

	@PostMapping // creates a a quiz & adds it to collection - returns the 'Quiz' that it created
	public QuizResponseDto createQuiz(@RequestBody QuizRequestDto quizRequestDto) {
		return quizService.createQuiz(quizRequestDto);
	}

	@DeleteMapping("/{id}") // Deletes the specified quiz from collection - returns the deleted 'Quiz'
	public QuizResponseDto deleteQuiz(@PathVariable Long id) {
		return quizService.deleteQuiz(id);
	}

	@PatchMapping("/{id}/rename/{newName}") // Rename the specified quiz using the new name given - Returns the renamed 'Quiz'
	public QuizResponseDto renameQuiz(@PathVariable Long id, @RequestBody QuizRequestDto quizRequestDto) {
		return quizService.renameQuiz(id, quizRequestDto);
	}

	@GetMapping("/{id}/random") // Returns a random 'Question' from the specified quiz
	public QuestionResponseDto randomQuestion(@PathVariable Long id) {
		return quizService.randomQuestion(id);
	}

	@PatchMapping("/{id}/add") // adds a question to the specified quiz - Receives a 'Question' & Returns the modified 'Quiz'
	public QuizResponseDto addQuestion(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) {
		return quizService.addQuestion(id, questionRequestDto);
	}

	@DeleteMapping("/{id}/delete/{question}") // Deletes the specified question from the specified quiz - Returns the deleted 'Question'
	public QuizResponseDto deleteQuestion(@PathVariable Long id, @RequestBody QuestionRequestDto questionRequestDto) {
		return quizService.deleteQuestion(id, questionRequestDto);
	}
}
