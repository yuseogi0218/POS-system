package com.yuseogi.userservice.service.implementation;

import com.yuseogi.common.exception.CustomException;
import com.yuseogi.userservice.entity.UserEntity;
import com.yuseogi.userservice.exception.UserErrorCode;
import com.yuseogi.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findFirstByEmail(username).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        return new User(user.getEmail(), user.getPassword(), user.getAuthorities());
    }
}
