package il.sce.scecafe.security.jwt;

import il.sce.scecafe.entity.Role;
import il.sce.scecafe.entity.Users;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(Users user) {
        return new JwtUser(
             user.getId(),
             user.getLogin(),
             user.getPassword(),
             user.getBirthDay(),
             user.getFirstname(),
             user.getLastname(),
             user.getEmail(),
             mapToGrantedAuthorities(new ArrayList<>(user.getRoles()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles){
        return userRoles.stream()
                .map(role ->
                    new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
