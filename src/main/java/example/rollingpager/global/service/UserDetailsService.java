package example.rollingpager.global.service;

import example.rollingpager.global.entity.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadUserByNickname(String nickname) throws UsernameNotFoundException;
}
