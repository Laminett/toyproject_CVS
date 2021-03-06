package com.alliex.cvs.security;

import com.auth0.jwt.Algorithm;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class JWT {

    @Value("${jwt.token.issuer}")
    private String issuer;

    @Value("${jwt.token.clientId}")
    private String clientId;

    @Value("${jwt.token.clientSecret}")
    private String clientSecret;

    @Value("${jwt.token.expirySeconds}")
    private int expirySeconds;

    private JWTSigner signer;
    private JWTVerifier jwtVerifier;

    @PostConstruct
    public void init() {
        signer = new JWTSigner(Base64.decodeBase64(clientSecret));
        jwtVerifier = new JWTVerifier(Base64.decodeBase64(clientSecret), clientId, issuer);
    }

    private JWTSigner.Options defaultOptions() {
        JWTSigner.Options options = new JWTSigner.Options();
        options.setAlgorithm(Algorithm.HS512);
        options.setExpirySeconds(expirySeconds);
        options.setNotValidBeforeLeeway(0);
        options.setIssuedAt(true);
        options.setJwtId(false);

        return options;
    }

    public String generateToken(Map<String, Object> claims) {
        claims.put("iss", issuer);

        return signer.sign(claims, defaultOptions());
    }

    public String refreshToken(String token) throws Exception {
        Map<String, Object> claims = verify(token);
        claims.remove("exp");
        claims.remove("nbf");
        claims.remove("iat");

        return signer.sign(claims, defaultOptions());
    }

    public Map<String, Object> verify(String token) throws Exception {
        return jwtVerifier.verify(token);
    }

}