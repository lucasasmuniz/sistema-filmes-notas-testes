package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserFactory;
import com.devsuperior.dsmovie.utils.CustomUserUtil;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class UserServiceTests {

	@InjectMocks
	private UserService service;
	
    @Mock
    private UserRepository repository;
    
    @Mock	
    private CustomUserUtil userUtil;

    private String validUsername;
    private String invalidUsername;
    private UserEntity userValid;
    
    @BeforeEach
    void setUp() {
    	validUsername = "Maria";
    	invalidUsername = "Joao";
    	userValid = UserFactory.createUserEntity();
    	
    	Mockito.when(repository.findByUsername(validUsername)).thenReturn(Optional.of(userValid));
    	Mockito.when(repository.findByUsername(invalidUsername)).thenReturn(Optional.empty());
	}
    
	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(validUsername);
		UserEntity result = service.authenticated();
		
		assertNotNull(result);
		assertEquals(userValid.getName(), result.getName());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(invalidUsername);
		assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
		Mockito.verify(userUtil, Mockito.times(1)).getLoggedUsername();
		Mockito.verify(repository, Mockito.times(1)).findByUsername(invalidUsername);
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
	}
}
