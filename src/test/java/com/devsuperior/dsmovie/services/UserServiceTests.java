package com.devsuperior.dsmovie.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.repositories.UserRepository;
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
    
    @BeforeEach
    void setUp() {
    	
	}
    
	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
	}
}
