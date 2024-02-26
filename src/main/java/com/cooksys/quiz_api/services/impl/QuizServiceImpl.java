package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;
  	private final QuestionRepository questionRepository;
  	private final QuizMapper quizMapper;
  	private final QuestionMapper questionMapper;

// - - - - - - - - - - HELPER METHODS - - - - - - - - - - \\  	
  	private void validateQuizRequest(QuizRequestDto quizRequestDto) {
  		if (quizRequestDto.getName() == null || quizRequestDto.getQuestions() == null)
  		{ throw new BadRequestException("All fields are required on a Quiz request DTO."); }
  	}
  	
  	private void validateQuestionRequest(QuestionRequestDto questionRequestDto) {
  		if (questionRequestDto.getText() == null || questionRequestDto.getAnswers() == null)
  		{ throw new BadRequestException("All fields are required on a Quiz request DTO."); }
  	}
  	
  	
  	public Quiz getQuiz(Long id) {
  		Optional<Quiz> optionalQuiz = quizRepository.findById(id);
  		if(optionalQuiz.isEmpty() ) {
  			throw new NotFoundException("No quiz found with id: " +id);
  		}
  		return optionalQuiz.get();
  	}
  	
  	public Question getRandomElement(List<Question> list)
  		{ Random rand = new Random();
  		return list.get(rand.nextInt(list.size()));
  	}
  	
  	
  	
  	
  	
// - - - - - - - - - - ENDPOINT IMPLEMENTATION METHODS - - - - - - - - - - \\  	
  	@Override
  	public List<QuizResponseDto> getAllQuizzes() {
	  return quizMapper.entitiesToDtos(quizRepository.findAll());
  	}

  	@Override
  	public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
  		validateQuizRequest(quizRequestDto);
	  	Quiz createdQuiz = quizMapper.requestDtoToEntity(quizRequestDto);
	  	return quizMapper.entityToDto(quizRepository.saveAndFlush(createdQuiz));
  	}

  	@Override
	public QuizResponseDto deleteQuiz(Long id) {
		Quiz quizToDelete = getQuiz(id);
		quizRepository.delete(quizToDelete);
		return quizMapper.entityToDto(quizToDelete);
	}

	@Override
	public QuizResponseDto renameQuiz(Long id, QuizRequestDto quizRequestDto) {
		Quiz quizToRename = getQuiz(id);
		quizToRename.setName(quizRequestDto.getName());
		return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToRename));
	}

	@Override
	public QuestionResponseDto randomQuestion(Long id) { // Select a random id from the question repository within the passed in quiz repository
		Quiz quizRandom = getQuiz(id);
		return questionMapper.entityToDto(getRandomElement(quizRandom.getQuestions()));
	}

	@Override
	public QuizResponseDto addQuestion(Long id, QuestionRequestDto questionRequestDto) {
		validateQuestionRequest(questionRequestDto);
		Quiz modifiedQuiz = getQuiz(id);
		List<Question> listOfQuestions = modifiedQuiz.getQuestions(); // create list of questions in our quiz
		listOfQuestions.add(questionMapper.requestDtoToEntity(questionRequestDto)); // put(questionRequestDto) in questions list of QuizAddQuestion
		modifiedQuiz.setQuestions(listOfQuestions); // set list of questions equal to the list of questions we created with the new question inside
		return quizMapper.entityToDto(modifiedQuiz);
	}

	@Override // Deletes the specified question from the specified quiz - Returns the deleted 'Question'
	public QuizResponseDto deleteQuestion(Long id, QuestionRequestDto questionRequestDto) {
		Quiz quizQuestion = getQuiz(id);
		for(Question question : quizQuestion.getQuestions()) {
			if(question.equals(questionMapper.requestDtoToEntity(questionRequestDto)))
				questionRepository.delete(question);
		}
		return quizMapper.entityToDto(quizQuestion);
	}
  
  

}
