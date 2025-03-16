package models;

import lombok.Data;

@Data
public class LoginRequestModel {
    String email, password;
}
