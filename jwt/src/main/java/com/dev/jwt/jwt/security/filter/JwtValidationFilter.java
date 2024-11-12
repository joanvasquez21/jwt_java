package com.dev.jwt.jwt.security.filter;

import static com.dev.jwt.jwt.security.TokenJwtConfig.*;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// this class is to validate  the token
public class JwtValidationFilter  extends BasicAuthenticationFilter{
    
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
                //first to get the token
                String header = request.getHeader(HEADER_AUTHORIZATION);

                //Si la cabecera es nula y la autorizacion e(para urls que son de tipo publicos que no requieran autenticacion)
                if(header == null || !header.startsWith(PREFIX_TOKEN)){
                    return;
                }

                String token = header.replace(PREFIX_TOKEN, "");
                try{
                    Claims claims =  Jwts.parser()
                                        .verifyWith(SECRET_KEY)
                                        .build()
                                        .parseSignedClaims(token)
                                        .getPayload();

                }catch(Exception ex){
                    ex.g
                }



    }

    

    
}
