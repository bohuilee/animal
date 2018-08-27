package tw.com.bobolee.animal.Http;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sendGetDataToInternet {
    private static final int RETRY_TIMES = 5;
    private String uriAPI;
    private int retry = 0;

    public sendGetDataToInternet(String uriAPI_p) {
        uriAPI = uriAPI_p;
    }

    public String run() {
        if (uriAPI == null) {
            return null;
        }

        HttpURLConnection c = null;
        try {
            //connection setting
            URL u = new URL(uriAPI);
            Log.d("sendGetDataToInternet", "Url : " + uriAPI);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");

            //connect
            while (retry < RETRY_TIMES) {
                retry++;
                c.connect();
                int status = c.getResponseCode();
                Log.d("sendGetDataToInternet", "Status : " + status);

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        String result = sb.toString();
                        Log.d("sendGetDataToInternet", "ApiResponse : " + result);
                        return result;
                    default:
                }
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }
}
