package il.sce.scecafe.controller;

import il.sce.scecafe.dto.AuthenticationRequestDTO;
import il.sce.scecafe.entity.Role;
import il.sce.scecafe.entity.Users;
import il.sce.scecafe.repository.RoleRepository;
import il.sce.scecafe.repository.UsersRepository;
import il.sce.scecafe.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;
    private final UsersController userService;

    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UsersController userService, RoleRepository roleRepository, UsersRepository usersRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.usersRepository = usersRepository;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDTO requestDTO){
        try {
            String username = requestDTO.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDTO.getPassword()));
            Users user = userService.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("Username " + username + " not found.");
            }
            String token = jwtTokenProvider.createToken(username, user.getRoles());



            Map<Object, Object> response = new HashMap<>();
            System.out.println(username);
            response.put("username", username);
            System.out.println("Generated JWT Token: " + token);
            response.put("token", token);
            System.out.println("User ID: " + user.getId());
            response.put("userID", user.getId());
            System.out.println("User role: " + user.getRoles().get(0).getName());
            response.put("role", user.getRoles().get(0).getName());

            System.out.println("Response to client: " + response);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid login or password");
        }
    }


    public boolean addRole(Long userID, Long roleID) {
        Role newRole = this.roleRepository.findById(roleID).get();
        List<Role> rolesList = new ArrayList<>();
        rolesList.add(newRole);
        this.usersRepository.findById(userID).get().setRoles(rolesList);
        this.usersRepository.save(this.usersRepository.getReferenceById(userID));
        return true;
    }

    @GetMapping("test")
    public String test(@RequestParam String login){
        if (this.usersRepository.findByLogin(login) == null){
            return "NULL";
        }
        return "OK";
    }
    @PostMapping("register")
    public ResponseEntity register(@RequestBody Users newUser){
        if (this.usersRepository.findByLogin(newUser.getLogin()) != null)
                return ResponseEntity.status(409).body("User with same login already exist");
        if (this.usersRepository.findByPhoneNumber(newUser.getPhoneNumber()) != null)
            return ResponseEntity.status(409).body("User with same phone number already exist");
        Users user = new Users(newUser);
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(4)));
        System.out.println(user.getLogin() + " created");
        while (true){
            try {
                addRole(this.usersRepository.save(user).getId(), 2L);
                break;
            } catch (final DataIntegrityViolationException exc) {
                System.out.println("ID already exist. New try...");
            }
        }
        return ResponseEntity.ok().body("User was be created!");
    }
}
