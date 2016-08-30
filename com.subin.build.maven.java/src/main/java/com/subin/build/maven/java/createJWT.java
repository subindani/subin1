package com.subin.build.maven.java;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;   
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;


public class createJWT {

    private static String encodedKey = null;

    public static void main( String[] args )
    {
        System.out.println( "OAuth test start.." );
        String token = null;
        encodedKey = createSecretKey();
        token = createJWTRun("subinId", "http://trust.subin.com", "users/1300819380", TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS), encodedKey) ;
        if (!token.isEmpty() || token!=null )
        {
            System.out.println("Printing token.. ");
            System.out.println(token);
            System.out.println("Parsing token.. ");
            parseJWT(token,encodedKey);
        }
        System.out.println( "OAuth test end.." );
    }
    
    private static String createSecretKey()
    {
        //create new key
        SecretKey secretKey = null;
        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
        } catch(NoSuchAlgorithmException e) {
            System.out.println("Error: secretKey: "+secretKey);
            e.printStackTrace();
        }
        //get base64 encoded version of key
        encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }
    
    //Sample method to construct a JWT
    private static String createJWTRun(String id, String issuer, String subject, long ttlMillis, String encodedKey) {
     
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
         
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

      System.out.println("encodedKey in createJWTRun: "+encodedKey);
         
        if (!encodedKey.isEmpty() || encodedKey != null) {
            //We will sign our JWT with our ApiKey secret
            //byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
            System.out.println("encodedKey: "+encodedKey);
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
             
            //Let's set the JWT Claims
            try {
                JwtBuilder builder = Jwts.builder().setId(id)
                                              .setIssuedAt(now)
                                              .setSubject(subject)
                                              .setIssuer(issuer)
                                              .signWith(signatureAlgorithm, signingKey);
             
                 //if it has been specified, let's add the expiration
                if (ttlMillis >= 0) {
                    long expMillis = nowMillis + ttlMillis;
                    Date exp = new Date(expMillis);
                    builder.setExpiration(exp);
                }
                 
                 //Builds the JWT and serializes it to a compact, URL-safe string
                return builder.compact();
                
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
            
        }
        else
            return "Error: encodedKey is empty ";
         

    }   
    
    private static void parseJWT(String jwt, String encodedKey) {
      
      //System.out.println("jwt: "+jwt);
      System.out.println("encodedKey in parseJWT: "+encodedKey);
      
      //This line will throw an exception if it is not a signed JWS (as expected)
      byte[] parsed = DatatypeConverter.parseBase64Binary(encodedKey);
      
      Claims claims = Jwts.parser().setSigningKey(parsed).parseClaimsJws(jwt).getBody();
         
      /*System.out.println("ID: " + claims.getId());
      System.out.println("Subject: " + claims.getSubject());
      System.out.println("Issuer: " + claims.getIssuer());
      System.out.println("Expiration: " + claims.getExpiration());*/
   
   }   
}
