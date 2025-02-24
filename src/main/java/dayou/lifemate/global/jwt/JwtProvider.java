package dayou.lifemate.global.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider {
	private final Key key;
	private final long accessExpiration;

	public JwtProvider(@Value("${jwt.secret}") String key,
		@Value("${jwt.expiration}") long accessExpiration) {
		this.key = Keys.hmacShaKeyFor(key.getBytes());
		this.accessExpiration = accessExpiration;
	}

	public String createToken(String email) {
		Date now = new Date();
		Date validDate = new Date(now.getTime() + accessExpiration);

		return Jwts.builder()
			.subject(email)
			.issuedAt(now)
			.expiration(validDate)
			.signWith(key, SignatureAlgorithm.HS256) // deprecated, 추후에 encryptWith(secretKey, aeadAlgorithm) 변경 필요
			.compact();
	}

	public String getSubject(String accessToken) {
		return Jwts.parser()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(accessToken)
			.getBody()
			.getSubject();
	}

	public boolean validateToken(String accessToken) {
		try {
			Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
		}
		return false;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = parseClaims(token);

		Collection<? extends GrantedAuthority> authorities;
		if (claims.get("auth") != null) {
			authorities = Arrays.stream(claims.get("auth").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		} else {
			authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		}

		UserDetails principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSigningKey() {
		return key;
	}
}
