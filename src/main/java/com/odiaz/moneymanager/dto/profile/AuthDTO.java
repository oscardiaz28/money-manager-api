package com.odiaz.moneymanager.dto.profile;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthDTO {
    private String email;
    private String password;
    private String token;
}
