package com.bank.auth;
/**
 * <p>The constants class used for Authentication/Authorization</p>
 * 
 * @author Ketan.Soneji
 */
public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWT";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
	public static final String REGISTER_URL = "/api/v1/employee/register";
	public static final String AUTHORITIES_KEY = "roles";
	public static final String BLACKLIST_TOKEN_CACHE = "blacklistedTokens";
}
