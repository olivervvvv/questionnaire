package com.example.questionnaire.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.QuestionId;

@Repository
public interface QuestionDao extends JpaRepository<Question, QuestionId>{

	public List<Question> deleteAllByQnIdIn(List<Integer> qnidList);

	public List<Question> findByQuIdInAndQnId(List<Integer> quid,int qnid);
	
	public List<Question> deleteAllByQnIdInAndQuId(List<Integer> id,int qnid);
	
	public List<Question> findAllByQnIdIn(List<Integer> qnid);
	
}
