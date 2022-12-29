package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Tables;
import il.sce.scecafe.repository.OrdersRepository;
import il.sce.scecafe.repository.TablesRepository;
import jakarta.persistence.EntityManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 2400, allowCredentials = "false")
@RequestMapping("/tables")
public class TablesController {
    private EntityManager entityManager;

    private final TablesRepository tablesRepository;
    private final OrdersRepository ordersRepository;
    public TablesController(TablesRepository tablesRepository, OrdersRepository ordersRepository) {
        this.tablesRepository = tablesRepository;
        this.ordersRepository = ordersRepository;
    }
    @GetMapping("/")
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.tablesRepository.findAll());
    }

    @GetMapping("/count")
    public Long getCount(){
        return tablesRepository.count();
    }

    @GetMapping("/busy/{table}")
    public boolean isBusy(@PathVariable Long table){
        if (tablesRepository.getReferenceById(table).getOrder() != null){
            return true;
        }
        return false;
    }

    @PostMapping("/booking/{table}/{ordNum}")
    public String booking(@PathVariable Long table, @PathVariable Long ordNum){
        if (table <= 0 || table > getCount()){
            return "This table does not exist";
        }
        if(isBusy(table)){
            return "Sorry, this table is busy";
        }
        tablesRepository.getReferenceById(table).setOrder(ordersRepository.getReferenceById(ordNum));
        tablesRepository.save(tablesRepository.getReferenceById(table));
        return "ok";
    }

    @PostMapping("/add")
    public ResponseEntity add(){
        Tables table = new Tables();
        tablesRepository.save(table);
        return ResponseEntity.ok().build();
    }
}
