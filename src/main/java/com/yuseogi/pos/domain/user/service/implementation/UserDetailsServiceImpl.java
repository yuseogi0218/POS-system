package com.yuseogi.pos.domain.user.service.implementation;

import com.yuseogi.pos.domain.user.entity.UserEntity;
import com.yuseogi.pos.domain.user.exception.UserErrorCode;
import com.yuseogi.pos.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findFirstByEmail(username).orElseThrow(() -> new UsernameNotFoundException(UserErrorCode.NOT_FOUND_USER.getMessage()));
        return new User(userEntity.getEmail(), userEntity.getPassword(), userEntity.getAuthorities());
    }
}
