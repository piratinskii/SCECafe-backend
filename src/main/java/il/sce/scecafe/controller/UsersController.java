package il.sce.scecafe.controller;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import il.sce.scecafe.entity.Orders;
import il.sce.scecafe.entity.Position;
import il.sce.scecafe.entity.Users;
import il.sce.scecafe.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 2400, allowCredentials = "false")
@RequestMapping("/users")
public class UsersController {
    private final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.usersRepository.findAll());
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

    @GetMapping("checkpassword")
    public Boolean checkPassword(@RequestParam Long id, @RequestParam String password)
    {
        return usersRepository.findById(id).get().getPassword().equals(password);
    }

    @GetMapping("getrole")
    public String checkPassword(@RequestParam Long id)
    {
        return usersRepository.findById(id).get().getRole();
    }

    @GetMapping("{username}/{password}")
    public Long auth(@PathVariable String username, @PathVariable String password){
        for(Users user: this.usersRepository.findAll()){
            if (user.getLogin().equals(username)){
                if (user.getPassword().equals(password)){
                    return user.getId();
                }
                else return 0L;
            }
        }
        return 0L;
    }

    @PostMapping("change")
    public void change(@RequestBody Users user){
        this.usersRepository.findById(user.getId()).get().setFirstname(user.getFirstname());
        this.usersRepository.findById(user.getId()).get().setLastname(user.getLastname());
        this.usersRepository.findById(user.getId()).get().setBirthDay(user.getBirthDay());
        this.usersRepository.findById(user.getId()).get().setEmail(user.getEmail());
        this.usersRepository.save(this.usersRepository.getReferenceById(user.getId()));
    }

    @PostMapping("change/password")
    public void changePassword(@RequestBody Users user){
        System.out.println(user.getPassword());
        this.usersRepository.findById(user.getId()).get().setPassword(user.getPassword());
        this.usersRepository.save(this.usersRepository.getReferenceById(user.getId()));
    }
}
