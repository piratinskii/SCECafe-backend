package il.sce.scecafe.security;

import il.sce.scecafe.controller.UsersController;
import il.sce.scecafe.entity.Users;
import il.sce.scecafe.security.jwt.JwtUser;
import il.sce.scecafe.security.jwt.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsersController userService;
    @Autowired
    public JwtUserDetailsService(UsersController userService){
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userService.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        JwtUser jwtUser = JwtUserFactory.create(user);
        return jwtUser;
    }
}
