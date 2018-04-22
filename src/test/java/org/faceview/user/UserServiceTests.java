package org.faceview.user;

import org.faceview.user.entity.User;
import org.faceview.user.model.UserSearchResultModel;
import org.faceview.user.repository.UserRepository;
import org.faceview.user.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

    private static final String FIND_USERS_BY_SUBSTRING_EXPECTED = "pesho,gosho";
    private static final String FRIENDS_NOT_FOUND_MSG = "Friend cannon be found";
    private static final String TEST_GET_USER_BY_USERNAME_EXPECTED = "ivan";

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void startUp(){
        when(this.userRepository.findUsersByUsernameContainingAndUsernameIsNot(anyString(), anyString()))
                .thenReturn(Arrays.asList(new User(){{setUsername("pesho");}}, new User(){{setUsername("gosho");}}));

        when(this.userRepository.getUserByUsername(anyString()))
                .thenReturn(new User(){{setUsername("ivan"); setId("1");}});
        when(this.userRepository.findAllFriends(anyString()))
                .thenReturn(
                        Arrays.asList(
                                new User(){{setUsername("gosho"); setId("2");}},
                                new User(){{setUsername("pesho"); setId("3");}},
                                new User(){{setUsername("mitko"); setId("4");}}
                        )
                );

        when(this.modelMapper.map(any(), any(Type.class))).thenReturn(Arrays.asList(new UserSearchResultModel(){{setUsername("pesho");}}, new UserSearchResultModel(){{setUsername("gosho");}}));
    }

    @Test
    public void testFindUsersByUsernameContaining_withSubstringToFindAndLoggedInUsername_shouldFindUsersContainingSubstring() {

        List<UserSearchResultModel> userList = this.userService.findUsersWithUsernameContaining("o", "ivan");

        assertEquals(FIND_USERS_BY_SUBSTRING_EXPECTED, String.join(",", userList.stream().map(UserSearchResultModel::getUsername).collect(Collectors.toList())));
    }

    @Test
    public void testIsFriendMethod_withLoggedInUsernameAndFriendId_shouldReturnTrue(){
        boolean result = this.userService.isFriend("ivan", "3");

        assertTrue(FRIENDS_NOT_FOUND_MSG, result);
    }

    @Test
    public void testLoadUserByUsername_withUsername_shouldReturnUser(){

        UserDetails user = this.userService.loadUserByUsername("ivan");

        assertEquals(TEST_GET_USER_BY_USERNAME_EXPECTED, user.getUsername());
    }




}
