package org.faceview.user;

import org.faceview.user.entity.User;
import org.faceview.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllFriends_addedFriends_shouldFindAllFriendsForGivenUser(){
        User pesho = new User();
        pesho.setUsername("pesho");
        pesho.setEmail("pesho");

        User gosho = new User();
        gosho.setUsername("gosho");
        gosho.setEmail("gosho");

        User tosho = new User();
        tosho.setUsername("tosho");
        tosho.setEmail("tosho");

        pesho.setFriends(Arrays.asList(gosho, tosho));

        this.testEntityManager.persistAndFlush(tosho);
        this.testEntityManager.persistAndFlush(gosho);
        this.testEntityManager.persistAndFlush(pesho);

        List<User> friends = this.userRepository.findAllFriends(pesho.getId());

        assertEquals("gosho,tosho", String.join(",", friends.stream().map(User::getUsername).collect(Collectors.toList())));
    }

    @Test
    public void findAllFriends_noAddedFriends_shouldFindAllFriendsForGivenUser(){
        User pesho = new User();
        pesho.setUsername("pesho");
        pesho.setEmail("pesho");

        User gosho = new User();
        gosho.setUsername("gosho");
        gosho.setEmail("gosho");

        User tosho = new User();
        tosho.setUsername("tosho");
        tosho.setEmail("tosho");

        this.testEntityManager.persistAndFlush(tosho);
        this.testEntityManager.persistAndFlush(gosho);
        this.testEntityManager.persistAndFlush(pesho);

        List<User> friends = this.userRepository.findAllFriends(pesho.getId());

        assertEquals("", String.join(",", friends.stream().map(User::getUsername).collect(Collectors.toList())));
    }


}
