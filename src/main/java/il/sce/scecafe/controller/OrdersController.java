package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Orders;
import il.sce.scecafe.entity.Position;
import il.sce.scecafe.repository.OrdersRepository;
import il.sce.scecafe.repository.PositionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersRepository ordersRepository;
    private final PositionRepository positionRepository;

    public OrdersController(OrdersRepository ordersRepository, PositionRepository positionRepository) {
        this.ordersRepository = ordersRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public List<Orders> getAllOrders(){
        return this.ordersRepository.findAll();
    }

    @GetMapping("/preparing")
    public List<Orders> getPreparing(){
        return this.ordersRepository.findByStatus("paid");
    }

    @PostMapping("/done")
    public ResponseEntity setDone(@RequestParam Long id){
        this.ordersRepository.findById(id).get().setStatus("done");
        return ResponseEntity.status(201).body(this.ordersRepository.save(this.ordersRepository.getReferenceById(id)));
    }

    @PostMapping("/clear")
    public ResponseEntity clearOrder(@RequestParam Long id){
        for (Position position: this.ordersRepository.findById(id).get().getPositions()){
            this.positionRepository.deleteById(position.getId());
        }
        return ResponseEntity.status(201).body("OK");
    }

    @GetMapping("{id}")
    public List getOrder(@PathVariable Long id){
        List result = new ArrayList();
        Orders order = ordersRepository.findById(id).get();
        for(Position position: order.getPositions()){
            Map row = new HashMap();
            row.put("itemID",position.getItem().getId());
            row.put("title",position.getItem().getTitle());
            row.put("count",position.getCount());
            row.put("cost",position.getItem().getCost());
            row.put("sum",position.getItem().getCost()*position.getCount());
            result.add(row);
        }
        return result;
    }

    @GetMapping("get-current/{userID}")
    public Long getCurrent(@PathVariable Long userID){
        for (Orders order: this.ordersRepository.findByUserID(userID)){
            if (order.getStatus().equals("new"))
                return order.getId();
        }
        //Create new order, if we can't find exist new order
        Orders newOrder = new Orders(userID, "new");
        return this.ordersRepository.save(newOrder).getId();
    }

    @GetMapping("in-progress/{userID}")
    public List getInProgress(@PathVariable Long userID){
        List result = new ArrayList();
        Long id = 0L;
        for (Orders order: this.ordersRepository.findByUserID(userID)){
            if (order.getStatus() == "paid"){
                id = order.getId();
            }
        }
        if (id != 0L){
            Orders order = ordersRepository.findById(id).get();
            for(Position position: order.getPositions()){
                Map row = new HashMap();
                row.put("itemID",position.getItem().getId());
                row.put("title",position.getItem().getTitle());
                row.put("count",position.getCount());
                row.put("cost",position.getItem().getCost());
                row.put("sum",position.getItem().getCost()*position.getCount());
                result.add(row);
            }}
        return result;
    }

    @PostMapping
    public ResponseEntity createOrder(@RequestBody Orders orders) {
        return ResponseEntity.status(201).body(this.ordersRepository.save(orders));
    }

    @PostMapping("paid")
    public ResponseEntity toPaid(@RequestBody Orders order){
        this.ordersRepository.getReferenceById(order.getId()).setStatus("paid");
        this.ordersRepository.getReferenceById(order.getId()).setUserID(order.getUserID());
        if (order.getBaristaID() != null)
            this.ordersRepository.getReferenceById(order.getId()).setBaristaID(order.getBaristaID());
        return ResponseEntity.status(201).body(this.ordersRepository.save(this.ordersRepository.getReferenceById(order.getId())));
    }
}
