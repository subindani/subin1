import javax.xml.bind.DatatypeConverter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

public class parseJWT
{
   //Sample method to validate and read the JWT
   private void parseJWT(String jwt) {
   //This line will throw an exception if it is not a signed JWS (as expected)
   /*Claims claims = Jwts.parser()         
      .setSigningKey(DatatypeConverter.parseBase64Binary(apiKey.getSecret()))
      .parseClaimsJws(jwt).getBody();
   System.out.println("ID: " + claims.getId());
   System.out.println("Subject: " + claims.getSubject());
   System.out.println("Issuer: " + claims.getIssuer());
   System.out.println("Expiration: " + claims.getExpiration());*/
   }   
}
 
