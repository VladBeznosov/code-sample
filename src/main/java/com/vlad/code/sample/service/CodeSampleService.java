package com.vlad.code.sample.service;

import com.vlad.code.sample.exception.DuplicateUserException;
import com.vlad.code.sample.exception.InvalidEmailException;
import com.vlad.code.sample.exception.UserNotFoundException;
import com.vlad.code.sample.model.User;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class CodeSampleService {

    private final Map<String, User> registeredUsers = new HashMap<>();

    public CodeSampleService() {
        //Initialize User storage
        initUserDomain();
    }

    /**
     *
     * @return Return a list of all registered Users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(registeredUsers.values());
    }

    /**
     * @return Return an instance of a User with specified email address.
     * If the email address supplied is not found throw a UserNotFoundException
     * @param email of the user to be returned
     * @throws UserNotFoundException if the user is not found
     */
    public User getUserByEmail(String email) throws UserNotFoundException {
        return registeredUsers.values().stream().filter(u -> u.getEmail().equals(email))
                .findFirst().orElseThrow(UserNotFoundException::new);
    }

    /**
     * Do some partial validation on the passed in instance of User and add it to the
     * Map of registered users if it is valid. Otherwise throw a corresponding Exception
     * @param user to be added to the Map
     * @return newly created User
     * @throws DuplicateUserException if the user with specified id already in the Map
     * @throws InvalidEmailException if the email of passed in user is invalid
     */
    public User createNewUser(User user) throws DuplicateUserException, InvalidEmailException {
        String errorMessage;
        if (registeredUsers.containsKey(user.getId())) {
            errorMessage = "User with specified id already registered.";
            log.error("Attempt to create User {} failed due to the following error: {}", user.toString(), errorMessage);
            throw new DuplicateUserException(errorMessage);
        } else {
            if (EmailValidator.getInstance().isValid(user.getEmail())) {
                User newUser = new User(user.getId(), user.getName(), user.getEmail());
                registeredUsers.put(newUser.getId(), newUser);
                log.info("Created new User: {}", newUser.toString());
                return newUser;
            } else {
                errorMessage = "Email supplied for the new user is invalid.";
                log.error("Attempt to create User {} failed due to the following error: {}", user.toString(), errorMessage);
                throw new InvalidEmailException(errorMessage);
            }
        }
    }

    /**
     * Remove a User with specified Id from the Map of registered users.
     * If it is not found, throw an Exception
     *
     * @param id of the user to be deleted
     * @return deleted user instance
     * @throws UserNotFoundException if the user is not found
     */
    public User removeUserById(String id) throws UserNotFoundException {
        if (!registeredUsers.containsKey(id)) {
            log.error("Attempt to delete a User with id {} failed due to the following error: {}",
                    id, UserNotFoundException.DEFAULT_ERROR_MESSAGE);
            throw new UserNotFoundException();
        } else {
            User userToDelete = registeredUsers.get(id);
            registeredUsers.remove(id);
            log.info("Removed User: {}", userToDelete.toString());
            return userToDelete;
        }
    }

    //This method id sued in Demo mode to support POC functionality
    private void initUserDomain() {
        User user1 = new User("1", "Tom", "tom@email.com");
        registeredUsers.put(user1.getId(), user1);
        User user2 = new User("2", "Ann", "ann@email.com");
        registeredUsers.put(user2.getId(), user2);
        User user3 = new User("3", "Jack", "jack@email.com");
        registeredUsers.put(user3.getId(), user3);
    }
}
