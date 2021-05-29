package com.blog.project.app.rest.auth;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.project.app.entities.User;
import com.blog.project.app.models.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;

@Service
public class JsonWebTokenHandler implements JWTHandler {

	@Autowired
	private IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static final Logger logger = LoggerFactory.getLogger(JsonWebTokenHandler.class);
	public static final String SECRET_KEY = "d>b_8m;]TMkK1VOyl?OpN\",WL'ehZB6_U(=[B,o(E8OEDz]>p75RfR.sao_OD)W";

	SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

	public String generateJWTToken(String username, String password) throws RuntimeException {

		if (!userExists(username))
			throw new RuntimeException("Token could not be generated");
		if (!passwordIsFromUser(username, password))
			throw new RuntimeException("Token could not be generated");


		String token = Jwts.builder().setSubject(username)
				.claim("authorities",
						getRolesFromUser(username).stream().map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 604800000)).signWith(secretKey).compact();

		return "Bearer " + token;
	}

	public boolean isTokenValid(String jwt) {

		// SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(),
		// SignatureAlgorithm.HS256.getJcaName());
		String token = jwt.replace("Bearer ", "");
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);

			return true;
		} catch (JwtException e) {
			logger.info("Token was failed to verify");
			return false;
		}
	}

	public boolean containsRole(String jwt, String... role) {

		if (this.isTokenValid(jwt)) {

			JSONObject responseJson = getJsonOfToken(jwt);

			String authorities = responseJson.get("authorities").toString();

			for (String rol : role) {
				if (authorities.contains(rol))
					return true;

			}
			return false;
		}
		return false;
	}

	public JSONObject getJsonOfToken(String jwt) {
		jwt = jwt.replace("Bearer ", "");
		//logger.info(jwt);
		String[] token = jwt.split("\\.");
		String tokenPart2 = token[1];

		String decodedToken = new String(Base64.getDecoder().decode(tokenPart2));

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Object> map = null;
		JSONObject responseJson = null;
		try {
			map = mapper.readValue(decodedToken, Map.class);
		//	System.out.println(map);
			responseJson = new JSONObject(map);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			responseJson.appendField("message", "Token is not valid");
		}

		return responseJson;
	}
	@Override
	public String getUsernameFromJWT(String jwt) {
		if (this.isTokenValid(jwt)) {
			JSONObject responseJson = getJsonOfToken(jwt);			
			return (String) responseJson.get("sub");
		}
		// TODO Auto-generated method stub
		return null;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	private List<GrantedAuthority> getRolesFromUser(String username) {
		List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("");

		if (!userExists(username))
			return null;

		User user = userService.getUserByUsername(username);
		List<String> roles = user.getUserRoles();

		for (String role : roles)
			authorities.add(new SimpleGrantedAuthority(role));

		return authorities;
	}

	private boolean userExists(String username) {

		try {
			User user = userService.getUserByUsername(username);
			if (user.getPassword().equals(null))
				return false;
			if (user.getEmail().equals(null))
				return false;

			return true;
		} catch (RuntimeException e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	private boolean passwordIsFromUser(String username, String password) {

		if (!userExists(username))
			return false;

		User user = userService.getUserByUsername(username);

		boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());

		if (passwordMatch)
			return true;
		else
			return false;
	}


}
