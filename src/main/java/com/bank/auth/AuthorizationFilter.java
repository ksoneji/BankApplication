package com.bank.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
/**
 * <p>The filter class to perform authorization</p>
 * 
 * @author Ketan.Soneji
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {
	private CacheManager cacheManager;

	public AuthorizationFilter(AuthenticationManager authManager, CacheManager cacheManager) {
		super(authManager);
		this.cacheManager = cacheManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		if (token != null) {
			
			// Return if the bearer token is blacklisted i.e. logged out. 
			if (cacheManager.getCache(SecurityConstants.BLACKLIST_TOKEN_CACHE).get(token) != null)
				return null;
			
			// parse the token.
			String user = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
					.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();

			Claim claim = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes())).build()
					.verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getClaim(SecurityConstants.AUTHORITIES_KEY);
			
		    List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claim.asString());
			
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
			return null;
		}
		return null;
	}
	
	
}
