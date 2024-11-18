package Auth;

import Utils.CognitoRSAKeyProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.example.demo.awsServices.Credentials;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@ApplicationScoped
public class CognitoService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final Credentials creds = Credentials.getInstance();

    private JWTVerifier jwtVerifier;

    public CognitoService() {
        this.cognitoClient = CognitoIdentityProviderClient.builder()
                .region(Region.of(creds.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                        creds.getAWSAccessKeyId(),
                        creds.getAWSSecretKey())))
                .build();

        RSAKeyProvider keyProvider = new CognitoRSAKeyProvider();
        Algorithm algorithm = Algorithm.RSA256(keyProvider);
        this.jwtVerifier = JWT.require(algorithm).build();
    }

    private String calculateSecretHash(String username) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(creds.getCognitoClientSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            mac.update(username.getBytes(StandardCharsets.UTF_8));
            byte[] rawHmac = mac.doFinal(creds.getCognitoClientId().getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error while calculating secret hash", e);
        }
    }

    public void signUp(String email, String password) {
        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("email", email);

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .clientId(creds.getCognitoClientId())
                .username(email)
                .password(password)
                .secretHash(calculateSecretHash(email))
                .userAttributes(userAttributes.entrySet().stream()
                        .map(entry -> AttributeType.builder().name(entry.getKey()).value(entry.getValue()).build())
                        .toList())
                .build();

        cognitoClient.signUp(signUpRequest);
    }

    public Map<String, String> login(String email, String password) {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", email);
        authParams.put("PASSWORD", password);
        authParams.put("SECRET_HASH", calculateSecretHash(email));

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .userPoolId(creds.getCognitoUserPoolId())
                .clientId(creds.getCognitoClientId())
                .authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                .authParameters(authParams)
                .build();

        AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("refreshToken", authResponse.authenticationResult().refreshToken());
        tokens.put("idToken", authResponse.authenticationResult().idToken());
        tokens.put("accessToken", authResponse.authenticationResult().accessToken());
        return tokens;
    }
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    public Map<String, String> refreshTokens(String accessToken,String refreshToken) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] parts = accessToken.split("\\.");
        JSONObject payload = new JSONObject(decode(parts[1]));
        var username = payload.getString("username");
        Map<String, String> authParams = new HashMap<>();
        authParams.put("REFRESH_TOKEN", refreshToken);
        authParams.put("SECRET_HASH", calculateSecretHash(username));

        AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
                .userPoolId(creds.getCognitoUserPoolId())
                .clientId(creds.getCognitoClientId())
                .authFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                .authParameters(authParams)
                .build();

        AdminInitiateAuthResponse authResponse = cognitoClient.adminInitiateAuth(authRequest);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("idToken", authResponse.authenticationResult().idToken());
        tokens.put("accessToken", authResponse.authenticationResult().accessToken());
        return tokens;
    }

    public boolean isEmailExists(String email) {
        try {
            AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                    .userPoolId(creds.getCognitoUserPoolId())
                    .username(email) // Use email as username
                    .build();
            cognitoClient.adminGetUser(getUserRequest);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public boolean isEmailVerified(String email) {
        try {
            AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                    .userPoolId(creds.getCognitoUserPoolId())
                    .username(email)
                    .build();

            AdminGetUserResponse getUserResponse = cognitoClient.adminGetUser(getUserRequest);

            for (AttributeType attribute : getUserResponse.userAttributes()) {
                if (attribute.name().equals("email_verified")) {
                    return Boolean.parseBoolean(attribute.value());
                }
            }
            return false;
        } catch (UserNotFoundException e) {
            // Handle user not found case
            e.printStackTrace();
            return false;
        }
    }
}

