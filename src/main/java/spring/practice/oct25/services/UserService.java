package spring.practice.oct25.services;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.repos.UserRepo;
import spring.practice.oct25.securityservices.UserDetailsServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);



    private  static  final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public UserService() {

    }
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;

    }

    public UserRepo getUserRepo() {
        return userRepo;
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<User> getAllUsers(){

        return userRepo.findAll();

    }

    public Optional<User> getUserByUserName(String userName ) {
        log.info("Starting getUserByUserName process...{}", userName);
        return userRepo.findByUserName(userName);
    }

    public void saveUser(User userObj) throws Exception {
        log.info("Starting saveUser process...{}", userObj);
        userRepo.save(userObj);

    }
    public boolean saveUserEncodePassword(User userObj) throws Exception {
       try {
           log.info("Starting saveNewUser process...{}", userObj);
           userObj.setPassword( PASSWORD_ENCODER.encode(userObj.getPassword()));
           log.info("after encoding saveNewUser {}", userObj);
           userObj.setRoles(Arrays.asList("USER"));
           userRepo.save(userObj);
           return true;
       }catch (Exception e){
         //  System.out.println(e.getMessage());
           LOGGER.info("\n=============logger.info================\n {}",userObj);
           LOGGER.warn("\n=============logger.warn================\n",e.getCause());
           LOGGER.error("\n================logger.error for user {} ================\n", userObj.getUserName() , e.getMessage(), e);
           LOGGER.debug("\n===============logger.debug================\n: {}",e.getMessage());
           LOGGER.trace("\n================logger.trace================\n: {}",e.getMessage());
           return false;
       }

    }
    public void saveAdminUserEncodePassword(User userObj) throws Exception {
        log.info("Starting saveNewUser process...{}", userObj);
        userObj.setPassword( PASSWORD_ENCODER.encode(userObj.getPassword()));
        log.info("after encoding saveNewUser {}", userObj);
        userObj.setRoles(Arrays.asList("ADMIN","USER"));
        userRepo.save(userObj);

    }

    public void updateUserByUserName(String userName , User UserObj) throws Exception {
        log.info("Starting updateUserById process...{} ", UserObj);
        if(!userName.equals(UserObj.getUserName())){
            throw new Exception("Username not matching in authentication user and body user");
        }
        User existingUser=userRepo.findByUserName(userName).orElse(null);

        if(existingUser == null){
            throw new Exception("User not found userName:"+ userName);
        }else{
            // existingUser.setUserName(UserObj.getUserName() != null && !UserObj.getUserName().isEmpty() ? UserObj.getUserName() : UserObj.getUserName());
            existingUser.setPassword(UserObj.getPassword() != null && !UserObj.getPassword().isEmpty() ? UserObj.getPassword() : UserObj.getPassword());
           // existingUser.setJournalEntryV2List(UserObj.getJournalEntryV2List() != null && !UserObj.getJournalEntryV2List().isEmpty() ? UserObj.getJournalEntryV2List() : UserObj.getJournalEntryV2List());
        }

        saveUserEncodePassword(existingUser);

    }

    public void deleteUser(ObjectId entryId) throws Exception {
        log.info("Starting deleteUser process...{}",entryId);

        User je=userRepo.findById(entryId).orElseGet(null);

        if(je == null){
            throw new Exception("User not found");
        }
        userRepo.deleteById(entryId);
    }
    public void deleteUserByUsername(String userName) throws Exception {
        log.info("Starting deleteUserByUsername process...{}",userName);

       /* User je=userRepo.findByUserName(userName).orElse(null);

        if(je == null){
            throw new Exception("User not found");
        }
        userRepo.deleteById(je.getId());*/

        userRepo.deleteByUserName(userName);
    }

    public void deleteAll()  {
        log.info("Starting deleteAll process...{}");
        userRepo.deleteAll();


    }
}
