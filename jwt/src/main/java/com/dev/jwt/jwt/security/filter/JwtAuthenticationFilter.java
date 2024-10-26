package com.dev.jwt.jwt.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dev.jwt.jwt.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //Para decir que usuarios son autetnticos
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
                //mediante el request obtenemos el username y password
                //1.- tomar el jsn y copnverlito a un objeto de java
                User user = null;
                String username = null;
                String password = null;

                try {
                    user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                    username = user.getUsername();
                    password = user.getPassword();
                } catch (StreamReadException e) {
                    e.printStackTrace();
                } catch (DatabindException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                return authenticationManager.authenticate(authenticationToken);


    }

    

    
    
}
