package spring.practice.oct25.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.practice.oct25.entities.User;
import spring.practice.oct25.services.UserService;

import java.net.URI;
import java.util.List;

@RequestMapping("/admin-api")
@RestController
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    @GetMapping("/health-check")
    public String getUser(){
        return  "Ok";
    }

    @PostMapping("/create-user")
    public ResponseEntity createUser(@RequestBody User user ){
        URI location = null;
        try {
            boolean isSuccess= userService.saveUserEncodePassword(user);
            if (isSuccess){
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }else{
                return ResponseEntity.badRequest().body("User creation failed");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
    @PostMapping("/create-admin")
    public ResponseEntity createAdminUser(@RequestBody User user ){
        URI location = null;
        try {
            userService.saveAdminUserEncodePassword(user);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("/all-users")
    public ResponseEntity<List> getAllUser(){
        System.out.printf(" get all users ");
        List<User> users= userService.getAllUsers();
        if (users != null && !users.isEmpty()){
            return ResponseEntity.ok().body(users);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
