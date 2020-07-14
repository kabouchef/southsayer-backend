package fr.personnel.southsayerbackend.service.user;


import fr.personnel.exceptions.handling.WebClientError.NotFoundException;
import fr.personnel.southsayerbackend.configuration.message.NotFoundMessage;
import fr.personnel.southsayerdatabase.entity.user.User;
import fr.personnel.southsayerdatabase.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param users : List of Users
     * @return {@link List<User>}
     */
    public List<User> save(List<User> users) {
        return users.stream().map(this.userRepository::save).collect(Collectors.toList());
    }

    /**
     * Delete a Users
     *
     * @param ldap : LDAP
     */
    @Transactional
    public void deleteByIdUser(List<String> ldap) {
        this.userRepository.deleteByIdUser(ldap);
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

