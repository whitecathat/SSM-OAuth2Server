package com.apabi.center.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apabi.center.util.CertUtil;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apabi.center.entity.LocalUser;
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
	public Object authorize(HttpServletRequest request, HttpServletResponse cookieResponse, Model model) throws OAuthSystemException, OAuthProblemException {
        try{
			OAuthAuthzRequest oAuthzRequest = new OAuthAuthzRequest(request);
			String authCode = null;
			String redirectURI = oAuthzRequest.getRedirectURI();
			String clientId = oAuthzRequest.getClientId();
			String cert = CertUtil.getCertFromCookie(request);
			int uid;
			
			if (!oAuthService.checkClientByIdURI(clientId, redirectURI)) {
	            OAuthResponse response =
	                    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
	                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
	                            .buildJSONMessage();
	            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}
			if (!oAuthzRequest.getResponseType().equals(ResponseType.CODE.toString())) {
		    OAuthResponse response = 
			    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
			    	    .setError(OAuthError.TokenResponse.INVALID_GRANT)
			            .buildJSONMessage();
		    return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            		}
			LocalUser localUser = oAuthService.findLocalUserByCert(cert);
			if (StringUtils.isEmpty(cert) || localUser == null) {
				LocalUser loginLocalUser = (LocalUser) login(request, cookieResponse);
				if (loginLocalUser == null) {
					model.addAttribute("clientId", clientId);
					model.addAttribute("redirectURI", redirectURI);
					return "login";
				} else {
					uid = loginLocalUser.getUid();
				}
			} else {
				uid = localUser.getUid();
			}
			
			OAuthIssuerImpl oAuthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
			authCode = oAuthIssuerImpl.authorizationCode();
			
			oAuthService.addOAuthCode(authCode, uid);
	        OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
	        builder.setCode(authCode);
        	final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
        	HttpHeaders headers = new HttpHeaders();
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
		String clientId = tokenRequest.getClientId();
		String clientSecret = tokenRequest.getClientSecret();
		String redirectURI = tokenRequest.getRedirectURI();
		
		if (!oAuthService.checkClientByIdSecretURI(clientId, clientSecret, redirectURI)) {
            OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                            .buildJSONMessage();
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
		}
		if (!tokenRequest.getGrantType().equals(GrantType.AUTHORIZATION_CODE.toString())) {
            OAuthResponse response = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                    .setError(OAuthError.TokenResponse.INVALID_GRANT)
                    .buildJSONMessage();
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                }
		String oauthCode = tokenRequest.getCode();
		int uid = oAuthService.getUidByCode(oauthCode);
		String uidStr = String.valueOf(uid);
		if (uid == 0) {
            OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                            .buildJSONMessage();
            return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
		}
		
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        final String accessToken = oauthIssuerImpl.accessToken();
        
        oAuthService.addAccessToken(accessToken, uid);
        OAuthResponse response = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setTokenType(OAuth.OAUTH_ACCESS_TOKEN)
                .setParam("uid", uidStr)
                .setExpiresIn(String.valueOf(3600))
                .buildJSONMessage();
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
	}
	
	@RequestMapping(value = "get_user_info", method = RequestMethod.POST)
	public Object getUserInfo() {
		return null;
	}
	
	public Object login(HttpServletRequest request, HttpServletResponse response) {
		if (!"POST".equals(request.getMethod())) {
			return null;
		}
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			return null;
		}
		
		LocalUser localUser = oAuthService.findLocalUserByEmailPassword(email, password);
		if (localUser != null) {
			Cookie cookie = new Cookie("cert", localUser.getSalt());
			response.addCookie(cookie);
		}
		return localUser;
	}
}
