package Utils;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.example.demo.awsServices.Credentials;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class CognitoRSAKeyProvider implements RSAKeyProvider {

    private final URL aws_kid_store_url;
    private final JwkProvider provider;

    private final Credentials creds = Credentials.getInstance();

    public CognitoRSAKeyProvider() {
        String url = String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", creds.getRegion(), creds.getCognitoUserPoolId());
        try {
            aws_kid_store_url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("Invalid URL provided, URL=%s", url));
        }
        provider = new JwkProviderBuilder(aws_kid_store_url).build();
    }


    @Override
    public RSAPublicKey getPublicKeyById(String kid) {
        try {
            return (RSAPublicKey) provider.get(kid).getPublicKey();
        } catch (JwkException e) {
            throw new RuntimeException(String.format("Failed to get JWT kid=%s from aws_kid_store_url=%s", kid, aws_kid_store_url));
        }
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return null;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
