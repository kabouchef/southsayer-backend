package fr.personnel.southsayerbackend.controller.user;

import fr.personnel.southsayerbackend.service.user.UserService;
import fr.personnel.southsayerdatabase.entity.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

import static fr.personnel.southsayerbackend.configuration.constant.RestConstantUtils.USER_PATH;

/**
 * @author Farouk KABOUCHE
 * API to manage user OAP
 * @version 1.0
 * @see UserService
 */
@Slf4j
@Api("API to manage user OAP")
@RestController
@RequestMapping(USER_PATH)
@Data
@Transactional
public class UserController {

    private final UserService userService;

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
     * Add User
     *
     * @param users : users to add
     * @return {@link User}
     */
    @ApiOperation(value = "Add a user")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Added"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 500, message = "Technical error happened")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public List<User> addUser(@RequestBody final List<User> users) {
        return this.userService.save(users);
    }

    /**
     * Delete a user
     * @param ldap : ldap
     */
    @ApiOperation(value = "Delete Users")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Deleted")})
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestBody final String ldap) {
        this.userService.deleteByIdUser(ldap);
    }


}
