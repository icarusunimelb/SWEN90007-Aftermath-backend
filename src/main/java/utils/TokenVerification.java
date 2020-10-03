package utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TokenVerification {
    private static final String SECRET_KEY = "Aftermath";
    private static final long TTEXPIRE = 7200000;
    private static volatile List<String> tokens = new ArrayList<String>();
    public static String createJWT(String email, String userType) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(email)
                .setSubject(userType)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        long expMillis = nowMillis + TTEXPIRE;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public String verifyToken(String token){

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token).getBody();
        System.out.println("email: " + claims.getId());
        System.out.println("userType: " + claims.getSubject());
        System.out.println("Expiration: " + claims.getExpiration());
        return claims.getId()+"::"+claims.getSubject();
    }

    public static void addToken(String token){
        if (!tokens.contains(token)){
            tokens.add(token);
        }
    }

    public static void removeToken(String token){
        if (!tokens.contains(token)){
            tokens.remove(token);
        }
    }

    public static boolean validToken(String token){
        return tokens.contains(token);
    }

}
