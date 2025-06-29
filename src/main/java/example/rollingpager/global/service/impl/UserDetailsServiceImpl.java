package example.rollingpager.global.service.impl;

import example.rollingpager.global.entity.UserDetails;
import example.rollingpager.global.repository.UserRepository;
import example.rollingpager.global.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUserID(String userID) throws UsernameNotFoundException {
        logger.info("UserDetailsServiceImpl: loadUserByUserID 실행 유저 ID : {}", userID);
        return userRepository.findByNickname(userID);
    }
}
