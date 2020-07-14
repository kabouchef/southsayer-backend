package fr.personnel.southsayerbackend.user;

import com.google.common.collect.Lists;
import fr.personnel.southsayerbackend.service.activitycode.ActivityCodeService;
import fr.personnel.southsayerbackend.service.user.UserService;
import fr.personnel.southsayerdatabase.entity.activitycode.ActivityCode;
import fr.personnel.southsayerdatabase.entity.user.User;
import fr.personnel.southsayerdatabase.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * @author Farouk KABOUCHE
 */
@Tag("User Service Tests")
@DisplayName("Activity Code Service Tests")
public class UserTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    String idUser = "10001000";
    String idUser2nd = "10001001";

    /**
     * {@link UserService#findAll()}
     */
    @Test
    @Tag("GetAllUserServiceTests")
    @DisplayName("Get all users")
    public void getAll_Users_Test() {
        when(userRepository.findAll()).thenReturn(createListUsers());

        Iterable<User> users = userService.findAll();

        verify(userRepository, times(1)).findAll();
        assertNotNull(users);
    }

    /**
     * {@link UserService#findOne(String)}
     */
    @Test
    @Tag("GetAllUserServiceTests")
    @DisplayName("Get all users")
    public void getOne_User_Test() {
        when(userRepository.findByIdUser(anyString())).thenReturn(Optional.of(createUser(idUser)));

        User users = userService.findOne(anyString());

        verify(userRepository, times(1)).findByIdUser(anyString());
        assertThat(idUser).isEqualTo(users.getIdUser());
        assertNotNull(users);
    }

    /**
     * {@link UserService#save(List)}
     */
    @Test
    @Tag("AddActivityCodeServiceTests")
    @DisplayName("Add Users")
    public void add_User_Test() {
        when(userRepository.save(any(User.class))).thenReturn(createUser(idUser));

        List<User> users = userService.save(createListUsers());

        verify(userRepository, times(2)).save(any(User.class));
        assertNotNull(users);
    }

    /**
     * {@link ActivityCodeService#getByIdOAP(String)}
     */
    @Test
    @Tag("DeleteUsersServiceTests")
    @DisplayName("Delete Users")
    public void delete_Users_ByIdUser_Test() {
        doNothing().when(userRepository).deleteByIdUser(Lists.newArrayList(anyString()));
        List<String> strings = new ArrayList<>();
        strings.add(idUser);
        strings.add(idUser2nd);

        userService.deleteByIdUser(strings);

        verify(userRepository, times(1)).deleteByIdUser(strings);
    }


    private User createUser(String id) {
        return new User(id, "lastname", "firstname", "ADMINISTRATEUR");

    }

    private List<User> createListUsers() {
        return Lists.newArrayList(createUser("10001000"), createUser("10001001"));
    }


}
