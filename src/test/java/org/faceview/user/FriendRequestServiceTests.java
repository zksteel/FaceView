package org.faceview.user;

import org.faceview.user.entity.User;
import org.faceview.user.repository.FriendRequestRepository;
import org.faceview.user.service.FriendRequestServiceImpl;
import org.faceview.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FriendRequestServiceTests {

    private static final String FRIEND_REQUEST_NOT_FOUND_USER_MSG = "Unhandled null user case";

    @Mock
    private UserService userService;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestServiceImpl friendRequestService;

    @Before
    public void setUp(){

    }

    @Test
    public void testAcceptFriendRequest_withInvalidUsersId_shouldReturnFalse() {
        when(this.userService.findById(anyString())).thenReturn(null);

        boolean result = this.friendRequestService.acceptFriendRequest("2", "4");

        assertFalse(FRIEND_REQUEST_NOT_FOUND_USER_MSG, result);
    }

    @Test
    public void testDeclineFriendRequest_withInvalidUsersId_shouldReturnFalse() {
        when(this.userService.findById(anyString())).thenReturn(null);

        boolean result = this.friendRequestService.acceptFriendRequest("2", "4");

        assertFalse(FRIEND_REQUEST_NOT_FOUND_USER_MSG, result);
    }

    @Test
    public void testAcceptFriendRequest_withValidUsersId_shouldReturnTrue() {
        when(this.userService.findById(anyString())).thenReturn(new User());

        boolean result = this.friendRequestService.acceptFriendRequest("2", "4");

        assertTrue(result);
    }

    @Test
    public void testDeclineFriendRequest_withValidUsersId_shouldReturnTrue() {
        when(this.userService.findById(anyString())).thenReturn(new User());

        boolean result = this.friendRequestService.acceptFriendRequest("2", "4");

        assertTrue(result);
    }
}
