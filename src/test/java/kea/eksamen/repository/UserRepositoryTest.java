package kea.eksamen.repository;

import kea.eksamen.model.Role;
import kea.eksamen.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserRepositoryTest {
    private User user;

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup(){
        user = new User("first name", "last name", "email", "password");
        user.setRole(Role.EMPLOYEE);
    }

    @Test
    @DisplayName("UserRepository Integration test: adding new user in repository")
    void addUser_createNewUser()  {
        User savedUser = userRepository.addUser(user);
        assertNotNull(savedUser);
        assertEquals("first name", savedUser.getFirstName());
        assertEquals(6, savedUser.getId()); // 6 because there are inserted 5 users at initialization

        logger.info("Test add user: " + savedUser);
    }

    @Test
    @DisplayName("UserRepository Integration test: finding user by email in repository")
    void findUserByEmail_retrieveUserByEmail() {
        User user =  new User("first name", "last name", "some email", "password");
        user.setRole(Role.EMPLOYEE);
        userRepository.addUser(user);
        User foundUser = userRepository.findUserByEmail("some email");
        assertNotNull(foundUser);
        assertEquals("first name", foundUser.getFirstName());
    }

    @Test
    @DisplayName("Integration test finding users assigned to a project")
    void findTeamMembersByProjectId_getAssignedUsers() {
        List<User> team = userRepository.findTeamMembers(1);
        assertEquals(2, team.size());
        for(User user : team){
            System.out.println(user);
        }
    }

    @Test
    @DisplayName("Integration test finding users NOT assigned to the project")
    void findUnassignedUsersByProjectId_getUnassignedUsers() {
        List<User> unassignedUsers = userRepository.findUnassignedUsers(2);
        assertEquals(3, unassignedUsers.size());
        for(User user : unassignedUsers){
            System.out.println(user);
        }
    }
}