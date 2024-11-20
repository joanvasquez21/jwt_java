package com.dev.jwt.jwt.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.dev.jwt.jwt.security.SimpleGrantedAuthorityJsonCreator;
import static com.dev.jwt.jwt.security.TokenJwtConfig.CONTENT_TYPE;
import static com.dev.jwt.jwt.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.dev.jwt.jwt.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.dev.jwt.jwt.security.TokenJwtConfig.SECRET_KEY;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// this class is to validate  the token
public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //first to get the token
        String header = request.getHeader(HEADER_AUTHORIZATION);

        //Si la cabecera es nula y la autorizacion e(para urls que son de tipo publicos que no requieran autenticacion)
        if (header == null || !header.startsWith(PREFIX_TOKEN))  {
            chain.doFilter(request,response);
            return;
        }

        String token = header.replace(PREFIX_TOKEN, "");
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            //return the sub, si tiene un id,username
            String username = claims.getSubject();
            //return the username
           // String username2 = (String) claims.get("username");
            Object authoritiesClaims = claims.get("authorities");
            //Tenemos que convertir el object authoritiesClaims  en grantedauthority de tipo collection, tenemos que procesar los roles 
            Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            //autenticamos
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            //si todo sale bien,continuamos con la cadena de filtro
            chain.doFilter(request, response);

        } catch (Exception ex) {
            Map<String, String> body = new HashMap<>();
            body.put("error", ex.getMessage());
            body.put("message", "The token is not valid.");

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
        }

    }

}
