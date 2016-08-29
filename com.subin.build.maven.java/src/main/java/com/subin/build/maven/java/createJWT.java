import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;   
import javax.crypto.SecretKey;
import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class createJWT {
    //Sample method to construct a JWT
    private String createJWT(String id, String issuer, String subject, long ttlMillis) {
     
    //The JWT signature algorithm we will be using to sign the token
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    //SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
     
    long nowMillis = System.currentTimeMillis();
    Date now = new Date(nowMillis);
     
    //create new key
    SecretKey secretKey = null;
    try {
        secretKey = KeyGenerator.getInstance("AES").generateKey();
        System.out.println("secretKey: "+secretKey);
    } catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
     
    //get base64 encoded version of key
    String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
     
    //We will sign our JWT with our ApiKey secret
    //byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey.getSecret());
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(encodedKey);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
     
      //Let's set the JWT Claims
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
    }    
}
