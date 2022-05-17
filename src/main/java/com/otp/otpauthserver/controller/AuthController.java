package com.otp.otpauthserver.controller;

import com.otp.otpauthserver.entity.User;
import com.otp.otpauthserver.model.OtpDTO;
import com.otp.otpauthserver.model.UserDTO;
import com.otp.otpauthserver.service.UserService;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping
public class AuthController {

    private final UserService service;

    @PostMapping("/user/add")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        User user = service.save(userDTO);
        return ResponseEntity
            .created(URI.create(String.format("/user/%s", user.getId()))).build();
    }

    @PostMapping("/user/auth")
    public void auth(@RequestBody UserDTO userDTO) {
        service.auth(userDTO);
    }

    @PostMapping("/otp/check")
    public void check(@RequestBody OtpDTO otpDTO, HttpServletResponse response) {
        if (service.check(otpDTO)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
