import java.io.IOException;
import java.lang.reflect.Type;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;

public class CardtokensHttp {
    public static <T> T postJson(String jsonInputString, String apiKey, String endpoint, Type typeOfT) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(endpoint);
        httpPost.setEntity(new StringEntity(jsonInputString, "UTF-8"));
        httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpPost.setHeader("x-api-key", apiKey);
        httpPost.setHeader("x-request-id", UUID.randomUUID().toString());

        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                System.out.println("Got response: " + result);
                Gson gson = new Gson();
                T responseObj = gson.fromJson(result, typeOfT);
                ((CardtokensResponse)responseObj).Responsebody = result;
                ((CardtokensResponse)responseObj).Statuscode = response.getStatusLine().getStatusCode();
                return responseObj;
            }
        } finally {
            response.close();
        }
        return null;
    }

    public static <T> T get(String apiKey, String endpoint, Type typeOfT) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(endpoint);
        httpGet.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpGet.setHeader("x-api-key", apiKey);
        httpGet.setHeader("x-request-id", UUID.randomUUID().toString());

        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                // convert JSON response to object of type T
                Gson gson = new Gson();
                T responseObj = gson.fromJson(result, typeOfT);
                if (responseObj instanceof CardtokensResponse) {
                    ((CardtokensResponse)responseObj).Responsebody = result;
                    ((CardtokensResponse)responseObj).Statuscode = response.getStatusLine().getStatusCode();
                }
                return responseObj;
            }
        } finally {
            response.close();
        }
        return null;
    }

    public static <T> T delete(String apiKey, String endpoint, Type typeOfT) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(endpoint);
        httpDelete.setHeader("Content-Type", "application/json; charset=UTF-8");
        httpDelete.setHeader("x-api-key", apiKey);
        httpDelete.setHeader("x-request-id", UUID.randomUUID().toString());

        CloseableHttpResponse response = httpClient.execute(httpDelete);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                System.out.println(result);

                // convert JSON response to object of type T
                Gson gson = new Gson();
                T responseObj = gson.fromJson(result, typeOfT);
                if (responseObj instanceof CardtokensResponse) {
                    ((CardtokensResponse)responseObj).Responsebody = result;
                    ((CardtokensResponse)responseObj).Statuscode = response.getStatusLine().getStatusCode();
                }
                return responseObj;
            }
        } finally {
            response.close();
        }
        return null;
    }
}