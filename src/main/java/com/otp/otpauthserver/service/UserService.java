package com.otp.otpauthserver.service;

import com.otp.otpauthserver.entity.Otp;
import com.otp.otpauthserver.entity.Role;
import com.otp.otpauthserver.entity.User;
import com.otp.otpauthserver.model.OtpDTO;
import com.otp.otpauthserver.model.UserDTO;
import com.otp.otpauthserver.model.UserDecorator;
import com.otp.otpauthserver.repository.OtpRepository;
import com.otp.otpauthserver.repository.UserRepository;
import com.otp.otpauthserver.util.GenerateCodeUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpRepository otpRepository;

    @Override
    public UserDecorator loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(UserDecorator::new)
            .orElseThrow(() -> new IllegalArgumentException("Username not found"));
    }

    public boolean isCorrectPassword(String rawPassword, String password) {
        return passwordEncoder.matches(rawPassword, password);
    }

    @Transactional
    public User save(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        String password = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(password);

        List<Role> roles = userDTO.getRoles().stream().map(role -> {
            Role r = new Role();
            r.setName(role);
            r.setUser(user);
            return r;
        }).collect(Collectors.toList());

        user.setRoles(roles);
        userRepository.save(user);

        return user;
    }

    public void auth(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(() -> new BadCredentialsException("Username or password not found"));
        if (isCorrectPassword(userDTO.getPassword(), user.getPassword())) {
            renewOtp(user);
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    private void renewOtp(User user) {
        String code = GenerateCodeUtil.generateCode();
        Optional<Otp> otpByUsername = otpRepository.findOtpByUsername(user.getUsername());
        Otp otp;
        if (otpByUsername.isPresent()) {
            otp = otpByUsername.get();
            otp.setCode(code);
        } else {
            otp = new Otp();
            otp.setUsername(user.getUsername());
            otp.setCode(code);
        }
        otpRepository.save(otp);
    }

    public boolean check(OtpDTO otpDTO) {
        return otpRepository.findOtpByUsername(otpDTO.getUsername())
            .map(otp -> otp.getCode().equals(otpDTO.getCode()))
            .orElse(false);
    }
}
