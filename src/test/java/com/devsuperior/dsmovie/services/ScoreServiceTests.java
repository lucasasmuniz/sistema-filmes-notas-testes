package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	@Mock
    private UserService userService;
    
	@Mock
    private MovieRepository movieRepository;
    
	@Mock
    private ScoreRepository scoreRepository;
	
	private Long existingId, nonExistingId;
	private UserEntity user;
	private MovieEntity movie;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 2L;
		score = ScoreFactory.createScoreEntity();
		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();
		movie.getScores().add(score);
		scoreDTO = ScoreFactory.createScoreDTO();
		
		when(userService.authenticated()).thenReturn(user);
		when(movieRepository.save(any())).thenReturn(movie);
		when(scoreRepository.saveAndFlush(any())).thenReturn(score);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		when(movieRepository.findById(existingId)).thenReturn(Optional.of(movie));
		MovieDTO result = service.saveScore(scoreDTO);
		
		assertNotNull(result);
		assertEquals(movie.getTitle(), result.getTitle());
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		ScoreDTO dto = new ScoreDTO(nonExistingId, 22.4);
		assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(dto);
		});
		verify(movieRepository, times(1)).findById(nonExistingId);
	}
}
