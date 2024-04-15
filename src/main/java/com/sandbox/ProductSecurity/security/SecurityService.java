package com.sandbox.ProductSecurity.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SecurityService {

    public boolean login(String user, String password, HttpServletRequest request, HttpServletResponse response);
}
