package com.dev.jwt.jwt.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //Para decir que usuarios son autetenticos
    private AuthenticationManager authenticationManager;
 
    private static final SecretKey SECRET_KEY= Jwts.SIG.HS256.key().build();


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
                //mediante el request obtenemos el username y password
                //1.- tomar el jsn y lo convertimos a un objeto de java
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
                //se crea un username.... con el nombre del username y el password
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                //llama a authenticate para verificar las credenciales
                return authenticationManager.authenticate(authenticationToken);
    }



    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response, 
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User user =  (User) authResult.getPrincipal();
        String username = user.getUsername();

        String jwt = Jwts.builder()
                         .subject(username)
                         .signWith(SECRET_KEY)
                         .compact();
        //devolvemos el token a la vista
        response.addHeader("Authorization", "Bearer " + jwt);
        //ademas pasamos como respuesta json
        Map<String, String> body = new HashMap<>();

        body.put("token", jwt);
        body.put("username", username);
        body.put("message", String.format("Hello %s you have logged in successfully!", username));
        //Escribimos este json a la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType("application/json");
        response.setStatus(200);
    }
}
