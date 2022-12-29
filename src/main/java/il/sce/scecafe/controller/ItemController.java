package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Item;
import il.sce.scecafe.repository.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 2400, allowCredentials = "false")
@RequestMapping("/item")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.itemRepository.findAll());
    }

    @PostMapping
    public ResponseEntity createItem(@RequestBody Item item) {
        return ResponseEntity.status(201).body(this.itemRepository.save(item));
    }
}
