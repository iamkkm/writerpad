package com.xebia.fs101.writerpad.api;

import com.xebia.fs101.writerpad.api.response.ProfileResponse;
import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.service.ProfileService;
import com.xebia.fs101.writerpad.service.UserService;
import com.xebia.fs101.writerpad.service.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/api/profiles")
public class ProfileResource {

    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> get(@PathVariable String username) {
        User user = userService.find(username);
        return ResponseEntity.status(OK).body(ProfileResponse.from(user));
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> follow(@CurrentUser User user,
                                                  @PathVariable String username) {
        User followedUser = userService.find(username);
        User followingUser = profileService.follow(user, followedUser);
        return ResponseEntity.status(OK).body(ProfileResponse.from(followingUser));
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponse> unfollow(@CurrentUser User user,
                                                    @PathVariable String username) {
        User unfollowedUser = userService.find(username);
        User unfollowingUser = profileService.unfollow(user, unfollowedUser);
        return ResponseEntity.status(OK).body(ProfileResponse.from(unfollowingUser));
    }
}
