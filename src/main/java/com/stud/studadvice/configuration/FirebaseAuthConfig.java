package com.stud.studadvice.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseAuthConfig {

    @Bean
    public FirebaseAuth firebaseAuth() throws IOException {
        InputStream serviceAccountStream = loadServiceAccountResource().getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();

        FirebaseApp.initializeApp(options);

        return FirebaseAuth.getInstance();
    }

    public Resource loadServiceAccountResource() {
        return new ClassPathResource("serviceAccountKey.json");
    }
}
