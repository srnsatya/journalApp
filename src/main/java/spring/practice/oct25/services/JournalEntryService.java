package spring.practice.oct25.services;

import org.springframework.stereotype.Service;
import spring.practice.oct25.entities.JournalEntry;

import java.util.*;

@Service
public class JournalEntryService {

    private Map<String, JournalEntry> journalEntryMap = new HashMap<>();

    public JournalEntryService() {
        this.journalEntryMap.put("1000"  ,new JournalEntry("1000" , "Veg lifestyle","Veg lifestyle"));
        this.journalEntryMap.put("2000"  ,new JournalEntry("2000" , "Non-Veg lifestyle","Non-Veg lifestyle"));
        this.journalEntryMap.put("3000"  ,new JournalEntry("3000" , "Non-Veg lifestyle","Childhood days"));
    }

    public List<JournalEntry> getAllJournals(){

        return new ArrayList<>(journalEntryMap.values());

    }

    public JournalEntry getJournalEntryById(String entryId) throws Exception {
        if(!journalEntryMap.containsKey(entryId)){
            throw new Exception("Journal Entry not found");
        }
        return journalEntryMap.get(entryId);

    }

    public JournalEntry addJournalEntry(JournalEntry journalEntry) throws Exception {
        if(journalEntry.getId() == null){
            journalEntry.setId(String.valueOf(new Random().nextLong()));
        }
         journalEntryMap.put(journalEntry.getId(),journalEntry);
        return journalEntry;
    }
    public JournalEntry updateJournalById(String entryId , JournalEntry journalEntry) throws Exception {
        if(!journalEntryMap.containsKey(entryId)){
            throw new Exception("Journal Entry not found");
        }
        if(journalEntry.getId() != null && !journalEntry.equals(entryId)){
            throw new Exception("Journal Entry Id not matching");
        }
        return journalEntry;
    }

    public void deleteJournalEntry(String entryId) throws Exception {

        if(!journalEntryMap.containsKey(entryId)){
            throw new Exception("Journal Entry not found");
        }
        journalEntryMap.remove(entryId);
    }

    public List<JournalEntry> resetJournalEntry()  {

        this.journalEntryMap = new HashMap<>();
        this.journalEntryMap.put("1000"  ,new JournalEntry("1000" , "Veg lifestyle","Veg lifestyle"));
        this.journalEntryMap.put("2000"  ,new JournalEntry("2000" , "Non-Veg lifestyle","Non-Veg lifestyle"));
        this.journalEntryMap.put("3000"  ,new JournalEntry("3000" , "Non-Veg lifestyle","Childhood days"));

        return new ArrayList<>(journalEntryMap.values());
    }
}
