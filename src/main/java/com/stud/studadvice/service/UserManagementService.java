package com.stud.studadvice.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.stud.studadvice.dto.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final FirebaseAuth firebaseAuth;

    public void setUserClaims(String uid, List<Authority> requestedRoles) throws FirebaseAuthException {
        List<String> permissions = requestedRoles
                .stream()
                .map(Enum::toString)
                .toList();

        Map<String, Object> claims = Map.of("custom_claims", permissions);

        firebaseAuth.setCustomUserClaims(uid, claims);
    }
}
