package com.fsd08.MediLink.security;

import com.fsd08.MediLink.MediLinkApplication;
import com.fsd08.MediLink.repository.UserRepository;
import com.fsd08.MediLink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MediLinkApplication.class);

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(String.valueOf(userRepository.findByUsername(username).map(UserDetailsImpl::new).orElseThrow(()->new UsernameNotFoundException("No user Found"))));
        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new).orElseThrow(()->new UsernameNotFoundException("No user Found"));
    }
}
