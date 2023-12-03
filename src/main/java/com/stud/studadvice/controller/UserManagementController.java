package com.stud.studadvice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.stud.studadvice.service.UserManagementService;
import com.stud.studadvice.model.security.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserManagementController {
    private final UserManagementService userManagementService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path = "/user-claims/{uid}")
    public void setUserClaims( @PathVariable String uid, @RequestBody List<Authority> requestedClaims) throws FirebaseAuthException {
        userManagementService.setUserClaims(uid, requestedClaims);
    }
}
