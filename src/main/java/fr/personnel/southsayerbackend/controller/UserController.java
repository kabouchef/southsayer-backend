package fr.personnel.southsayerbackend.controller;

import fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils;
import fr.personnel.southsayerbackend.service.UserService;
import fr.personnel.southsayerdatabase.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api("API to extract OAP simulation prices")
@RestController
@RequestMapping(UserController.PATH)
@RequiredArgsConstructor
public class UserController {

    public final static String PATH = RestConstantUtils.DEFAULT_PATH + "/user";

    private final UserService userService;

    /**
     * Add User
     *
     * @param user : user to add
     * @return {@link User}
     */
    @ApiOperation(value = "Add a user")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "User added"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User addUser(@RequestBody final User user) {
        return this.userService.save(user);
    }

    /**
     * GET all User
     *
     * @return {@link Iterable<User>}
     */
    @ApiOperation(value = "GET all users")
    @GetMapping
    public Iterable<User> getAllUser() {
        return this.userService.findAll();
    }

    /**
     * GET a User
     *
     * @param ldap : LDAP
     * @return {@link User}
     */
    @ApiOperation(value = "GET one user")
    @GetMapping("{ldap}")
    public User getUser(@PathVariable final String ldap) {
        return this.userService.findOne(ldap);
    }

    /**
     * Delete a user
     *
     * @param ldap : LDAP
     */
    @ApiOperation(value = "Delete a User")
    @DeleteMapping("{ldap}")
    public void deleteUser(@PathVariable final String ldap) {
        this.userService.deleteOne(ldap);
    }


}
