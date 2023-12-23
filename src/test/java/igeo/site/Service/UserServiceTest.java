package igeo.site.Service;

import igeo.site.DTO.CreateUserDto;
import igeo.site.Model.User;
import igeo.site.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void getCurrentUser() {
        // Mock SecurityContext and Authentication
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        // Prepare a test user
        User testUser = User.createUser(new CreateUserDto("duf7317@naver.com","12321","HSY","1234"),passwordEncoder);

        // Set up the mock Authentication to return the test user
        Authentication authentication = mock(Authentication.class);

        when(authentication.getDetails()).thenReturn(testUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Create UserService and call getCurrentUser
        UserService userService = new UserService();
        User currentUser = userService.getCurrentUser();

        // Assert that the returned user is the same as the test user
        assertEquals(testUser, currentUser);
    }

    @Test
    void save() {

        //given
        CreateUserDto createUserDto = new CreateUserDto("duf7317@naver.com","1234","황승열","1212");
        User createUser =User.createUser(createUserDto,passwordEncoder);
        // configure userRepository.save to return a non-null user
        when(userRepository.save(Mockito.any(User.class))).thenReturn(createUser);
        User savedUser = userService.save(createUser);
        assertNotNull(savedUser);
        assertEquals(savedUser.getEmail() , "duf7317@naver.com");
        assertEquals(savedUser.getName() , "황승열");

        verify(userRepository,times(1)).save(Mockito.any(User.class));

    }


    @Test
    void isAuthenticated() {
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        // Prepare a test user
        User testUser = User.createUser(new CreateUserDto("duf7317@naver.com","12321","HSY","1234"),passwordEncoder);

        // Set up the mock Authentication to return the test user
        Authentication authentication = mock(Authentication.class);

        when(authentication.getDetails()).thenReturn(testUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        UserService userService = new UserService();
        boolean isAuthenticated = userService.isAuthenticated();

        assertTrue(isAuthenticated);
    }

    @Test
    void loadUserByUsername() {
        User testUser = User.createUser(new CreateUserDto("duf7317@naver.com","12321","HSY","1234"),passwordEncoder);
        when(userRepository.findByEmail("duf7317@naver.com")).thenReturn(testUser);

        UserDetails userDetails = userService.loadUserByUsername("duf7317@naver.com");
        assertNotNull(userDetails);
        assertEquals("duf7317@naver.com", userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void deleteAccount_unauthenticatedUser_notDeleted() {
        // given
        // No need to set up authentication for an unauthenticated user
        when(userService.isAuthenticated()).thenReturn(false);

        // when
        boolean deletionResult = userService.deleteAccount();

        // then
        assertFalse(deletionResult);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void deleteAccount_authenticatedUser_successfullyDeleted() {
        // given
        CreateUserDto createUserDto = new CreateUserDto("duf7317@naver.com", "1234", "황승열", "1212");
        User createUser = User.createUser(createUserDto, passwordEncoder);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(createUser);

        // Save the user
        User savedUser = userService.save(createUser);
        assertNotNull(savedUser);

        // Set up security context with the saved user as the current user
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(savedUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.isAuthenticated()).thenReturn(true);

        // when
        boolean deletionResult = userService.deleteAccount();

        // then
        assertTrue(deletionResult);
        verify(userRepository, times(1)).delete(savedUser);
    }

    @Test
    void deleteAccount_exceptionDuringDeletion_notDeleted() {
        // given
        CreateUserDto createUserDto = new CreateUserDto("duf7317@naver.com", "1234", "황승열", "1212");
        User createUser = User.createUser(createUserDto, passwordEncoder);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(createUser);

        // Save the user
        User savedUser = userService.save(createUser);
        assertNotNull(savedUser);

        // Set up security context with the saved user as the current user
        org.springframework.security.core.context.SecurityContext securityContext = mock(
                org.springframework.security.core.context.SecurityContext.class);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        Authentication authentication = mock(Authentication.class);

        when(authentication.getDetails()).thenReturn(savedUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(userService.isAuthenticated()).thenReturn(true);

        // Mock an exception during deletion
        doThrow(new RuntimeException("Simulated deletion error")).when(userRepository).delete(savedUser);

        // when
        boolean deletionResult = userService.deleteAccount();

        // then
        assertFalse(deletionResult);

        // Verify that delete is called with the correct user instance
        verify(userRepository, times(1)).delete(eq(savedUser));
    }
}