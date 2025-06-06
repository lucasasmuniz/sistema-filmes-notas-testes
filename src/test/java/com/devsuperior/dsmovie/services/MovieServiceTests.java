package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;
	
	@Mock
	private MovieRepository repository;
	
	private PageImpl<MovieEntity> page;
	private Long existingId;
	private Long nonExistingId; 
	private Long dependentId;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		movie = MovieFactory.createMovieEntity();
		movieDTO = MovieFactory.createMovieDTO();
		page = new PageImpl<>(List.of(movie));
		
		Mockito.when(repository.searchByTitle(any(), (Pageable)any())).thenReturn(page);
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(repository.save(any())).thenReturn(movie);
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(movie);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}


	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		PageRequest pageRequest = PageRequest.of(0, 12);
		
		Page<MovieDTO> result = service.findAll("", pageRequest);
		
		Assertions.assertNotNull(result);
	}

	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(movie.getTitle(), result.getTitle());
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
		Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
	}

	@Test
	public void insertShouldReturnMovieDTO() {
		MovieDTO result = service.insert(movieDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(movieDTO.getTitle(), result.getTitle());
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.update(existingId, movieDTO);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(existingId, result.getId());
		Assertions.assertEquals(movie.getTitle(), result.getTitle());
	}
	
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, movieDTO);
		});
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		}) ;
		Mockito.verify(repository, Mockito.times(1)).existsById(existingId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,() -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).existsById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class,() -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).existsById(dependentId);
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}
}