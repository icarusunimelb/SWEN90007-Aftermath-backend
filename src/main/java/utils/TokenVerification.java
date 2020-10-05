package utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;

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
    public static final int STUDENTFLAG = 0;
    public static final int LECTURERFLAG = 1;
    public static final int ADMINTFLAG = 2;
    public static final int ERRORFLAG = -1;

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

    public static String verifyToken(String token){
        String realToken = token.substring(7,token.length());
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(realToken).getBody();
        System.out.println("userId: " + claims.getId());
        System.out.println("userType: " + claims.getSubject());
        System.out.println("Expiration: " + claims.getExpiration());
        System.out.println("token: " + realToken);
        return claims.getId()+","+claims.getSubject();
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
    
    public static int validLecturer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");

        String token = getTokenFromHeader(request);
        String userIdAndUserType = "";
        System.out.println("token: " + token);
        if(token.equals("")){

            return ERRORFLAG;
        } else {
            try{
                userIdAndUserType = verifyToken(token);
            } catch(io.jsonwebtoken.ExpiredJwtException e) {
                return ERRORFLAG;
            }
            if(userIdAndUserType.equals("")){
                return ERRORFLAG;
            } else if(userIdAndUserType.contains("STUDENT")){
                return STUDENTFLAG;
            }
        }
        return LECTURERFLAG;
    }

}
