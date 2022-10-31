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
		// TodoEntity ����
		TodoEntity entity = TodoEntity.builder().title("My first doto item").build();
		
		// TodoEntity ����
		repository.save(entity);
		
		// TodoEntity �˻�
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
	
	// �����丵
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
		// ������ ��ƼƼ�� ��ȿ���� Ȯ��
		validate(entity);
		
		// �Ѱܹ��� ��ƼƼ id�� �̿��� TodoEntity ������
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		if(original.isPresent()) {
			// ��ȯ�� TodoEntity�� �����ϸ� ���� �� entity ������ ���� ����
			final TodoEntity todo = original.get();
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// �����ͺ��̽��� �� ���� ����
			repository.save(todo);
		}
		
		// Retrieve Todo���� ���� �޼��带 �̿��� ������� ��� Todo ����Ʈ�� ����
		return retrieve(entity.getUserId());
	}
	
	public List<TodoEntity> delete(final TodoEntity entity) {
		// ������ ��ƼƼ�� ��ȿ���� Ȯ��
		validate(entity);
		
		try {
			// ��ƼƼ�� ����
			repository.delete(entity);
			
		} catch (Exception e) {
			// exception �α�
			log.error("error deleting entity ", entity.getId(), e);
			
			// ��Ʈ�ѷ��� exception�� ����. �����ͺ��̽� ���� ������ ĸ��ȭ�Ϸ��� e�� �������� �ʰ� �� exception ������Ʈ�� ����
			throw new RuntimeException("error deleting entity " + entity.getId());
		}
		
		// �� Todo ����Ʈ�� ������ ����
		return retrieve(entity.getUserId());
	}
}
