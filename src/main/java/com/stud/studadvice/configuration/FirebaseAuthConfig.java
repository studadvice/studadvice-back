package com.stud.studadvice.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.time.Duration;


@Configuration
public class FirebaseAuthConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            // solve FirebaseApp name [DEFAULT] already exists!
            FirebaseApp.initializeApp(options);
        }

        return FirebaseAuth.getInstance();
    }
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkUri = "https://www.googleapis.com/service_accounts/v1/jwk/securetoken@system.gserviceaccount.com";
        return NimbusJwtDecoder.withJwkSetUri(jwkUri)
                .build();
    }

}
