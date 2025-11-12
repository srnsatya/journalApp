package spring.practice.oct25.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.practice.oct25.entities.JournalEntryV2;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.repos.JournalEntryRepo;

import java.util.*;

@Service
@Slf4j
public class JournalEntryServiceV2 {

   // @Autowired
    private JournalEntryRepo journalEntryRepo;


    private UserService userService;


    public JournalEntryServiceV2(JournalEntryRepo journalEntryRepo, UserService userService) {
        this.journalEntryRepo = journalEntryRepo;
        this.userService = userService;
    }

    public JournalEntryRepo getJournalEntryV2Repo() {
        return journalEntryRepo;
    }

    public void setJournalEntryRepo(JournalEntryRepo journalEntryRepo) {
        this.journalEntryRepo = journalEntryRepo;
    }

    public List<JournalEntryV2> getAllJournals(){

        return journalEntryRepo.findAll();

    }

    public Optional<JournalEntryV2> getJournalEntryById(ObjectId entryId) {
        log.info("Starting getJournalEntryById process...{}", entryId);
        return journalEntryRepo.findById(entryId);
    }

    public void saveJournalEntry(JournalEntryV2 journalEntry) throws Exception {
        log.info("Starting saveJournalEntry process...{}", journalEntry);
        journalEntryRepo.save(journalEntry);

    }
    @Transactional
    public void saveJournalEntry(JournalEntryV2 journalEntry , String username) throws Exception {
        log.info("Starting saveJournalEntry process...{} in username {}", journalEntry, username);
        Optional<User> userO = userService.getUserByUserName(username);
        if(!userO.isPresent() ){
           throw new Exception("Users not found with username: " + username);
        }

        JournalEntryV2 createdJournalEntry = journalEntryRepo.save(journalEntry);
        User user = userO.get();
        user.getJournalEntryV2List().add(createdJournalEntry);
        // user.setUserName(null);
        userService.saveUser(user);

    }
    public void updateJournalById(ObjectId entryId , JournalEntryV2 journalEntry) throws Exception {
        log.info("Starting updateJournalById process...{} by {}", journalEntry,entryId);
        JournalEntryV2 je=journalEntryRepo.findById(entryId).orElseGet(null);

        if(je == null){
            throw new Exception("Journal Entry not found");
        }else{
            je.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : je.getContent());
            je.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : je.getTitle());
        }

        journalEntryRepo.save(je);

    }
    public void updateJournalByUsername( String username ,ObjectId entryId , JournalEntryV2 journalEntry) throws Exception {
        log.info("Starting updateJournalById process...{} by {} in username {}", journalEntry,entryId, username);
        Optional<User> userO = userService.getUserByUserName(username);
        if(!userO.isPresent() ){
            throw new Exception("Users not found with username: " + username);
        }
        JournalEntryV2 je=journalEntryRepo.findById(entryId).orElse(null);

        if(je == null){
            throw new Exception("Journal Entry not found");
        }
            je.setContent(journalEntry.getContent() != null && !journalEntry.getContent().isEmpty() ? journalEntry.getContent() : je.getContent());
            je.setTitle(journalEntry.getTitle() != null && !journalEntry.getTitle().isEmpty() ? journalEntry.getTitle() : je.getTitle());
            JournalEntryV2 updatedJE = journalEntryRepo.save(je);
            User user = userO.get();
            this.addIfAbsent(user.getJournalEntryV2List(), updatedJE);;
            userService.saveUser(user);
    }
    private void addIfAbsent(List<JournalEntryV2> list, JournalEntryV2 entry) {
        if (list.stream().noneMatch(e -> e.getId().equals(entry.getId()))) {
            list.add(entry);
        }
    }
    public void deleteJournalEntry(ObjectId entryId) throws Exception {
        log.info("Starting deleteJournalEntry process...{}",entryId);

        JournalEntryV2 je=journalEntryRepo.findById(entryId).orElseGet(null);

        if(je == null){
            throw new Exception("Journal Entry not found");
        }
        journalEntryRepo.deleteById(entryId);
    }
    public void deleteJournalEntry(String username , ObjectId entryId) throws Exception {
        log.info("Starting deleteJournalEntry process...usernmae: {} entryid: {}",username , entryId);
        Optional<User> userO = userService.getUserByUserName(username);
        if(!userO.isPresent() ){
            throw new Exception("Users not found with username: " + username);
        }
        User user = userO.get();
        boolean isRelated = user.getJournalEntryV2List().stream().anyMatch(journalEntryV2 -> journalEntryV2.getId().equals(entryId));
        if(! isRelated ){
            throw new Exception("Journal & User not related");
        }
        JournalEntryV2 je=journalEntryRepo.findById(entryId).orElse(null);
        if(je == null){
            throw new Exception("Journal Entry not found");
        }
        user.getJournalEntryV2List().removeIf(journalEntryV2 -> journalEntryV2.getId().equals(je.getId()));
        userService.saveUser(user);
        journalEntryRepo.deleteById(entryId);
    }

    public void deleteAll()  {
        log.info("Starting deleteAll process...{}");
        journalEntryRepo.deleteAll();


    }
}
