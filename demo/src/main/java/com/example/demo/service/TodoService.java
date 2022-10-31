package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {
	
	@Autowired
	private TodoRepository repository;
	
	public String testService() {
		// TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first doto item").build();
		
		// TodoEntity 저장
		repository.save(entity);
		
		// TodoEntity 검색
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		
		return savedEntity.getTitle();
	}
	
	public List<TodoEntity> create(final TodoEntity entity) {
		// Validations
		validate(entity);
		
		repository.save(entity);
		
		log.info("Entity Id : {} is saved.", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	// 리팩토링
	private void validate(final TodoEntity entity) {
		if(entity == null) {
			log.warn("Entity cannot be null.");
			throw new RuntimeException("Entity cannot be null.");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user.");
			throw new RuntimeException("Unknown user.");
		}
	}
	
	public List<TodoEntity> retrieve(final String userId) {
		return repository.findByUserId(userId);
	}
	
	public List<TodoEntity> update(final TodoEntity entity) {
		// 저장할 엔티티가 유효한지 확인
		validate(entity);
		
		// 넘겨받은 엔티티 id를 이용해 TodoEntity 가져옴
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		if(original.isPresent()) {
			// 반환된 TodoEntity가 존재하면 값을 새 entity 값으로 덮어 씌움
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// 데이터베이스에 새 값을 저장
			repository.save(todo);
		}
		
		// Retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		// 저장할 엔티티가 유효한지 확인
		validate(entity);
		
		try {
			// 엔티티를 삭제
			repository.delete(entity);
			
		} catch (Exception e) {
			// exception 로깅
			log.error("error deleting entity ", entity.getId(), e);
			
			// 컨트롤러로 exception을 보냄. 데이터베이스 내부 로직을 캡슐화하려면 e를 리턴하지 않고 새 exception 오브젝트를 리턴
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// 새 Todo 리스트를 가져와 리턴
		return retrieve(entity.getUserId());
	}
}
