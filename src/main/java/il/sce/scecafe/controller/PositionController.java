package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Position;
import il.sce.scecafe.repository.ItemRepository;
import il.sce.scecafe.repository.PositionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = {"*"}, maxAge = 2400, allowCredentials = "false")
@RequestMapping("/position")
public class PositionController {
    private final ItemRepository itemRepository;
    private final PositionRepository positionRepository;

    public PositionController(ItemRepository itemRepository, PositionRepository positionRepository) {
        this.itemRepository = itemRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.positionRepository.findAll());
    }

    @PostMapping
    public ResponseEntity createItem(@RequestBody Position position) {
        return ResponseEntity.status(201).body(this.positionRepository.save(position));
    }
    @PostMapping("/add/{orderID}/{itemID}")
    public ResponseEntity addItem(@PathVariable Long orderID, @PathVariable Long itemID) {
        for(Position position:this.positionRepository.findAll()){
            if (Objects.equals(position.getOrdID(),orderID)){
                    if (Objects.equals(position.getItem().getId(),itemID)) {
                        positionRepository.getReferenceById(position.getId()).setCount(position.getCount()+1);
                        positionRepository.save(positionRepository.getReferenceById(position.getId()));
                        return ResponseEntity.status(201).body("Item added");
                    }
            }
        }

        Position newPosition = new Position(orderID,1,itemRepository.getReferenceById(itemID));
        positionRepository.save(newPosition);
        return ResponseEntity.status(201).body("Item added");
    }

    @PostMapping("/remove/{orderID}/{itemID}")
    public ResponseEntity removeItem(@PathVariable Long orderID, @PathVariable Long itemID) {
        for(Position position:this.positionRepository.findAll()){
            if (Objects.equals(position.getOrdID(),orderID)){
                if (Objects.equals(position.getItem().getId(),itemID)) {
                    if (Objects.equals(position.getCount(),1))
                    {
                        positionRepository.deleteById(position.getId());
                    } else {
                    positionRepository.getReferenceById(position.getId()).setCount(position.getCount()-1);
                    positionRepository.save(positionRepository.getReferenceById(position.getId()));}
                    return ResponseEntity.status(201).body("Item removed");
                }
            }
        }

        Position newPosition = new Position(orderID,1,itemRepository.getReferenceById(itemID));
        positionRepository.save(newPosition);
        return ResponseEntity.status(201).body("Item added");
    }

    @PostMapping("/full-remove/{orderID}/{itemID}")
    public ResponseEntity removeItemFull(@PathVariable Long orderID, @PathVariable Long itemID) {
        for(Position position:this.positionRepository.findAll()){
            if (Objects.equals(position.getOrdID(),orderID)){
                if (Objects.equals(position.getItem().getId(),itemID)) {
                        positionRepository.deleteById(position.getId());
                    return ResponseEntity.status(201).body("Item removed");
                }
            }
        }

        Position newPosition = new Position(orderID,1,itemRepository.getReferenceById(itemID));
        positionRepository.save(newPosition);
        return ResponseEntity.status(201).body("Item added");
    }

    @PostMapping("/change/{orderID}/{itemID}/{value}")
    public ResponseEntity changeItem(@PathVariable Long orderID, @PathVariable Long itemID, @PathVariable int value) {
        for(Position position:this.positionRepository.findAll()){
            if (Objects.equals(position.getOrdID(),orderID)){
                if (Objects.equals(position.getItem().getId(),itemID)) {
                    positionRepository.getReferenceById(position.getId()).setCount(value);
                    positionRepository.save(positionRepository.getReferenceById(position.getId()));}
                }
            }
        return ResponseEntity.status(201).body("Item changed");
    }

}
