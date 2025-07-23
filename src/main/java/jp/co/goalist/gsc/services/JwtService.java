package jp.co.goalist.gsc.services;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

import jp.co.goalist.gsc.enums.Role;
import jp.co.goalist.gsc.enums.ScreenPermission;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jp.co.goalist.gsc.entities.Account;
import jp.co.goalist.gsc.utils.GeneralUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtService {

    private Long expirationDays;

    private String privateKeyStr;
    private String publicKeyStr;

    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;

    public JwtService(@Value("${gsc-be-core.jwt.private-key}") String privateKeyStr,
            @Value("${gsc-be-core.jwt.public-key}") String publicKeyStr,
            @Value("${gsc-be-core.jwt.expiration-days}") Long expirationDays) {
        this.privateKeyStr = privateKeyStr;
        this.publicKeyStr = publicKeyStr;
        this.expirationDays = expirationDays;
        privateKey = getSigningKey();
        publicKey = getVerificationKey();
    }

    public Long getExpiration() {
        return expirationDays * 24 * 60 * 60 * 1000;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String createToken(Map<String, String> claims, String subject) {
        return Jwts
                .builder()
                .signWith(privateKey)
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getExpiration()))
                .compact();
    }

    public String generateToken(Account account, String proxyLoginAccount, boolean isProxyLogin, List<String> permissions) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", account.getUsername());
        claims.put("role", account.getRole());
        claims.put("subRole", account.getSubRole());
        claims.put("id", account.getId());

        if (Objects.nonNull(permissions) && !permissions.isEmpty()) {
            claims.put("permissions", Strings.join(permissions, ','));
        }
        
        if (isProxyLogin) {
            claims.put("proxyLoginAccount", proxyLoginAccount);
            claims.put("isProxyLogin", String.valueOf(true)); 
        } else if (Objects.equals(account.getRole(), Role.CLIENT.getId())) {
            claims.put("isProxyLogin", String.valueOf(false));
        }
        return createToken(claims, account.getUsername());
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private RSAPrivateKey getSigningKey() {
        String privateKeyTemp = GeneralUtils.convertBase64ToString(privateKeyStr);
        String privateKeyPEM = privateKeyTemp
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Can not load the private key", e);
            e.printStackTrace();
            return null;
        }
    }

    private RSAPublicKey getVerificationKey() {
        String publicKeyTemp = GeneralUtils.convertBase64ToString(publicKeyStr);
        String publicKeyPEM = publicKeyTemp
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error("Can not load the public key", e);
            e.printStackTrace();
            return null;
        }
    }

    public boolean isTokenValid(String token, Account account) {
        final String username = extractUsername(token);
        return (username.equals(account.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String getPermissions(String token) {
        Claims claims = extractAllClaims(token);
        if (claims.containsKey("permissions")) {
            return claims.get("permissions", String.class);
        } else {
            return null;
        }
    }
}
