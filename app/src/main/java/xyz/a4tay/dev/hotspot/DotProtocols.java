package xyz.a4tay.dev.hotspot;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import okhttp3.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;


public class DotProtocols
    {
    GoogleMap map;

    public void enableStrictMode()
        {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        }

    public JSONObject getDots(Double currentCameraLat, Double currentCameraLng) throws Exception
        {
        enableStrictMode();

        StringBuilder result = new StringBuilder();
        URL hsURL = new URL("https://eg75gef3gi.execute-api.us-east-1.amazonaws.com/alpha?lat=" + currentCameraLat + "&lng=" + currentCameraLng);
        HttpURLConnection hsConnection = (HttpURLConnection) hsURL.openConnection();
        hsConnection.setRequestMethod("GET");
        BufferedReader hsBR = new BufferedReader(new InputStreamReader(hsConnection.getInputStream()));
        String line;
        while ((line = hsBR.readLine()) != null)
            {
            result.append(line);
            }
        hsBR.close();
        JSONObject JSONresult = new JSONObject(result.toString());
        return JSONresult;
        }

    public Marker dotMaker(String dotID, double lat, double lng, String hash, Integer colorCode)
        {
        Marker dot = map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(hash));

        return dot;
        }

    public Response putDot(double lat, double lng, Integer colorCode, Double dotID)
        {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n\"body\":\r\n\t{\r\n\t\"lat\":"+lat+",\r\n\t\"lng\":"+lng+",\r\n\t\"locationID\":987654321,\r\n\t\"colorCode\":4\r\n\t\"hash\":''\r\n\t}\r\n}\r\n");
        Request request = new Request.Builder()
                .url("https://eg75gef3gi.execute-api.us-east-1.amazonaws.com/alpha")
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0fa333f0-a6cd-44ee-9c85-937555b95324")
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
