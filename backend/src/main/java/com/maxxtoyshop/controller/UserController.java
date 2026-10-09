package com.maxxtoyshop.controller;
import com.maxxtoyshop.dto.RegisterRequest; import com.maxxtoyshop.model.*; import com.maxxtoyshop.repository.UserRepository; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/users") @RequiredArgsConstructor public class UserController {
 private final UserRepository repo; private final PasswordEncoder encoder;
 @PostMapping("/register") public User register(@Valid @RequestBody RegisterRequest r){
  if(repo.existsByEmail(r.getEmail()))throw new IllegalArgumentException("Email is already registered");
  User u=new User();u.setFirstName(r.getFirstName());u.setLastName(r.getLastName());u.setEmail(r.getEmail().toLowerCase().trim());u.setPassword(encoder.encode(r.getPassword()));u.setPhone(r.getPhone());u.setRole(Role.CUSTOMER);return repo.save(u);
 }
}