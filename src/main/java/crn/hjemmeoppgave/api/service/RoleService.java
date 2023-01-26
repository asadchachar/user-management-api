package crn.hjemmeoppgave.api.service;

import crn.hjemmeoppgave.api.dao.IRoleRepository;
import crn.hjemmeoppgave.api.dao.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    IRoleRepository roleRepository;

    public Iterable<Role> getAllRoles() {
        return this.roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Integer roleId) {
        return this.roleRepository.findById(roleId);
    }
}
