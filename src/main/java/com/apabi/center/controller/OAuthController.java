package com.apabi.center.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apabi.center.service.OAuthService;

@Controller
@RequestMapping("/OAuth")
public class OAuthController {
	
	@Autowired
	private OAuthService oAuthService;
	
	/**
	 * @name authorize
	 * @param request(response_type, client_id, redirect_uri)
	 * @return
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	@RequestMapping("/authorize")
	public Object authorize(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
		OAuthAuthzRequest oAuthzRequest = new OAuthAuthzRequest(request);
		String authCode = null;
		OAuthIssuerImpl oAuthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		authCode = oAuthIssuerImpl.authorizationCode();
		String redirectURI = oAuthzRequest.getRedirectURI();
        OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
        builder.setCode(authCode);
        final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
        HttpHeaders headers = new HttpHeaders();
        try{
	        headers.setLocation(new URI(response.getLocationUri()));
	        return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @name code2Token
	 * @param request(grant_type, code, redirect_uri, client_id, client_secret, (Content-Type))
	 * @return
	 * @throws OAuthSystemException
	 * @throws OAuthProblemException
	 */
	@RequestMapping(value = "access_token", method = RequestMethod.POST)
	public Object code2Token(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
		OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        final String accessToken = oauthIssuerImpl.accessToken();
        OAuthResponse response = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setTokenType(OAuth.OAUTH_TOKEN_TYPE)
                .setParam("uid", "123456")
                .setExpiresIn(String.valueOf(3600))
                .buildJSONMessage();
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
	}
	
	@RequestMapping("callback")
	public void callback() {
		
	}
	
	public boolean checkLoginStatus(HttpServletRequest request) {
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    
	    if(null != cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    
	    if ((cookieMap.get("token") != null)) {
	    	return true;
	    }
		return false;  	
	}
}
