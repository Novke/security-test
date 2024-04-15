package com.sandbox.ProductSecurity.security;

import com.sandbox.ProductSecurity.model.User;
import com.sandbox.ProductSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = userRepository.findByEmail(username);
        dbUser.getRoles().size();

        if (dbUser == null) throw new UsernameNotFoundException("User not found for email: " + username);

        return new org.springframework.security.core.userdetails.User(dbUser.getEmail(), dbUser.getPassword(), dbUser.getRoles());
    }
}
