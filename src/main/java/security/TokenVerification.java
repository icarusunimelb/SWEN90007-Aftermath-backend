package security;


import io.jsonwebtoken.*;
import org.json.JSONObject;
import utils.USERTYPE;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class TokenVerification {
    private static final String SECRET_KEY = "Aftermath";
    private static final long TTEXPIRE = 7200000;
    private static volatile List<String> tokens = new ArrayList<String>();

    public static String createJWT(String userId, String userType) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(userId)
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

    public static String getIdAndSubject(String token){
        try {
            if (token == null || !token.startsWith("Bearer ")){
                System.out.println("Invalid token");
                return "";
            }
            String realToken = token.substring(7, token.length());
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(realToken).getBody();
            return claims.getId() + "," + claims.getSubject();
        } catch (ExpiredJwtException e){
            System.out.println(e);
            return "";
        } catch (SignatureException e) {
            System.out.println(e);
            return "";
        } catch (MalformedJwtException e) {
            System.out.println(e);
            return "";
        } catch (UnsupportedJwtException e) {
            System.out.println(e);
            return "";
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return "";
        }
    }

//    public static String getUserTypeFromToken()

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

    public static String getTokenFromHeader(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {

            String headerName = headerNames.nextElement();

            if(headerName.equals("authorization")){
                Enumeration<String> headers = request.getHeaders(headerName);
                while (headers.hasMoreElements()) {

                    String headerValue = headers.nextElement();

                    return headerValue;

                }
            }
        }
        return "";
    }
    
    public static USERTYPE getRoleFromRequest(HttpServletRequest request) {

        String token = getTokenFromHeader(request);
        String userIdAndUserType = "";
        //System.out.println("token: " + token);
        if(token.equals("")){

            return USERTYPE.UNKNOWN;
        } else {
            try{
                userIdAndUserType = getIdAndSubject(token);
            } catch(io.jsonwebtoken.ExpiredJwtException e) {
                return USERTYPE.UNKNOWN;
            }
            if(userIdAndUserType.contains("LECTURER")){
                return USERTYPE.LECTURER;
            } else if(userIdAndUserType.contains("STUDENT")){
                return USERTYPE.STUDENT;
            } else if(userIdAndUserType.contains("ADMIN")){
                return USERTYPE.ADMIN;
            }
        }
        return USERTYPE.UNKNOWN;
    }

}
