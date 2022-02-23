package com.followorkback.followorkback.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.followorkback.followorkback.entity.Role;
import com.followorkback.followorkback.entity.User;
import com.followorkback.followorkback.repository.RoleRepository;
import com.followorkback.followorkback.repository.UserRepository;
import com.followorkback.followorkback.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getusers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<List<User>>saveUser(@RequestBody DataFromUserForm datas) {


        log.info("Adding New user: {}", datas);

        User user = new User();
        ArrayList roles = datas.getRoles();
        String username = datas.getUsername();

        // Setting user infos
        user.setFirstName(datas.getFirstName());
        user.setLastName(datas.getLastName());
        user.setPassword(datas.getPassword());
        user.setEmail(datas.getEmail());
        user.setUsername(username);

        // Saving user
        try{
            userService.saveUser(user);

            // Adding roles
            roles.forEach((role) -> {

                try{
                    userService.saveRole(new Role(null,(String)role));
                }catch (Exception e){
                    log.info("Erreur de role : {]", e.getMessage());
                }

                try{
                    userService.addRoleToUser(username, (String)role);
                }catch (Exception e){
                    log.info("Erreur de role : {]", e.getMessage());
                }
            });

        }catch (Exception e){
            log.info("Error saving user : {]", e.getMessage());
        }

//        URI url = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());

//        return ResponseEntity.created(url).body(userService.saveUser(user));
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("user/delete")
    public ResponseEntity<List<User>>deleteUser(@RequestBody long id)
    {
        try{
            userService.deleteUserById(id);
        }catch (Exception e){
            return  ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(value = "/usersByRole/{userRole}")
    public List<User> getUser(@PathVariable String userRole){
        log.info("Requesting user list by role");
        Role role = roleRepository.findByName(userRole);
//        Set<Role> role_set = new HashSet<Role>(Arrays.asList(role));
        return userRepository.findByRolesIsContaining(role);
    }


    @PostMapping("/user/edit")
    public ResponseEntity<List<User>>editUser(@RequestBody DataFromUserFormEdit datas){
        log.info("Editing New user: {}", datas);
        String username = datas.getUsernameEdit();
        ArrayList roles = datas.getRolesEdit();

        try{
            User user = userService.getUser(datas.getUsernameEdit());

            // Setting user infos
            user.setFirstName(datas.getFirstNameEdit());
            user.setLastName(datas.getLastNameEdit());
            user.setEmail(datas.getEmailEdit());
            Set<Role> rolesSet = new HashSet<>();
            user.setRoles(rolesSet);
            userService.updateUser(user);
            // Adding roles

            roles.forEach((role) -> {

                try{
                    userService.saveRole(new Role(null,(String)role));
                }catch (Exception e){
                    log.info("Erreur de role : {]", e.getMessage());
                }

                try{
                    userService.addRoleToUser(username, (String)role);
                }catch (Exception e){
                    log.info("Erreur de role : {]", e.getMessage());
                }
            });

        }catch (Exception e){
            return  ResponseEntity.status(500).body(new ArrayList<>());
        }

        return ResponseEntity.ok().body(userService.getUsers());
    }


    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role) {

        URI url = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        return ResponseEntity.created(url).body(userService.saveRole(role));
    }

    @PostMapping("/role/addTouser")
    public ResponseEntity<?>addRole(@RequestBody RoleToUserForm form) {
//        URI url = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        userService.addRoleToUser(form.getUsername(), form.getRolename());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorisationHeader = request.getHeader(AUTHORIZATION);

        if(authorisationHeader != null && authorisationHeader.startsWith("Bearer ")){

            log.info("doing authorization stuff");

            try{
                String refresh_token = authorisationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secred".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
//                    response.sendError(HttpStatus.FORBIDDEN.value());
//                    filterChain.doFilter(request, response);
                Map<String, String> error = new HashMap<>();
                error.put("error", exception.getMessage());
//                    error.put("error", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        }else {
            throw new RuntimeException("Refresh Token is missing");
        }

    }

}

@Data
class RoleToUserForm {
    private String username;
    private String rolename;
}

@Data
class DataFromUserForm{
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList roles;
}

@Data
class DataFromUserFormEdit{
    private String usernameEdit;
    private String emailEdit;
    private String firstNameEdit;
    private String lastNameEdit;
    private ArrayList rolesEdit;
}
