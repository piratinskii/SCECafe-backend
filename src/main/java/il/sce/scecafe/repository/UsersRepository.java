package il.sce.scecafe.repository;

import il.sce.scecafe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByLogin(String name);
    Users findByPhoneNumber(String phoneNumber);
    boolean existsByLogin(String username);
}
