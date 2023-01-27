package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.Role;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoleServiceTest {

    private IRoleRepository roleRepository = Mockito.mock(IRoleRepository.class);
    private RoleService roleService = new RoleService(roleRepository);
    private Optional<Role> mockedRole = Optional.ofNullable(new Role(1, 1, "new mocked role"));
    private List<Role> mockedRoles = Arrays.asList(
            new Role(301,1,"role1"),
            new Role(302,1,"role2"),
            new Role(303,2,"role3")
    );

    @Test
    void getAllRoles() {
        when(this.roleRepository.findAll()).thenReturn(mockedRoles);

        Iterable<Role> roles = this.roleService.getAllRoles();
        List<Role> actualRoles = new ArrayList<>();
        roles.forEach(actualRoles::add);

        assertEquals(3, actualRoles.size());
        assertEquals(actualRoles.get(1).getName(), "role2");
    }

    @Test
    void getRoleById() {
        when(this.roleRepository.findById(any())).thenReturn(mockedRole);

        Optional<Role> actualRole = this.roleService.getRoleById(123);

        assertNotNull(actualRole.get());
        assertEquals("new mocked role", actualRole.get().getName());
    }
}