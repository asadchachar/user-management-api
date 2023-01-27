package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IRoleRepository;
import crn.hjemmeoppgave.api.dao.IUserRepository;
import crn.hjemmeoppgave.api.dao.IUserRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.Role;
import crn.hjemmeoppgave.api.dao.entities.UserRole;
import crn.hjemmeoppgave.api.dao.entities.Users;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserRoleServiceTest {
    private IUserRepository userRepository = Mockito.mock(IUserRepository.class);
    private IUserRoleRepository userRoleRepository = Mockito.mock(IUserRoleRepository.class);
    private IRoleRepository roleRepository = Mockito.mock(IRoleRepository.class);
    private UserRoleService userRoleService = new UserRoleService(userRepository, userRoleRepository, roleRepository);
    private Optional<Role> mockedRole = Optional.ofNullable(new Role(1, 1, "new mocked role"));
    private UserRole mockUserRoles = new UserRole(111, 1, 1, 1, 1, Timestamp.valueOf(LocalDateTime.now()), null);
    private List<UserRole> mockUsersRolesList = Arrays.asList(
            new UserRole(101, 1, 1, 1, 1, Timestamp.valueOf(LocalDateTime.now()), null),
            new UserRole(102, 1, 2, 1, 1, Timestamp.valueOf(LocalDateTime.now().minusDays(2)), null),
            new UserRole(103, 2, 1, 3, 1, Timestamp.valueOf(LocalDateTime.now().plusDays(3)), null)
    );
    private Users mockedUser = new Users(101, 1, "I am new mocked user");

    @Test
    void getAllUserRoles() {
        when(userRoleRepository.findAll()).thenReturn(mockUsersRolesList);

        Iterable<UserRole> actualUserRoles = this.userRoleService.getAllUserRoles();
        List<UserRole> actualUsersRoles = new ArrayList<>();
        actualUserRoles.forEach(actualUsersRoles::add);

        assertTrue(actualUserRoles.iterator().hasNext());
        assertEquals(3, actualUsersRoles.size());

    }

    @Test
    void getValidUserRoles() {
        when(userRoleRepository.findAll()).thenReturn(mockUsersRolesList);

        List<List<UserRole>> actualValidUsersRoles = this.userRoleService.getValidUserRoles(1, 1, null);

        assertEquals(1, actualValidUsersRoles.get(0).size());
    }

    @Test
    void createuserRole() {
        when(userRoleRepository.save(any())).thenReturn(mockUserRoles);
        when(roleRepository.findById(any())).thenReturn(mockedRole);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(mockedUser));

        UserRole userRole = this.userRoleService.createUserRole(mockUserRoles);

        assertEquals(111, userRole.getId());
    }
}