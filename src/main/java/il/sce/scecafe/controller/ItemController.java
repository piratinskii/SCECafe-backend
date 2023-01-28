package il.sce.scecafe.controller;

import il.sce.scecafe.entity.Item;
import il.sce.scecafe.repository.ItemRepository;
import il.sce.scecafe.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;
    private final ItemRepository itemRepository;
    private final PositionRepository positionRepository;
    public ItemController(ItemRepository itemRepository, PositionRepository positionRepository) {
        this.itemRepository = itemRepository;
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public ResponseEntity getAllItems(){
        return ResponseEntity.ok(this.itemRepository.findAll());
    }

    private List<Item> OrderByName(List<Item> items){
        items.sort(((o1, o2) -> o1.getTitle().compareTo(o2.getTitle())));
        return items;
    }

    private List<Item> OrderByCost(List<Item> items){
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Double.compare(o1.getCost(), o2.getCost());
            }
        });
        return items;
    }

    private List<Item> OrderByPopularity(List<Item> items){
        Collections.sort(items, (o1, o2) -> {
            if (positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())) == null &&  positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())) == null) return 0;
            if (positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())) == null) return -1;
            if (positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())) == null) return 1;
            return Integer.compare(positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())).size(), positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())).size());
        });
        return items;
    }

    private List<Item> OrderByPopularityReverse(List<Item> items){
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())) == null && positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())) == null)
                    return 0;
                if (positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())) == null) return -1;
                if (positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())) == null) return 1;
                return Integer.compare(positionRepository.findByItem(itemRepository.getReferenceById(o2.getId())).size(), positionRepository.findByItem(itemRepository.getReferenceById(o1.getId())).size());
            }
        });
        return items;
    }

    private List<Item> OrderByCostReverse(List<Item> items){
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return Double.compare(o2.getCost(), o1.getCost());
            }
        });
        return items;
    }
    private List<Item> OrderByNameReverse(List<Item> items){
        Collections.sort(items, (o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
        return items;
    }


    @GetMapping("/orderBy/{orderType}")
    public ResponseEntity getOrderBy(@PathVariable String orderType){
        if (orderType.equals("byTitle")) {
            return ResponseEntity.ok(OrderByName(this.itemRepository.findAll()));
        } else
            if (orderType.equals("byTitleReverse")){
                return ResponseEntity.ok(OrderByNameReverse(this.itemRepository.findAll()));
            } else
                if (orderType.equals("byCost")) {
                    return ResponseEntity.ok(OrderByCost(this.itemRepository.findAll()));
                } else
                    if (orderType.equals("byCostReverse")) {
                        return ResponseEntity.ok(OrderByCostReverse(this.itemRepository.findAll()));
                    } else
                        if (orderType.equals("byPopularity")) {
                            return ResponseEntity.ok(OrderByPopularity(this.itemRepository.findAll()));
                        } else
                            if (orderType.equals("byPopularityReverse")) {
                                return ResponseEntity.ok(OrderByPopularityReverse(this.itemRepository.findAll()));
                            }
        return ResponseEntity.ok(this.itemRepository.findAll());
    }

    @PostMapping
    public ResponseEntity createItem() {
        Item item = new Item();
        return ResponseEntity.status(201).body(this.itemRepository.save(item));
    }

    @PostMapping("images/upload")
    public ResponseEntity fileUpload(@RequestParam("File")MultipartFile file, @RequestParam("id")Long id) throws IOException {
        File myFile = new File(FILE_DIRECTORY + id + ".jpg");
        myFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(myFile);
        fos.write(file.getBytes());
        fos.close();
        return ResponseEntity.status(201).body("OK");
    }

    private boolean checkFile(String filename){
        File file = new File (filename);
        return file.exists();
    }
    @GetMapping(value = "images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> file(@PathVariable Long id) throws IOException {
        String filepath = FILE_DIRECTORY + "placeholder.jpg";
        MediaType contentType = MediaType.IMAGE_JPEG;
        if (checkFile(FILE_DIRECTORY+id+".jpg"))
            filepath=FILE_DIRECTORY+id+".jpg";
        File file = new File(filepath);
        InputStream in = new FileInputStream(file);
        return ResponseEntity.ok()
                .contentType(contentType)
                .body(new InputStreamResource(in));
    }

    @PostMapping("edit")
    public ResponseEntity editItem(@RequestParam Long id, @RequestParam String title, @RequestParam String desc, @RequestParam double cost){
        this.itemRepository.findById(id).get().setTitle(title); //SELECT * FROM USERS WHERE ID = id
        this.itemRepository.findById(id).get().setDescription(desc);
        this.itemRepository.findById(id).get().setCost(cost);
        return ResponseEntity.status(201).body(this.itemRepository.save(this.itemRepository.getReferenceById(id)));
    }

    @PostMapping("remove")
    public ResponseEntity removeItem(@RequestParam Long id){
        this.itemRepository.deleteById(id);
        return ResponseEntity.status(201).body("Item #" + id + " deleted successful");
    }

}
