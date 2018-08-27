package tw.com.bobolee.animal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.GlobalClass.GlobalVariable;
import tw.com.bobolee.animal.Http.sendGetDataToInternet;
import tw.com.bobolee.animal.Model.Animal;

public class WelcomeActivity extends Activity {
    //宣告Handler物件 等等用來執行延遲開啟MainActivity
    final Handler startMainActivity_Handler = new Handler();
    //宣告Runnable物件,內容是開啟MainActivity
    final Runnable startMainActivity_Runnable = new Runnable() {
        @Override
        public void run() {
            startMainActivity();
        }
    };

    private SQLiteDatabase db;
    private RelativeLayout RLProgress;
    private TextView Loading;

    private List<String> WelcomeDrawableFile= new ArrayList<String>(){{
        add("pic_welcome1");
        add("pic_welcome2");
        add("pic_welcome3");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        ImageView WelcomeImageView = (ImageView) findViewById(R.id.WelcomeImageView);
        WelcomeImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                getResources().getIdentifier(WelcomeDrawableFile.get(new Random().nextInt(WelcomeDrawableFile.size())), "drawable",
                        getPackageName())));

        GlobalVariable.initialize();

        RLProgress = (RelativeLayout) findViewById(R.id.RLProgress);
        Loading = (TextView) findViewById(R.id.loading);

