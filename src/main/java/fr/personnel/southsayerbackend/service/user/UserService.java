package fr.personnel.southsayerbackend.service.user;


import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerdatabase.entity.user.User;
import fr.personnel.southsayerdatabase.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Farouk KABOUCHE
 * <p>
 * User Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final NotFoundMessage notFoundMessage;

    /**
     * Save User
     *
     * @param user : User
     * @return {@link User}
     */
    public User save(User user) {
        return this.userRepository.save(user);
    }

    /**
     * Delete a User
     *
     * @param ldap : LDAP
     */
    @Transactional
    public void deleteOne(String ldap) {
        this.userRepository.deleteUserByIdUser(ldap);
    }

    /**
     * Find One User
     *
     * @param ldap : LDAP
     * @return {@link User}
     */
    public User findOne(String ldap) {
        Optional<User> user = this.userRepository.findByIdUser(ldap);

        if (!user.isPresent()) throw new NotFoundException(this.notFoundMessage.toString(ldap));

        return user.get();

    }

    /**
     * Find All User
     *
     * @return {@link Iterable <User>}
     */

    public Iterable<User> findAll() {
        return this.userRepository.findAll();
    }
}

