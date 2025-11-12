package spring.practice.oct25.services;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.repos.UserRepo;

import java.util.Arrays;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @BeforeAll
    public static void beforeAll(){
        System.out.printf("################### All Testing starts ###################");
    }

    @AfterAll
    public static void afterAll(){
        System.out.printf("################### All Testing end ###################");
    }
     static int count=0;
    @BeforeEach
    public  void beforeEach(){
        count++;
        System.out.printf("~~~~~~~~~~~~~~~ Test No :"+count +"~~~~~~~~~~~~~~~~\n");
    }
    @AfterEach
    public void afterEach(){
        System.out.printf("~~~~~~~~~~~~~~~ end of Test ~~~~~~~~~~~~~~~\n");
    }

    @Test
    @Disabled
    public void testAdd() {
        Assertions.assertEquals(4, 2 + 2);
    }

    @Test
    public void testgetExistUserByUserName() {
        Assertions.assertNotNull(userRepo.findByUserName("ram"));
        Assertions.assertNotNull(userRepo.findByUserName("Ram").get().getRoles());
        Assertions.assertEquals(Arrays.asList("USER"),userRepo.findByUserName("Ram").get().getRoles());

    }

    @ParameterizedTest
    @CsvSource({
            "Ram",
            "Ali",
            "roop",
            "tapuni"
    })
    public void testParameterisedExistUserByUserName(String username) {
        Assertions.assertNotNull(userRepo.findByUserName(username).get().getRoles());
       // Assertions.assertEquals(Arrays.asList("USER"),userRepo.findByUserName(username).get().getRoles(),"failed findByUserName "+username);
        Assertions.assertTrue(
                userRepo.findByUserName(username).get().getRoles().contains("USER"),
                "failed findByUserName " + username + " — role USER not found"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Ram",
            "Ali"
    })
    public void testParameterised2ExistUserByUserName(String username) {
        Assertions.assertNotNull(userRepo.findByUserName(username).get().getRoles());
        //Assertions.assertEquals(Arrays.asList("USER"),userRepo.findByUserName(username).get().getRoles(),"failed findByUserName "+username);
        Assertions.assertTrue(
                userRepo.findByUserName(username).get().getRoles().contains("USER"),
                "failed findByUserName " + username + " — role USER not found"
        );
    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentProvider.class)
    public void testParameterisedSaveUserService(User user) throws Exception {
        Assertions.assertTrue(userService.saveUserEncodePassword(user), "failed to saveUserEncodePassword "+user.getUserName());
    }

    @Test
    public void testgetNonExistUserByUserName() {
       // Optional<User> user = userRepo.findByUserName("Arun");
       // System.out.printf("user"+user.isPresent());
        Assertions.assertNull(userRepo.findByUserName("Arun").orElse(null));
    }
    @Test
    public void testgetExistUserJournalSize() {
        // Optional<User> user = userRepo.findByUserName("Arun");
        // System.out.printf("user"+user.isPresent());
        Assertions.assertTrue(userRepo.findByUserName("Ram").get().getJournalEntryV2List().size()>1);
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2,1,3",
            "10,10,20",
            "1,90,91"
    })
    public void testParameterised(int a , int b , int result) {
        Assertions.assertEquals(result, a + b);
    }
}
