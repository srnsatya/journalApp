package spring.practice.oct25.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.practice.oct25.entities.JournalEntryV2;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.services.JournalEntryServiceV2;
import spring.practice.oct25.services.UserService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequestMapping("/journalV2-api")
@RestController
public class JournalEntryControllerV2 {


    private JournalEntryServiceV2 journalEntryService;
    private UserService userService;

    public JournalEntryControllerV2(JournalEntryServiceV2 journalEntryService, UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    @GetMapping("/health-check")
    public String getJournal(){
        return  "Ok";
    }

    @GetMapping("/journal")
    public ResponseEntity<List> getAllJournal(){
        List<JournalEntryV2> journalEntries= journalEntryService.getAllJournals();

       return ResponseEntity.ok().body(journalEntries);
    }

    @GetMapping("/journal/{id}")
    public ResponseEntity getSingleJournal( @PathVariable("id") ObjectId journalId ){
        Optional<JournalEntryV2> journalEntryO= journalEntryService.getJournalEntryById(journalId);
        /*
               if (journalEntryO.isPresent()) {
                    return ResponseEntity.ok(journalEntryO.get());
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("User not found with ID: " + journalId);
                }



         */
       return  journalEntryO.<ResponseEntity<?>>map(journalEntryV2 -> new ResponseEntity<>(journalEntryV2,HttpStatus.OK)).orElseGet(()->  ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Journal not found with ID: " + journalId));
    }
    @GetMapping("/journal/username/{username}")
    public ResponseEntity getJournalsByUserName( @PathVariable("username") String username ){
        Optional<User> users= userService.getUserByUserName(username);
        return  users.<ResponseEntity<?>>map(user -> new ResponseEntity<>(user.getJournalEntryV2List(),HttpStatus.OK)).orElseGet(()->  ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("Users not found with username: " + username));
    }

    @PostMapping("/journal")
    public ResponseEntity createJournal(@RequestBody JournalEntryV2 journalEntry ){
        URI location = null;
        try {
            journalEntry.setDate(LocalDateTime.now());
             journalEntryService.saveJournalEntry(journalEntry);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
    }
    @PostMapping("/journal/username/{username}")
    public ResponseEntity createJournalByUserName( @PathVariable("username") String username ,@RequestBody JournalEntryV2 journalEntry ){

        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.saveJournalEntry(journalEntry, username);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
    }

    @PutMapping("/journal/{id}")
    public ResponseEntity updateJournalById(@PathVariable("id") ObjectId journalId ,@RequestBody JournalEntryV2 journalEntry ){

        try {
            journalEntry.setDate(LocalDateTime.now());
             journalEntryService.updateJournalById(journalId,journalEntry);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal not found with ID: " + journalId);
        }

        return ResponseEntity.ok().body("updated successfully");
    }

    @PutMapping("/journal/username/{username}/{id}")
    public ResponseEntity updateJournalByUserName( @PathVariable("username") String username , @PathVariable("id") ObjectId journalId ,@RequestBody JournalEntryV2 journalEntry ){

        try {
            journalEntry.setDate(LocalDateTime.now());
            journalEntryService.updateJournalByUsername(username ,journalId,journalEntry);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal not found with ID: " + journalId);
        }

        return ResponseEntity.ok().body("updated successfully");
    }



    @DeleteMapping("/journal/{id}")
    public ResponseEntity deleteJournal(@PathVariable("id") ObjectId journalId ){


        try {
             journalEntryService.deleteJournalEntry(journalId);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Journal not found with ID: " + journalId);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted successfully");
    }
    @DeleteMapping("/journal/username/{username}/{id}")
    public ResponseEntity deleteJournal(@PathVariable("username") String username  , @PathVariable("id") ObjectId journalId ){

        try {
            journalEntryService.deleteJournalEntry(username ,journalId);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted successfully");
    }
    @DeleteMapping("/journal/delete-all")
    public ResponseEntity resetAllJournal( ){
         journalEntryService.deleteAll();
        return ResponseEntity.ok().body("deleted all successfully");
    }

}
