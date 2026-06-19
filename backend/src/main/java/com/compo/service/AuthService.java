package com.compo.service;

import com.compo.dto.AuthRequest;
import com.compo.dto.AuthResponse;

public interface AuthService {

    AuthResponse register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
