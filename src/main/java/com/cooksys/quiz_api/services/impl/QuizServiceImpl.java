package com.cooksys.quiz_api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.cooksys.quiz_api.dtos.QuestionRequestDto;
import com.cooksys.quiz_api.dtos.QuestionResponseDto;
import com.cooksys.quiz_api.dtos.QuizRequestDto;
import com.cooksys.quiz_api.dtos.QuizResponseDto;
import com.cooksys.quiz_api.entities.Answer;
import com.cooksys.quiz_api.entities.Question;
import com.cooksys.quiz_api.entities.Quiz;
import com.cooksys.quiz_api.exceptions.BadRequestException;
import com.cooksys.quiz_api.exceptions.NotFoundException;
import com.cooksys.quiz_api.mappers.QuestionMapper;
import com.cooksys.quiz_api.mappers.QuizMapper;
import com.cooksys.quiz_api.repositories.AnswerRepository;
import com.cooksys.quiz_api.repositories.QuestionRepository;
import com.cooksys.quiz_api.repositories.QuizRepository;
import com.cooksys.quiz_api.services.QuizService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizRepository quizRepository;
  	private final QuestionRepository questionRepository;
  	private final AnswerRepository answerRepository;
  	private final QuizMapper quizMapper;
  	private final QuestionMapper questionMapper;

// - - - - - - - - - - HELPER METHODS - - - - - - - - - - \\  	
  	private void validateQuizRequest(QuizRequestDto quizRequestDto) {
  		if (quizRequestDto == null || quizRequestDto.getName() == null)
  		{ throw new BadRequestException("All fields are required on a Quiz request DTO."); }
  	}
  	
  	private void validateQuestionRequest(QuestionRequestDto questionRequestDto) {
  		if (questionRequestDto.getText() == null || questionRequestDto.getAnswers() == null)
  		{ throw new BadRequestException("All fields are required on a Quiz request DTO."); }
  	}
  	
  	
  	public Quiz getQuiz(Long id) {
  		Optional<Quiz> optionalQuiz = quizRepository.findByIdAndDeletedFalse(id);
  		if(optionalQuiz.isEmpty()) 
  			{ throw new NotFoundException("No quiz found with id: " +id); }
  		return optionalQuiz.get();
  	}
  	
  	public Question getQuestion(Long id) {
  		Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(id);
  		if(optionalQuestion.isEmpty()) 
  			{ throw new NotFoundException("No quiz found with id: " +id); }
  		return optionalQuestion.get();
  	}
  	
  	public Question getRandomElement(List<Question> list)
  		{ Random rand = new Random();
  		return list.get(rand.nextInt(list.size())); // returns a random index of the passed in list
  	}
  	
  	
  	
  	
  	
// - - - - - - - - - - ENDPOINT IMPLEMENTATION METHODS - - - - - - - - - - \\  	
  	@Override
  	public List<QuizResponseDto> getAllQuizzes() {
	  return quizMapper.entitiesToDtos(quizRepository.findAll());
  	}

  	@Override
  	public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
  		validateQuizRequest(quizRequestDto);
	  	Quiz quizToCreate = quizMapper.requestDtoToEntity(quizRequestDto);
	  	quizRepository.saveAndFlush(quizToCreate); // Save Quiz first, then questions, then answers
	  	for(Question question : quizToCreate.getQuestions()) {
	  		question.setQuiz(quizToCreate); // set the current question in our passed in quiz
	  		questionRepository.saveAndFlush(question); // save current question into respective database
	  		for(Answer answer : question.getAnswers()) {
	  			answer.setQuestion(question); // set the current answer in our current question
	  		}
	  		answerRepository.saveAllAndFlush(question.getAnswers()); // save all answers into their respective database
	  	}
	  	return quizMapper.entityToDto(quizToCreate);
  	}

  	@Override
	public QuizResponseDto deleteQuiz(Long id) {
		Quiz quizToDelete = getQuiz(id);
		//quizToDelete.setDeleted(true);
//		for(Question question : quizToDelete.getQuestions()) {
//			answerRepository.deleteAll(question.getAnswers()); // deleteAll (ITERABLE) version
//			//question.setDeleted(true);
//		}
//		questionRepository.deleteAll(quizToDelete.getQuestions());
		quizRepository.delete(quizToDelete); // deletes questions & answers because of CASCADE on the entities
		return quizMapper.entityToDto(quizToDelete);
	}

  	@Override
	public QuizResponseDto renameQuiz(Long id, String newName) {
		Quiz quizToRename = getQuiz(id);
		quizToRename.setName(newName);
		return quizMapper.entityToDto(quizRepository.saveAndFlush(quizToRename));
	}

	@Override
	public QuestionResponseDto randomQuestion(Long id) { // Select a random id from the question repository within the passed in quiz repository
		Quiz quizRandom = getQuiz(id);
		/* Random rand = new Random();
		 * int randomNumber = rand.nextInt(quizRandom.getQuestions().size());
		 * return questionMapper.entityToDto(quizRandom.getQuestions().get(randomNumber));  */
		return questionMapper.entityToDto(getRandomElement(quizRandom.getQuestions()));
	}

	@Override
	public QuizResponseDto addQuestion(Long id, QuestionRequestDto questionRequestDto) {
		validateQuestionRequest(questionRequestDto);
		Quiz modifyQuiz = getQuiz(id);
		Question questionToAdd = questionMapper.requestDtoToEntity(questionRequestDto);
		questionToAdd.setQuiz(modifyQuiz);
		for(Answer answerOfQuestion : questionToAdd.getAnswers()) {
		  if(answerOfQuestion.getText() == null) { throw new NotFoundException("No text in answer."); }
		  answerOfQuestion.setQuestion(questionToAdd);
		}
		questionRepository.saveAndFlush(questionToAdd);
		answerRepository.saveAllAndFlush(questionToAdd.getAnswers());
		return quizMapper.entityToDto(modifyQuiz);	 
	}

	@Override // Deletes the specified question from the specified quiz - Returns the deleted 'Question'
	public QuestionResponseDto deleteQuestion(Long id, Long questionID) {
		Quiz quizToSelect = getQuiz(id);
		Question questionToDelete = getQuestion(questionID);
		if(!questionToDelete.getQuiz().equals(quizToSelect)) { throw new BadRequestException("No such question exists in this quiz."); }
		answerRepository.deleteAll(questionToDelete.getAnswers()); // delete all answers related to this question
		questionRepository.deleteById(questionID); // delete this question
		return questionMapper.entityToDto(questionToDelete);
	}  

}