        // check if you are connected or not
        if (!isConnected()) {
            startMainActivity_Handler.removeCallbacks(startMainActivity_Runnable);
            Toast.makeText(getBaseContext(), getResources().getString(R.string.CheckNetworkState), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    WelcomeActivity.this.finish();
                    System.exit(1);
                }
            }, 2000);
        }

        MySQLite SQLite = MySQLite.getInstance(getApplicationContext());
        db = SQLite.getWritableDatabase();
        GlobalVariable.setDB(db);

        CheckFavorite CheckFavorite = new CheckFavorite();
        CheckFavorite.execute();
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo.isConnected();
    }

    private class CheckFavorite extends AsyncTask<Integer, Float, Void> {
        private int RLProgressWidth;
        private int RLProgressHeight;

        private List<ImageView> ListDogFootPrint = new ArrayList<>();
        private int ListDogFootPrintIndex = 0;
        private int NextImagePercent = 0;
        private int DogFootPrintSlot = 7;
        private boolean setDogFootPrintHeight = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Loading.setVisibility(View.VISIBLE);
            RLProgress.setVisibility(View.VISIBLE);
            Loading.setText(getResources().getString(R.string.CheckingVersion));
        }

        @Override
        protected Void doInBackground(Integer... params) {
            db.beginTransaction();

            String DataSqlStatement;
            DataSqlStatement = "SELECT " + MySQLite.FavoriteAnimalIdColumn
                    +" FROM " + MySQLite.FavoriteTable
                    +" ORDER BY " + MySQLite.FavoriteIdColumn;
            Cursor cursor = db.rawQuery(DataSqlStatement, null);

            int CurrentIndex=0;
            while (cursor.moveToNext()) {
                String ResultString;
                String uriAPI = getApplicationContext().getResources().getString(R.string.api_url)+"Animal/AnimalData?AnimalId="+cursor.getString(0);
                sendGetDataToInternet sPDT1 = new sendGetDataToInternet(uriAPI);
                ResultString = sPDT1.run();
                Gson gson = new Gson();
                Animal animal =  gson.fromJson(ResultString, Animal.class);

                //存在SERVER就更新，不存在就刪除
                if(animal==null){
                    //將我的最愛動物刪除
                    db.execSQL("DELETE FROM " + MySQLite.FavoriteTable
                            + " WHERE " + MySQLite.FavoriteAnimalIdColumn + " = '" + cursor.getString(0) + "'");
                }else{
                    //將我的最愛動物的照片URL跟地區更新
                    db.execSQL("UPDATE " + MySQLite.FavoriteTable
                            + " SET "+ MySQLite.FavoriteAreaNameColumn + " = '" + animal.getAnimalAreaName() + "'"
                            + " , "+ MySQLite.FavoriteSexNameColumn + " = '" + animal.getAnimalSexName() + "'"
                            + " , "+ MySQLite.FavoriteAlbumFileColumn + " = '" + animal.getAnimalAlbumFile() + "'"
                            + " WHERE " + MySQLite.FavoriteAnimalIdColumn + " = '" + cursor.getString(0) + "'");
                }
                float percent = (((float) CurrentIndex+1) / (float) cursor.getCount()) * 100;
                this.publishProgress(percent);
                CurrentIndex++;
            }
            cursor.close(); //關閉Cursor
            db.setTransactionSuccessful();
            db.endTransaction();

            String ResultString;
            String uriAPI = getApplicationContext().getResources().getString(R.string.api_url)+"Search/SearchCondition";
            sendGetDataToInternet sPDT1 = new sendGetDataToInternet(uriAPI);
            ResultString = sPDT1.run();
            try {
                JSONObject ResultJSONObject = new JSONObject(ResultString);
                JSONArray ContentJSONArray;

                ContentJSONArray = ResultJSONObject.getJSONArray("Area");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAreaList(ContentJSONArray.getJSONObject(i).getString("AreaName"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalColour");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalColourList(ContentJSONArray.getJSONObject(i).getString("color"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalKind");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalKindList(ContentJSONArray.getJSONObject(i).getString("AnimalKindName"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalSex");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalSexList(ContentJSONArray.getJSONObject(i).getString("AnimalSexName"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalBodyType");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalBodyTypeList(ContentJSONArray.getJSONObject(i).getString("AnimalBodyName"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalSterilization");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalSterilizationList(ContentJSONArray.getJSONObject(i).getString("AnimalSterilizationName"));
                }

                ContentJSONArray = ResultJSONObject.getJSONArray("AnimalBacterin");
                for (int i=0;i<ContentJSONArray.length();i++) {
                    GlobalVariable.addAnimalBacterinList(ContentJSONArray.getJSONObject(i).getString("AnimalBacterinName"));
                }

            } catch (Exception e) {
                Log.e("MYAPP", "GetSearchConditionAsyncTask",e);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);

            RLProgressWidth = RLProgress.getWidth();
            RLProgressHeight = RLProgress.getHeight();

            Loading.setText(getResources().getString(R.string.DownloadingData) + String.format("%.0f", values[0]) + "%");

            //DogImgFootPrint
            //if (Math.round(values[0]) == NextImagePercent+DogFootPrintSlot) {
            int a = (int) Math.ceil((Math.round(values[0])-(NextImagePercent+DogFootPrintSlot))/DogFootPrintSlot);
            for(int i=0 ; i<=a ; i++){
                ListDogFootPrint.add(new ImageView(WelcomeActivity.this));
                ListDogFootPrintIndex = ListDogFootPrint.size() - 1;
                ListDogFootPrint.get(ListDogFootPrintIndex).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.dogfootprint));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RLProgressHeight/4, RLProgressHeight/4);
                layoutParams.leftMargin = (int) (RLProgressWidth * ((float) NextImagePercent / (float) 100));

                if (setDogFootPrintHeight) {
                    layoutParams.topMargin = RLProgressHeight/3;
                } else {
                    layoutParams.topMargin = RLProgressHeight/2;
                }
                setDogFootPrintHeight = !setDogFootPrintHeight;

                ListDogFootPrint.get(ListDogFootPrintIndex).setLayoutParams(layoutParams);
                RLProgress.addView(ListDogFootPrint.get(ListDogFootPrintIndex));

                NextImagePercent += DogFootPrintSlot;
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            //執行後 完成背景任務
            super.onPostExecute(result);
            startMainActivity();
        }
    }

    @Override
    public void onBackPressed() {
        //解除Handler
        startMainActivity_Handler.removeCallbacks(startMainActivity_Runnable);
        WelcomeActivity.this.finish();
        System.exit(0);
    }

    //開啟MainActivity 並結束WelcomeActivity
    protected void startMainActivity() {
        final Intent i = new Intent(getApplicationContext(), MainActivity.class);
        //Toast.makeText(WelcomeActivity.this,result, Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                WelcomeActivity.this.finish();
            }
        }, 100);
    }
}
