package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Item;
import il.sce.scecafe.entity.Orders;
import il.sce.scecafe.entity.Position;
import il.sce.scecafe.repository.ItemRepository;
import il.sce.scecafe.repository.OrdersRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 2400, allowCredentials = "false")
@RequestMapping("/orders")
public class OrdersController {
    private EntityManager entityManager;

    private final OrdersRepository ordersRepository;

    public OrdersController(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @GetMapping("/")
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.ordersRepository.findAll());
    }

    @GetMapping("{id}")
    public List getOrder(@PathVariable Long id){
        List result = new ArrayList();
        Orders order = ordersRepository.findById(id).get();
        int i = 0;
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
        for (Orders order:this.ordersRepository.findAll()){
            if (order.getUserID() == userID)
                if (order.getStatus().equals("new"))
                    return order.getId();
        }
        //Create new order, if we can't find exist new order
        Orders newOrder = new Orders(userID, "new");
        System.out.println(newOrder);
        return this.ordersRepository.save(newOrder).getId();
    }

    @GetMapping("in-progress/{userID}")
    public List getInProgress(@PathVariable Long userID){
        List result = new ArrayList();
        Long id = 0L;
        for (Orders order: ordersRepository.findAll()){
            if (order.getUserID().equals(userID)){
                if (order.getStatus().equals("paid")){
                    id = order.getId();
                }
            }
        }
        if (id != 0L){
        Orders order = ordersRepository.findById(id).get();
        int i = 0;
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
    public ResponseEntity createItem(@RequestBody Orders orders) {
        return ResponseEntity.status(201).body(this.ordersRepository.save(orders));
    }

    @PostMapping("paid/{orderID}")
    public ResponseEntity toPaid(@PathVariable Long orderID){
        this.ordersRepository.getReferenceById(orderID).setStatus("paid");
        return ResponseEntity.status(201).body(this.ordersRepository.save(this.ordersRepository.getReferenceById(orderID)));
    }
}
