package com.otp.otpauthserver.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Otp implements Serializable {

    @Id
    private String username;
    private String code;
}
