package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo() {
		String str = service.testService();
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	
	@PostMapping
	public ResponseEntity<?> createTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user"; // temporary user id
			
			// TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// id�� null�� �ʱ�ȭ (���� ��ÿ��� id�� ����� �ϱ� ����)
			entity.setId(null);
			
			// �ӽ� ����� ���̵� ���� (����, �ΰ� ��� ����)
			entity.setUserId(temporaryUserId);
			
			// ���񽺸� �̿��� ��ƼƼ ����
			List<TodoEntity> entities = service.create(entity);
			
			// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// ResponseDTO ����
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	public ResponseEntity<?> retrieveTodoList() {
		String temporaryUserId = "temporary-user"; //temporary user id
		
		// ���� �޼����� retrieve() �޼��带 ����� Todo ����Ʈ ������
		List<TodoEntity> entities = service.retrieve(temporaryUserId);
		
		// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// ResponseDTO�� ����
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
		String temporaryUserId = "temporary-user";	// temporary user id
		
		// dto�� entity�� ��ȯ
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// id�� temporaryUserId�� �ʱ�ȭ
		entity.setUserId(temporaryUserId);
		
		// ���񽺸� �̿��� entity�� ������Ʈ
		List<TodoEntity> entities = service.update(entity);
		
		// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		// ResponseDTO�� ����
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@RequestBody TodoDTO dto) {
		try {
			String temporaryUserId = "temporary-user";
			
			// TodoEntity�� ��ȯ
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			// �ӽ� ����� ���̵� ����
			entity.setUserId(temporaryUserId);
			
			// ���񽺸� �̿��� entity�� ����
			List<TodoEntity> entities = service.delete(entity);
			
			// �ڹ� ��Ʈ���� �̿��� ���ϵ� ��ƼƼ ����Ʈ�� TodoDTO ����Ʈ�� ��ȯ
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// ��ȯ�� TodoDTO ����Ʈ�� �̿��� ResponseDTO�� �ʱ�ȭ
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			// ResponseDTO�� ����
			return ResponseEntity.ok().body(response);
			
		} catch (Exception e) {
			// dto ��� error�� �޽����� �־� ����
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
}
