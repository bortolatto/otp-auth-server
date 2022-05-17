package com.otp.otpauthserver.security;

import com.otp.otpauthserver.model.UserDecorator;
import com.otp.otpauthserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        UserDecorator byUsername = userService.loadUserByUsername(username);
        if (userService.isCorrectPassword(rawPassword, byUsername.getPassword())) {
            return authentication(byUsername);
        } else {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    private Authentication authentication(UserDecorator userDecorator) {
        return new UsernamePasswordAuthenticationToken(userDecorator.getUsername(), userDecorator.getPassword(), userDecorator.getAuthorities());
    }
}
