import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.Base64;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;

public class Cardtokens {
    //
    // A base64 encoded RSA public key
    //
    private static String publicKey = "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0NCk1JSUNJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBZzhBTUlJQ0NnS0NBZ0VBMW1zdE1QckZSVmQ4VE1HclkzMjQNCjJwcTQ2aFlFMFBieXcrTnB0MnRDSjBpRHkrWkxQWWJGMnVYTkg1UE9neGkzdDVIVTY2MVVTQThYOXp5N2pJTzANCjlpOGxRMkdoN1dpejlqZXpFVDBpVmNvUGovSFFrV1N1KzA5Y0RIUk5qUDJoaWtIWUEwOUlZc05vemo3eHR2ME4NCnJxbjZacWZ5amhOS1NrN2RUeUVVQ0xoaEwvTUVFRTZ0QUREVVJZb0tIVXFrVml0cFlzcE1HamlKNkFBSVlVZWENCk1DdkZ2cnhaSkFNSW5FbnY3THNhTHVBV21pdzRrOXM5M0x1MXdoM3A1bjR1a09pVWpRWEZ5Nm9NNzMwblpvb1MNCmR2U2lYUlR2UlFwMDkyZDAzbnY5Zk55cWgwM3ZoM2l5TFJja3RoVnc2ZklPN3p4cktjTXpoVmhzK3doUGVtMzkNCkRhU05oSjFrZUx4bzcyaDJIL01FMzRuQzNOSUhCUEhQZ1NBeHVDSjlCcXVVRW1idXdGMTc0eDlGOUhFYm5jRlkNClRTd1hmS3diN1cxZ0F1U1RlWmhKVXc1eDZ6a3ZUTmRTejRWaFFjT051SjJ6am1VdGdSK3FXc1NjOUh2N1RGREgNCjlQbCt5NmQxeVJ0Rmp2TmlqeGZQUmo5a1dKbVJvcnBVVExUMTh2dThlbzg1aWNLTVY1VmladDMweGxpc1RVTjANCjJOWkxjNG83TVdraHE1eGhGcXhmZDdTZXZEc1FLa0VpenlRbi9zOUpZNmsybEtQUG4wTXk1UjdURWtBZEhVREUNCklIc09qTXlrZnpwYVdoNldMK2RmRlRFVzE4MFNkRHdXbEFXaWtpYWhFT1NDRGVFMkpWTDluMjY3QzJkc0ZJZDYNCjVPczJKVjE5anl5b2VGQkhOQm11MFBjQ0F3RUFBUT09DQotLS0tLUVORCBQVUJMSUMgS0VZLS0tLS0NCg==";

    //
    // The merchantid used for the test
    //
    public static String merchantId = "523ca9d5eb9d4ce0a60b2a3f5eb3119d";

    //
    // The API key used for the test
    //
    public static String apikey = "95f734793a424ea4ae8d9dc0b8c1a4d7";

    //
    // Endpoint to Cardtokens
    //
    public static String endpoint = "https://api.cardtokens.io/";

    public static void main(String[] args) throws ClientProtocolException, IOException, Exception {
        //
        // Create the create token type object and fill with standard demo values
        //
        TokenType tt = new TokenType();
        tt.clientwalletaccountemailaddress = "thomas@cardtokens.io";
        tt.merchantid = merchantId;
        CardType ct = new CardType();
        ct.expmonth = "10";
        ct.expyear = "2026";
        ct.pan = "4111111145551142";
        ct.securitycode = "000";

        //
        // Transform the object into  a JSON string
        //
        String jcard = new Gson().toJson(ct);

        //
        // Now encrypt, create token, create cryptogram, get status and delete the token
        //
        try {
            //
            // Decode the base64 encoded pem key
            //
            String decodedKey = new String(Base64.getDecoder().decode(publicKey));
            //
            // Trim the key for header, footer and whitespaces
            //
            String pubKey = decodedKey.replace("-----BEGIN PUBLIC KEY-----", "")
                                 .replace("-----END PUBLIC KEY-----", "")
                                 .replaceAll("\\s", ""); 
            
            //
            // Now set the enccard to the encrypted base64 value
            //
            tt.enccard = CardtokensEncryptor.Encrypt(jcard, pubKey);

            //
            // Now request Cardtokens to create a token on behalf of the data
            //
            TokenResponseType tokenResponse = CardtokensHttp.postJson(new Gson().toJson(tt), apikey, endpoint + "/api/token", new TypeToken<TokenResponseType>(){}.getType());

            //
            // If Cardtokens retuned by success
            //
            if (tokenResponse.Statuscode == 200) {
                //
                // Validate that we actually did receive a cryptogram
                //
                if (tokenResponse.tokenid.length() == 0) {
                    throw new Exception("Expected to receive a token");
                }

                //
                // Now create the cryptogram request
                //
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("reference", "123j12323");
                jsonObject.addProperty("transactiontype", "ecom");
                jsonObject.addProperty("unpredictablenumber", "87654321");

                //
                // POST to Cardtokens
                //
                CryptogramResponseType cryptogramResponseResponse = CardtokensHttp.postJson(jsonObject.toString(), apikey, endpoint + "/api/token/" + tokenResponse.tokenid + "/cryptogram", new TypeToken<CryptogramResponseType>(){}.getType());

                //
                // If Cardtokens returned by success
                //
                if (cryptogramResponseResponse.Statuscode == 200) {
                    //
                    // Validate that we actually did receive a cryptogram
                    //
                    if (cryptogramResponseResponse.cryptogram.length() == 0) {
                        throw new Exception("Expected to receive a cryptogram");
                    }

                    //
                    // Now request the token status
                    //
                    StatusResponseType tokenStatus = CardtokensHttp.get(apikey, endpoint + "/api/token/" + tokenResponse.tokenid + "/status", new TypeToken<StatusResponseType>(){}.getType());

                    //
                    // If Cardtokens returned by success
                    //
                    if (tokenStatus.Statuscode == 200) {
                        //
                        // Validate that the status is ACTIVE, which is expected
                        //
                        if (!tokenStatus.status.equals("ACTIVE")) {
                            throw new Exception("Expected the token to have status ACTIVE");
                        }

                        //
                        // Now request Cardtokens to deleete the token
                        //
                        DeleteResponseType tokenDelete = CardtokensHttp.delete(apikey, endpoint + "/api/token/" + tokenResponse.tokenid + "/delete", new TypeToken<DeleteResponseType>(){}.getType());

                        //
                        // If Cardtokens returned by success
                        //
                        if (tokenDelete.Statuscode == 200) {
                            System.out.println("token deleted");
                        }
                    }
                }
            }
        } catch (Exception e) {
            //
            // Very basic error handling - simply print the errror
            //
            System.out.println("Unhanled exception occured!");
            System.out.println(e);
        }
    }
}
