package com.Assignment.group.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import com.Assignment.group.service.GroupUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private GroupUserDetailsService groupUserDetailsService;
	
	@Autowired
	private JwtUtils jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

	final String requestTokenHeader=request.getHeader("Authorization");
	System.out.println(requestTokenHeader+"this is token");
	String username=null;
	String jwtToken=null;
	
	if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
	
			jwtToken=requestTokenHeader.substring(7);
	try {
		username=this.jwtUtil.extractUsername(jwtToken);

	} catch (ExpiredJwtException e) {
				e.printStackTrace();
		System.out.println("jwt token has expired");	

	}catch (Exception e) {
		e.printStackTrace();
		System.out.println("error");
	}
	
	}
		else {
			System.out.println("Invalid token, not start with bearer string");
			
		}
	
	//validating
	
	if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
		final UserDetails userDetails=this.groupUserDetailsService.loadUserByUsername(username);
		System.out.println(username +"name printing");
		if(this.jwtUtil.validateToken(jwtToken, userDetails)) {
			//token is valid
			UsernamePasswordAuthenticationToken usernamePasswordAuthentication= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			usernamePasswordAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthentication);
			
		}
	}
	else {
		System.out.println("Token is Invalid");
	}
	
		filterChain.doFilter(request, response);

}
}
