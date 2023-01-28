package il.sce.scecafe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import il.sce.scecafe.entity.Users;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public Users toUsers() {
        Users user = new Users();
        user.setId(id);
        user.setLogin(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);

        return user;
    }

    public static UserDto fromUser(Users user){
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.username = user.getLogin();
        userDto.firstname = user.getFirstname();
        userDto.lastname = user.getLastname();
        userDto.email = user.getEmail();

        return userDto;
    }
}
