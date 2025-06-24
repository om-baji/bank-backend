package com.bank.user_service.services;


import com.bank.user_service.repository.UserRepository;
import com.bank.user_service.schemas.ApiResponse;
import com.bank.user_service.schemas.RegisterSchema;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnConflictIfUsernameOrEmailExists() {
        RegisterSchema schema = new RegisterSchema();
        schema.setEmail("test@example.com");
        schema.setUsername("testuser");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        ResponseEntity<?> response = userService.saveUser(schema);

        assertEquals(409, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ApiResponse);
        ApiResponse apiResponse = (ApiResponse) response.getBody();
        assertFalse(apiResponse.isSuccess());
    }
}
