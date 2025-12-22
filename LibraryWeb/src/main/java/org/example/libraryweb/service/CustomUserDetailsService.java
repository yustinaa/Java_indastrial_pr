package org.example.libraryweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private XMLManager xmlManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Element userEl = xmlManager.findUserByUsername(username);
            if (userEl == null)
            {
                System.out.println("Пользователь не найден в XML: " + username);
                throw new UsernameNotFoundException("User not found");
            }

            String password = userEl.getElementsByTagName("password").item(0).getTextContent();
            String role = userEl.getElementsByTagName("role").item(0).getTextContent();
            System.out.println("Найден пользователь: " + username + " с ролью: " + role);

            return User.withUsername(username)
                    .password("{noop}" + password) //без шифрования
                    .roles(role)
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error reading XML", e);
        }
    }
}