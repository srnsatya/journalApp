package spring.practice.oct25.securityservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import spring.practice.oct25.repos.UserRepo;
import spring.practice.oct25.entities.User;
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepo userRepo;
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);



    public UserDetailsServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsServiceImpl loadUserByUsername "+username);
        User user = userRepo.findByUserName(username).orElse(null);
        log.info("UserDetailsServiceImpl user "+user);
        if (user != null){
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .builder().username(user.getUserName())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            return userDetails;
        }else{
            throw new UsernameNotFoundException("user not found");
        }
    }
}
