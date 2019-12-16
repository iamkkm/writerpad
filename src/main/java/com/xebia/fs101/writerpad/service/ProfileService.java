package com.xebia.fs101.writerpad.service;

import com.xebia.fs101.writerpad.domain.User;
import com.xebia.fs101.writerpad.exception.SameUserException;
import com.xebia.fs101.writerpad.exception.UserAlreadyFollowException;
import com.xebia.fs101.writerpad.exception.UserUnfollowingException;
import com.xebia.fs101.writerpad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class ProfileService {
    @Autowired
    UserRepository userRepository;

    public User follow(User user, User userToFollow) {
        User followingUser = userRepository.findByUsername(user.getUsername());
        if (followingUser.getUsername().equals(userToFollow.getUsername())) {
            throw new SameUserException();
        }
        HashSet<String> following = followingUser.getFollowing();
        if (following.contains(userToFollow.getUsername())) {
            throw new UserAlreadyFollowException();
        }
        followingUser.setFollowingCount(
                followingUser.getFollowingCount() + 1);
        followingUser.setFollowing(following);
        userToFollow.setFollowerCount(
                userToFollow.getFollowerCount() + 1);
        User saved = userRepository.save(followingUser);
        userRepository.save(userToFollow);
        return saved;
    }

    public User unfollow(User user, User usertoUnfollow) {
        User unfollowingUser = userRepository.findByUsername(user.getUsername());
        if (unfollowingUser.getUsername().equals(usertoUnfollow.getUsername())) {
            throw new SameUserException();
        }
        HashSet<String> following = unfollowingUser.getFollowing();
        if (!following.contains(usertoUnfollow.getUsername())) {
            throw new UserUnfollowingException();
        }
        unfollowingUser.setFollowingCount(unfollowingUser
                .getFollowingCount() - 1);
        usertoUnfollow.setFollowerCount(usertoUnfollow
                .getFollowerCount() - 1);
        following.remove(usertoUnfollow.getUsername());
        unfollowingUser.setFollowing(following);
        User saved = userRepository.save(unfollowingUser);
        userRepository.save(usertoUnfollow);
        return saved;
    }
}
