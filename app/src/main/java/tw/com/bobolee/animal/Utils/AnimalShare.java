package tw.com.bobolee.animal.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Animal.AnimalDetailActivity;
import tw.com.bobolee.animal.Http.ApiAdapter.AnimalApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Http.sendGetDataToInternet;

/**
 * Created by bohuilee on 2016/4/12.
 */

public class AnimalShare {
    private Context mContext;
    private Animal mAnimal;
    private View mAnimalShareView;
    private List<View> ContentView = new ArrayList<>();

    public AnimalShare(Context context, String AnimalId) {
        this.mContext = context;
        AnimalApiAdapter animalApiAdapter = new AnimalApiAdapter();

        Observable<ApiResponse<Animal>> observable = animalApiAdapter.getAnimalDetail(AnimalId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<Animal>>(mContext) {
                    @Override
                    public void Next(ApiResponse<Animal> response) {
                        mAnimal = response.getData();
                        AnimalShareViewInflate();
                        View view = getAnimalShareView();
                        CatchScreen CS = new CatchScreen(view, mContext);
                        CS.shareThoughIntent();
                    }
                });
    }

    public AnimalShare(Context context, Animal Animal) {
        this.mContext = context;
        this.mAnimal = Animal;

        AnimalShareViewInflate();
        View view = getAnimalShareView();
        CatchScreen CS = new CatchScreen(view, mContext);
        CS.shareThoughIntent();
    }

    public View getAnimalShareView() {
        return mAnimalShareView;
    }

    private void AnimalShareViewInflate() {
        this.mAnimalShareView = View.inflate(mContext, R.layout.animal_share, null);

        if (mAnimal.getAnimalAlbumFile().trim().length() > 0) {
            ImageView imageView = (ImageView) mAnimalShareView.findViewById(R.id.backdrop);

            try {
                GetImgAsyncTask mAsyncTask = new GetImgAsyncTask();
                imageView.setImageBitmap(mAsyncTask.execute(mAnimal.getAnimalAlbumFile()).get());
            } catch (Exception e) {
                Log.e("My App", " Error");
            }
        }

        LinearLayout AnimalInfoRelativeLayout = (LinearLayout) mAnimalShareView.findViewById(R.id.AnimalInfoLinearLayout);
        TextView Title;
        TextView Content;

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AnimalSubID);
        Content.setText(mAnimal.getAnimalSubId());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AdoptBegin);
        Content.setText(mAnimal.getAnimalOpenDate());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AdoptEnd);
        String AnimalClosedDate = mAnimal.getAnimalClosedDate();
        if (AnimalClosedDate.contains("2999")) {
            AnimalClosedDate = mContext.getResources().getString(R.string.None);
        } else {
            try {
                SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Date DateCurrent = DateFormat.parse(date);
                Date DateClose = DateFormat.parse(AnimalClosedDate);
                Long LongCurrent = DateCurrent.getTime();
                Long LongClose = DateClose.getTime();
                Long day = (LongClose - LongCurrent) / (1000 * 60 * 60 * 24);
                AnimalClosedDate = AnimalClosedDate + " (" + mContext.getResources().getString(R.string.LeftDay1) + day + mContext.getResources().getString(R.string.LeftDay2) + ")";
                if (day <= 3) {
                    Content.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                }
            } catch (Exception e) {
                Log.e("My App", "SimpleDateFormat Error");
            }
        }
        Content.setText(AnimalClosedDate);
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Location);
        Content.setText(mAnimal.getAnimalAreaName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Shelter);
        Content.setText(mAnimal.getAnimalShelterName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Sex);
        Content.setText(mAnimal.getAnimalSexName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.BodyType);
        Content.setText(mAnimal.getAnimalBodyTypeName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Neuter);
        Content.setText(mAnimal.getAnimalSterilizationName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.RabiesVaccine);
        Content.setText(mAnimal.getAnimalBacterinName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Remark);
        Content.setText(mAnimal.getAnimalRemark());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(mContext, R.layout.text_container2, null));
        ImageView ImageTitle = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        ImageTitle.setImageDrawable(ContextCompat.getDrawable(mContext, mContext.getResources().getIdentifier("ic_caution", "drawable", mContext.getPackageName())));
        Content.setText(R.string.CautionSubID);
        Content.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        Content.setTypeface(null, Typeface.BOLD);
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        //收容所資訊
        /*
        LinearLayout ShelterInfoRelativeLayout = (LinearLayout) mAnimalShareView.findViewById(R.id.ShelterInfoLinearLayout);
        ShelterDetail ShelterDetail=new ShelterDetail(mContext,mAnimal.getAnimalShelterPkId());
        ShelterDetail.addView(ShelterInfoRelativeLayout);
        */
    }

    private class GetDataAsyncTask extends AsyncTask<String, Animal, Animal> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Animal doInBackground(String... params) {
            String AnimalId = params[0];
            String ResultString;
            String uriAPI = mContext.getResources().getString(R.string.api_url) + "Animal/AnimalData?AnimalId=" + AnimalId;
            sendGetDataToInternet sPDT1 = new sendGetDataToInternet(uriAPI);
            ResultString = sPDT1.run();
            Gson gson = new Gson();
            return gson.fromJson(ResultString, Animal.class);
        }

        @Override
        protected void onPostExecute(Animal result) {
            super.onPostExecute(result);
        }
    }

    private class GetImgAsyncTask extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bmp = null;
            try {
                String albumFile = params[0];
                URL url = new URL(albumFile);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.e("MYAPP", "", e);
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
        }
    }
}
