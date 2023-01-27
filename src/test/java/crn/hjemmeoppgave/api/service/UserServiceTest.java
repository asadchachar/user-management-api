package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IRoleRepository;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.Role;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    private IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private IUserRoleRepository userRoleRepository = Mockito.mock(IUserRoleRepository.class);
    private UserService mockUserService = new UserService(userRepository, userRoleRepository);
    private List<Users> mockUsersList = Arrays.asList(new Users(1, 1, "U1"), new Users(2, 1, "U2"));
    private Users mockedUser = new Users(101, 1, "I am new mocked user");
    private Users mockUpdatedUser = new Users(202, 2, "I am updated mocked user");
    private List<UserRole> mockUsersRolesList = Arrays.asList(
            new UserRole(101, 1, 1, 1, 1, Timestamp.valueOf(LocalDateTime.now()), null),
            new UserRole(102, 1, 2, 1, 1, Timestamp.valueOf(LocalDateTime.now().minusDays(2)), null),
            new UserRole(103, 2, 1, 3, 1, Timestamp.valueOf(LocalDateTime.now().plusDays(3)), null)
    );

    @Test
    void verifyGetAllUsers() {

        when(userRepository.findAll()).thenReturn(mockUsersList);

        Iterable<Users> users = mockUserService.getAllUsers();
        List<Users> ActualUsersList = new ArrayList<>();
        users.forEach(ActualUsersList::add);

        assertTrue(users.iterator().hasNext());
        assertEquals(2, ActualUsersList.size());
        assertEquals("U1", ActualUsersList.get(0).getName());
    }

    @Test
    void getUserById() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUsersList.get(0)));

        Optional<Users> actualUser = this.mockUserService.getUserById(1);

        assertEquals(actualUser.get().getName(), "U1");
        assertEquals(actualUser.get().getId(), 1);
    }

    @Test
    void getUsersWithRoleInUnit() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockUsersList.get(0)));
    }

    @Test
    void createUser() {
        when(userRepository.save(any())).thenReturn(mockedUser);
        Users newUser = this.mockUserService.createUser(new Users(3, 1, "newUser"));
        assertNotNull(newUser);
        assertEquals(newUser.getName(), mockedUser.getName());
    }

    @Test
    void updateUser() {
        when(userRepository.updateVersionAndNameById(any(), any(), any())).thenReturn(1);
        int resp = this.mockUserService.updateUser(mockUpdatedUser.getId(), mockUpdatedUser.getName(), mockUpdatedUser.getId());
        assertEquals(resp, 1);
    }


}