package com.jpmc.midascore.controller;

import com.jpmc.midascore.repository.UserRepository;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.service.KafkaProducerService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repo;
    private final KafkaProducerService producer;

    // Constructor injection for final fields
    public UserController(UserRepository repo, KafkaProducerService producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @GetMapping
    public List<UserRecord> getAll() {
        Iterable<UserRecord> iterable = repo.findAll();
        List<UserRecord> list = new ArrayList<>();
        iterable.forEach(list::add); // convert Iterable → List
        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRecord> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserRecord create(@RequestBody UserRecord user) {
        UserRecord saved = repo.save(user);
        producer.sendMessage("New user created: " + saved.getName());
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRecord> updateBalance(@PathVariable Long id, @RequestBody UserRecord userDetails) {
        return repo.findById(id)
                .map(user -> {
                    user.setBalance(userDetails.getBalance());
                    UserRecord updated = repo.save(user);
                    producer.sendMessage("User balance updated: " + updated.getName() +
                            ", new balance: " + updated.getBalance());
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id)
                .map(user -> {
                    repo.delete(user);
                    producer.sendMessage("User deleted: " + user.getName());
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
