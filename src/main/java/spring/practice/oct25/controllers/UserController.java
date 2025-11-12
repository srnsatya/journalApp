package spring.practice.oct25.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.services.UserService;

import java.util.Optional;

@RequestMapping("/user-api")
@RestController
@AllArgsConstructor
public class UserController {


    private UserService userService;


    @GetMapping("/health-check")
    public String healthCheck(){
        return  "Ok";
    }

    @GetMapping("/user")
    public ResponseEntity getSingleUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userO= userService.getUserByUserName(authentication.getName());
        /*
               if (userO.isPresent()) {
                    return ResponseEntity.ok(userO.get());
                } else {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body("User not found with ID: " + userId);
                }



         */
       return  userO.<ResponseEntity<?>>map(userV2 -> new ResponseEntity<>(userV2,HttpStatus.OK)).orElseGet(()->  ResponseEntity
                .status(HttpStatus.NOT_FOUND).body("User not found with User Name: " + authentication.getName()));
    }

    @PutMapping("/user")
    public ResponseEntity updateUser(@RequestBody User user ){

        try {
         Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
         userService.updateUserByUserName(authentication.getName() ,user);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.ok().body("updated successfully");
    }


    @DeleteMapping("/user")
    public ResponseEntity deleteUser(){

        String userName = null;
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            userName = authentication.getName();
           userService.deleteUserByUsername(authentication.getName());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with userName: " +userName);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted successfully");
    }

}
