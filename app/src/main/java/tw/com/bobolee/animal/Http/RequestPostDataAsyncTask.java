package tw.com.bobolee.animal.Http;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by bohuilee on 2016/10/4.
 */


public class RequestPostDataAsyncTask extends AsyncTask<String, String, String>{
    private String Url;
    private String[] Params;

    public RequestPostDataAsyncTask(String Url, String[] Params){
        this.Url = Url;
        this.Params = Params;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params)
    {
        String ResultString;
        sendPostDataToInternet sPDT1 = new sendPostDataToInternet(Url,Params);
        ResultString = sPDT1.run();
        Log.d("HttpResult", ResultString);

        return ResultString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
