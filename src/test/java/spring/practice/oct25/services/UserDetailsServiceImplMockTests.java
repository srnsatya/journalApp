package spring.practice.oct25.services;
import org.junit.jupiter.api.*;

import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.repos.UserRepo;
import spring.practice.oct25.securityservices.UserDetailsServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;


/* Option -1 // Here spring coxtext will not be loaded as @SpringBootTest && @Autowired is not here*/
/*//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplMockTests {

    //@Autowired
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    //@MockBean
    private UserRepo userRepo;

    @Test
    public void loadUserByUsername(){
        when(userRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(User.builder().userName("ram").password("ram").roles(Arrays.asList("USER")).build()));

        UserDetails userDetails= userDetailsService.loadUserByUsername("ram");
        System.out.printf("userDetails:"+userDetails);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("ram",userDetails.getUsername());
    }
}
*/
/* Option -2 // Here spring coxtext will be loaded as @SpringBootTest && @Autowired is here
@SpringBootTest
public class UserDetailsServiceImplMockTests {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserRepo userRepo;

    @Test
    public void loadUserByUsername(){
        when(userRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(User.builder().userName("ram").password("ram").roles(Arrays.asList("USER")).build()));

        UserDetails userDetails= userDetailsService.loadUserByUsername("ram");
        System.out.printf("userDetails:"+userDetails);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("ram",userDetails.getUsername());
    }
}

*/

/* Option -3 // Here spring use beforeAll to all mocked object @Mock*/
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplMockTests {

    //@Autowired
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    //@MockBean
    private UserRepo userRepo;

    @BeforeEach
    public  void beforeAll(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserByUsername(){
        when(userRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(Optional.ofNullable(User.builder().userName("ram").password("ram").roles(Arrays.asList("USER")).build()));

        UserDetails userDetails= userDetailsService.loadUserByUsername("ram");
        System.out.printf("userDetails:"+userDetails);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("ram",userDetails.getUsername());
    }
}