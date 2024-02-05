package com.project.onlinestore.jwt.util;

import com.project.onlinestore.entity.User;
import com.project.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("[loadUserByUsername] Username not found " + username));

        if (user != null) {
            System.out.println("user = " + user);
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            return userDetails;
        }

        return null;
    }
}
