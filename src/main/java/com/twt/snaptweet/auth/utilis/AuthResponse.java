package com.twt.snaptweet.auth.utilis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthResponse {

    String accessToken;

    String refreshToken;

    Process process;
}
