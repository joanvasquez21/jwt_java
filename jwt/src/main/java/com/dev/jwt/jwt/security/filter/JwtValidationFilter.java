package com.dev.jwt.jwt.security.filter;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.dev.jwt.jwt.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.dev.jwt.jwt.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.dev.jwt.jwt.security.TokenJwtConfig.SECRET_KEY;
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

                //return the sub, si tiene un id,username
                String username = claims.getSubject();
                //return the username
                String username2 = (String) claims.get("username");

                Object authoritiesClaims = claims.get("authorities");

                //
                Collection<? extends GrantedAuthority> authorities  = 
                             new ObjectMapper().readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class);
                //Procesar los roles
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username2, null,  authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
               // Collection<? extends GrantedAuthority> authorities = new ObjectMapper().readValue(authoritiesClaims);


                }catch(Exception ex){
                    ex.getMessage();
                }



    }

    

    
}
