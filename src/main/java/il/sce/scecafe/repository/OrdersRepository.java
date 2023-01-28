package il.sce.scecafe.repository;

import il.sce.scecafe.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByStatus(String Status);
    List<Orders> findByUserID(Long userID);
}
