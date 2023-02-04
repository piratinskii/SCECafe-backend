package il.sce.scecafe.controller;
import il.sce.scecafe.entity.Users;
import il.sce.scecafe.repository.RoleRepository;
import il.sce.scecafe.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersRepository usersRepository;
    private final RoleRepository roleRepository;

    public UsersController(UsersRepository usersRepository, RoleRepository roleRepository) {
        this.usersRepository = usersRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity getAllUsers(){
        return ResponseEntity.ok(this.usersRepository.findAll());
    }

    @GetMapping("isLogged")
    public Boolean isLogged(){
        return true;
    }

    @GetMapping("getusername/{id}")
    public String getName(@PathVariable Long id)
    {
        return usersRepository.findById(id).get().getFirstname();
    }

    @GetMapping("{id}")
    public Users getProfile(@PathVariable Long id)
    {
        return usersRepository.findById(id).get();
    }

    @PostMapping("change")
    public void change(@RequestBody Users user){
        this.usersRepository.findById(user.getId()).get().setFirstname(user.getFirstname());
        this.usersRepository.findById(user.getId()).get().setLastname(user.getLastname());
        this.usersRepository.findById(user.getId()).get().setBirthDay(user.getBirthDay());
        this.usersRepository.findById(user.getId()).get().setEmail(user.getEmail());
        this.usersRepository.save(this.usersRepository.getReferenceById(user.getId()));
    }

    public Users findByUsername(String login){
        return this.usersRepository.findByLogin(login);
    }

    @GetMapping("/findbyphone")
    public Users findByPhoneNumber(@RequestParam String phoneNumber){
        return this.usersRepository.findByPhoneNumber(phoneNumber);
    }

    @GetMapping("/getrole")
    public String getRole(@RequestParam Long id){
        return this.usersRepository.findById(id).get().getRoles().get(0).getName();
    }

    @PostMapping("change/password")
    public void changePassword(@RequestBody Users user){
        this.usersRepository.findById(user.getId()).get().setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(4)));
        this.usersRepository.save(this.usersRepository.getReferenceById(user.getId()));
    }
}
