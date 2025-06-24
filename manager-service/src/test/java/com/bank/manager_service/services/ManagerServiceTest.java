package com.bank.manager_service.services;


import com.bank.manager_service.repository.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ManagerServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ManagerService userService;

    public ManagerServiceTest() {
        MockitoAnnotations.openMocks(this);
    }


}
