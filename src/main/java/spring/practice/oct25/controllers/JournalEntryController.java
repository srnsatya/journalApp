package spring.practice.oct25.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import spring.practice.oct25.entities.JournalEntry;
import spring.practice.oct25.services.JournalEntryService;

import java.net.URI;
import java.util.List;

@RequestMapping("/journal-api")
@RestController
public class JournalEntryController {


    private JournalEntryService journalEntryService;

    public JournalEntryController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @GetMapping("/health-check")
    public String getJournal(){
        return  "Ok";
    }

    @GetMapping("/journal")
    public ResponseEntity<List> getAllJournal(){
        List<JournalEntry> journalEntries= journalEntryService.getAllJournals();

       return ResponseEntity.ok().body(journalEntries);
    }

    @GetMapping("/journal/{id}")
    public ResponseEntity getSingleJournal( @PathVariable("id") String journalId ){
        JournalEntry journalEntry= null;
        try {
            journalEntry = journalEntryService.getJournalEntryById(journalId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body(journalEntry);
    }

    @PostMapping("/journal")
    public ResponseEntity getJournal(@RequestBody JournalEntry journalEntry ){
        JournalEntry savedJournalEntry= null;
        URI location = null;
        try {
            savedJournalEntry = journalEntryService.addJournalEntry(journalEntry);
             location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedJournalEntry.getId())
                    .toUri();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.created(location).body(savedJournalEntry);
    }

    @PutMapping("/journal/{id}")
    public ResponseEntity updateJournalById(@PathVariable("id") String journalId ,@RequestBody JournalEntry journalEntry ){
        JournalEntry updatedJournalEntry= null;
        URI location = null;
        try {
            updatedJournalEntry = journalEntryService.updateJournalById(journalId,journalEntry);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body(updatedJournalEntry);
    }


    @DeleteMapping("/journal/{id}")
    public ResponseEntity deleteJournal(@PathVariable("id") String journalId ){


        try {
             journalEntryService.deleteJournalEntry(journalId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().body("deleted successfully");
    }
    @PutMapping("/journal/reset")
    public ResponseEntity resetAllJournal( ){


        List<JournalEntry> journalEntries= journalEntryService.resetJournalEntry();

        return ResponseEntity.ok().body(journalEntries);
    }

}
