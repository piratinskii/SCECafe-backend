package il.sce.scecafe.repository;

import il.sce.scecafe.entity.Item;
import il.sce.scecafe.entity.Orders;
import il.sce.scecafe.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByOrdID(Long id);
    List<Position> findByItem(Item item);
}
