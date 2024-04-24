package com.ninjas.gig.security;

import com.ninjas.gig.entity.UserAccount;
import com.ninjas.gig.entity.UserType;
import com.ninjas.gig.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserAccount user = userRepository.findByUsername(username);
            return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(Collections.singletonList(user.getUserType())));
        } catch (Exception e){
            throw new UsernameNotFoundException("Username not found");
        }

    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<UserType> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
    }
}