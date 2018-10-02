package xyz.a4tay.dev.hotspot;

import android.os.StrictMode;
import com.google.android.gms.maps.GoogleMap;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;


public class DotProtocols
    {
    GoogleMap map;
    OkHttpClient client = new OkHttpClient();

    public void enableStrictMode()
        {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        }

    public JSONObject getDots(Double currentCameraLat, Double currentCameraLng) throws Exception
        {
        enableStrictMode();

        Request request = new Request.Builder()
                .url("https://eg75gef3gi.execute-api.us-east-1.amazonaws.com/alpha?lat=" + currentCameraLat + "&lng=" + currentCameraLng)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject JSONresult = new JSONObject(response.body().string());
        return JSONresult;
        }

    public Response putDot(double lat, double lng, Integer colorCode, Double dotID)
        {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\"body\":\r\n\t{\r\n\t\"lat\":"+lat+",\r\n\t\"lng\":"+lng+"," +
                "\r\n\t\"locationID\":987654321,\r\n\t\"colorCode\":"+colorCode+",\r\n\t\"hash\":'this is a test'\r\n\t}\r\n}\r\n");
        Request request = new Request.Builder()
                .url("https://eg75gef3gi.execute-api.us-east-1.amazonaws.com/alpha")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response=null;
        {
        try
            {
            response = client.newCall(request).execute();
            return response;
            } catch (IOException e)
            {
            e.printStackTrace();
            }
        }
        return response;
        }
        public Response putDot(double lat, double lng, Integer colorCode, Double dotID, String hash)
        {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\"body\":\r\n\t{\r\n\t\"lat\":"+lat+",\r\n\t\"lng\":"+lng+"," +
                    "\r\n\t\"locationID\":123,\r\n\t\"colorCode\":"+colorCode+",\r\n\t\"hash\":"+hash+"\r\n\t}\r\n}\r\n");
            Request request = new Request.Builder()
                    .url("https://eg75gef3gi.execute-api.us-east-1.amazonaws.com/alpha")
                    .put(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response=null;
            {
                try
                {
                    response = client.newCall(request).execute();
                    return response;
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }
