package com.otp.otpauthserver.security;

import com.otp.otpauthserver.repository.OtpRepository;
import com.otp.otpauthserver.repository.UserRepository;
import com.otp.otpauthserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class UserConfig {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(userRepository, passwordEncoder(), otpRepository);
    }
}
