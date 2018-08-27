package tw.com.bobolee.animal.Http;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class sendPostDataToInternet {
    private String uriAPI;
    private String[] Params;

    public sendPostDataToInternet(String uriAPI_p, String... Params_p){
        uriAPI = uriAPI_p;
        Params = Params_p;
    }

    public String run() {
        if(uriAPI==null){
            return null;
        }

        HttpURLConnection c = null;
        try {
            //connection setting
            URL u = new URL(uriAPI);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(100000);
            c.setReadTimeout(100000);
            c.setDoInput(true);
            c.setDoOutput(true);

            //parameters
            Uri.Builder builder = new Uri.Builder();

            for(int i=0;i<Params.length;i++){
                String ParamName;
                String ParamValue;

                ParamName = Params[i].substring(0,Params[i].indexOf(":"));
                ParamValue = Params[i].substring(Params[i].indexOf(":")+1,Params[i].length());

                builder.appendQueryParameter(ParamName,ParamValue);
            }

            String query = builder.build().getEncodedQuery();
            c.setFixedLengthStreamingMode(query.getBytes().length);
            OutputStream os = c.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            //connect
            c.connect();
            int status = c.getResponseCode();

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
                    return sb.toString();
            }

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
