package il.sce.scecafe.service;

import il.sce.scecafe.entity.Role;
import il.sce.scecafe.entity.Users;
import il.sce.scecafe.repository.RoleRepository;
import il.sce.scecafe.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        createRoleIfNotExist("USER");
        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("GUEST");
        createRoleIfNotExist("BARISTA");

        createUserIfNotExist("guest", "guest", "GUEST");
        createUserIfNotExist("user", "user", "USER");
        createUserIfNotExist("admin", "admin", "ADMIN");
        createUserIfNotExist("barista", "barista", "BARISTA");
    }

    private void createRoleIfNotExist(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    public void createUserIfNotExist(String username, String password, String roleName) {
        if (!usersRepository.existsByLogin(username)) {
            Role role = roleRepository.findByName(roleName);
            Users user = new Users();
            user.setLogin(username);
            user.setFirstname(username);
            user.setLastname(username);
            user.setEmail(username + "@sce.com");
            user.setBirthDay("01.01.1990");
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Collections.singletonList(role));
            usersRepository.save(user);
        }
    }
}
