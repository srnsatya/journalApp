package spring.practice.oct25.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import spring.practice.oct25.entities.JournalEntryV2;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.services.JournalEntryServiceV2;
import spring.practice.oct25.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/journalV3-api")
@RestController
public class JournalEntryControllerV3 {


    private JournalEntryServiceV2 journalEntryService;
    private UserService userService;

    public JournalEntryControllerV3(JournalEntryServiceV2 journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String getJournal(){
        return  "Ok";
    }

    @GetMapping("/all-journal")
    public ResponseEntity<List> getAllJournal(){
        List<JournalEntryV2> journalEntries= journalEntryService.getAllJournals();

       return ResponseEntity.ok().body(journalEntries);
    }

    @GetMapping("/journal/{id}")
    public ResponseEntity<?> getSingleJournal( @PathVariable("id") ObjectId journalId ){
        Optional<JournalEntryV2> journalEntryO= journalEntryService.getJournalEntryById(journalId);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> users= userService.getUserByUserName(authentication.getName());
        return users.get().getJournalEntryV2List().stream()
                .filter(journalEntryV2 -> journalEntryV2.getId().equals(journalId))
                .findFirst()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Journal not found for id: " + journalId +
                                " for username " + authentication.getName()));    }
    @GetMapping("/journal")
    public ResponseEntity getJournalsByUserName(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> users= userService.getUserByUserName(authentication.getName());
        return  users.<ResponseEntity<?>>map(user -> new ResponseEntity<>(user.getJournalEntryV2List(),HttpStatus.OK)).orElseGet(()->  ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Users not found with username: " + authentication.getName()));
    }

    @PostMapping("/journal")
    public ResponseEntity createJournalByUserName( @RequestBody JournalEntryV2 journalEntry ){

        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveJournalEntry(journalEntry, authentication.getName());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
    }

    @PutMapping("/journal/{id}")
    public ResponseEntity updateJournalByUserName( @PathVariable("id") ObjectId journalId ,@RequestBody JournalEntryV2 journalEntry ){

        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.updateJournalByUsername(authentication.getName() ,journalId,journalEntry);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal not found with ID: " + journalId);
        }

        return ResponseEntity.ok().body("updated successfully");
    }



    @DeleteMapping("/journal/{id}")
    public ResponseEntity deleteJournal( @PathVariable("id") ObjectId journalId ){

        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            journalEntryService.deleteJournalEntry(authentication.getName() ,journalId);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted successfully");
    }

}
