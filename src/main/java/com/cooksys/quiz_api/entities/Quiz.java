package com.cooksys.quiz_api.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Quiz {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL) // if deleting, will delete underneath it (questions)
  private List<Question> questions = new ArrayList<>(); 
  // will automatically populate questions as an empty array when creating a quiz entity, then adds questions if there are some
  
  private boolean deleted;

}
