package com.blog.project.app.rest.login;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.app.entities.User.UserData;
import com.blog.project.app.models.service.IUserService;
import com.blog.project.app.utils.LocalUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class LoginRestController {

	private String contentType = "application/json";
	
	public static final String SECRETKEY = "d>b_8m;]TMkK1VOyl?OpN\",WL'ehZB6_U(=[B,o(E8OEDz]>p75RfR.sao_OD)W";

	@Autowired
	private IUserService userService;
	
	@GetMapping("/login")
	public List<UserData> getAllUsers(HttpServletResponse response, HttpServletRequest request) {
		response.setContentType(contentType);

		List<UserData> returningJSON = userService.findAllProjectedBy();

		if (returningJSON.isEmpty())
			LocalUtils.ThrowPayloadEmptyException(request);

		return returningJSON;
	}


	@PostMapping("/login")
	public JSONObject login(@RequestBody Map<String, Object> payload) {

		String username = (String) payload.get("username");
		String password = (String) payload.get("password");
		
		String token = getJWTToken(username);

		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "User and password are correct, token generated");
		responseJson.appendField("generatedToken", token);
		
		return responseJson;		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private String getJWTToken(String username) {
		//String secretKey = "mySecretKey";
		
		//SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		SecretKey secretKey = new SecretKeySpec(SECRETKEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		String token = Jwts
				.builder()
				.setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(secretKey).compact();

		return "Bearer " + token;
	}
}
