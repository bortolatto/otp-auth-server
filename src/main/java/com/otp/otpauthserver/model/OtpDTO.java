package com.otp.otpauthserver.model;

import lombok.Data;

@Data
public class OtpDTO {
    private String username;
    private String code;
}
