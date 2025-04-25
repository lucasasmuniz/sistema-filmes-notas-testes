package com.devsuperior.dsmovie.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.projections.UserDetailsProjection;
import com.devsuperior.dsmovie.repositories.UserRepository;
import com.devsuperior.dsmovie.tests.UserDetailsFactory;
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

    private String existingUsername;
    private String nonExistingUsername;
    private UserEntity userValid;
    private List<UserDetailsProjection> list;
    
    @BeforeEach
    void setUp() {
    	existingUsername = "Maria";
    	nonExistingUsername = "Joao";
    	userValid = UserFactory.createUserEntity();
    	list = UserDetailsFactory.createCustomAdminClientUser(existingUsername);
    	
    	Mockito.when(repository.findByUsername(existingUsername)).thenReturn(Optional.of(userValid));
    	Mockito.when(repository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());
    	when(repository.searchUserAndRolesByUsername(existingUsername)).thenReturn(list);
    	when(repository.searchUserAndRolesByUsername(nonExistingUsername)).thenReturn(new ArrayList<>());
	}
    
	@Test
	public void authenticatedShouldReturnUserEntityWhenUserExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(existingUsername);
		UserEntity result = service.authenticated();
		
		assertNotNull(result);
		assertEquals(userValid.getName(), result.getName());
	}

	@Test
	public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		Mockito.when(userUtil.getLoggedUsername()).thenReturn(nonExistingUsername);
		assertThrows(UsernameNotFoundException.class, () -> {
			service.authenticated();
		});
		Mockito.verify(userUtil, Mockito.times(1)).getLoggedUsername();
		Mockito.verify(repository, Mockito.times(1)).findByUsername(nonExistingUsername);
	}

	@Test
	public void loadUserByUsernameShouldReturnUserDetailsWhenUserExists() {
		UserDetails result = service.loadUserByUsername(existingUsername);
		
		assertNotNull(result);
		assertEquals(list.getFirst().getUsername(), result.getUsername());
		
	}

	@Test
	public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists() {
		assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(nonExistingUsername);
		});
		
		verify(repository, times(1)).searchUserAndRolesByUsername(nonExistingUsername);
	}
}
