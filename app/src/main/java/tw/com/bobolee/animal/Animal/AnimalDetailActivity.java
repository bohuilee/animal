package tw.com.bobolee.animal.Animal;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.AnimalApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Shelter.ShelterDetail;
import tw.com.bobolee.animal.Utils.AnimalShare;
import tw.com.bobolee.animal.Utils.CatchScreen;
import tw.com.bobolee.animal.GoogleAnalytics.GoogleAnalytics;
import tw.com.bobolee.animal.MainActivity;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.R;

public class AnimalDetailActivity extends AppCompatActivity {
    public AnimalApiAdapter animalApiAdapter;
    private List<View> ContentView = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        Bundle b = getIntent().getExtras();
        String AnimalId = b.getString("AnimalId");

        animalApiAdapter = new AnimalApiAdapter();
        GoogleAnalytics();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Observable<ApiResponse<Animal>> observable = animalApiAdapter.getAnimalDetail(AnimalId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<Animal>>(AnimalDetailActivity.this) {
                    @Override
                    public void Next(ApiResponse<Animal> response) {
                        setUpAnimalView(response.getData());
                    }
                });
    }

    private void setUpAnimalView(final Animal animal) {
        final ImageButton AnimalFavoriteButton = (ImageButton) findViewById(R.id.AnimalFavoriteButton);
        Glide.with(this.getApplicationContext()).load(animal.isFavorite() ? R.drawable.ic_heart_full : R.drawable.ic_heart_empty)
                .apply(new RequestOptions()
                        .centerInside())
                .into(AnimalFavoriteButton);
        AnimalFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AnimalId = animal.getAnimalId();
                if (animal.isFavorite()) {
                    animal.removeFavorite();
                    Glide.with(getApplicationContext()).load(R.drawable.ic_heart_empty)
                            .apply(new RequestOptions()
                                    .centerInside())
                            .into(AnimalFavoriteButton);
                    if (MainActivity.mNewAnimalList.isAdded()) {
                        MainActivity.mNewAnimalList.notifyFavoriteImgChange(AnimalId);
                    }
                    if (MainActivity.mExpiringAnimalList.isAdded()) {
                        MainActivity.mExpiringAnimalList.notifyFavoriteImgChange(AnimalId);
                    }
                    if (MainActivity.mFavoriteAnimalList.isAdded()) {
                        MainActivity.mFavoriteAnimalList.removeFavoriteList(AnimalId);
                    }

                } else {
                    animal.addFavorite();
                    Glide.with(getApplicationContext()).load(R.drawable.ic_heart_full)
                            .apply(new RequestOptions()
                                    .centerInside())
                            .into(AnimalFavoriteButton);
                    if (MainActivity.mNewAnimalList.isAdded()) {
                        MainActivity.mNewAnimalList.notifyFavoriteImgChange(AnimalId);
                    }
                    if (MainActivity.mExpiringAnimalList.isAdded()) {
                        MainActivity.mExpiringAnimalList.notifyFavoriteImgChange(AnimalId);
                    }
                    if (MainActivity.mFavoriteAnimalList.isAdded()) {
                        MainActivity.mFavoriteAnimalList.addFavoriteList(animal.getAnimalId(),
                                animal.getAnimalAreaName(),
                                animal.getAnimalSexName(),
                                animal.getAnimalAlbumFile());
                    }
                }
            }
        });

        final ImageButton AnimalShareButton = (ImageButton) findViewById(R.id.AnimalShareButton);
        Glide.with(getApplicationContext()).load(R.drawable.ic_share)
                .apply(new RequestOptions()
                        .centerInside())
                .into(AnimalShareButton);
        AnimalShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimalShare AnimalShare = new AnimalShare(getApplicationContext(), animal);

            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(animal.getAnimalAlbumFile())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.animal_prints)
                        .placeholder(R.drawable.animal_prints)
                        .centerInside())
                .into(imageView);

        LinearLayout AnimalInfoRelativeLayout = (LinearLayout) findViewById(R.id.AnimalInfoLinearLayout);
        TextView Title;
        TextView Content;

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AnimalSubID);
        Content.setText(animal.getAnimalSubId());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AdoptBegin);
        Content.setText(animal.getAnimalOpenDate());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.AdoptEnd);
        String AnimalClosedDate = animal.getAnimalClosedDate();
        if (AnimalClosedDate.contains("2999")) {
            AnimalClosedDate = getResources().getString(R.string.None);
        } else {
            try {
                SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Date DateCurrent = DateFormat.parse(date);
                Date DateClose = DateFormat.parse(AnimalClosedDate);
                Long LongCurrent = DateCurrent.getTime();
                Long LongClose = DateClose.getTime();
                Long day = (LongClose - LongCurrent) / (1000 * 60 * 60 * 24);
                AnimalClosedDate = AnimalClosedDate + " (" + getResources().getString(R.string.LeftDay1) + day + getResources().getString(R.string.LeftDay2) + ")";
                if (day <= 3) {
                    Content.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            } catch (Exception e) {
                Log.e("My App", "SimpleDateFormat Error");
            }
        }
        Content.setText(AnimalClosedDate);
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Location);
        Content.setText(animal.getAnimalAreaName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Shelter);
        Content.setText(animal.getAnimalShelterName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Sex);
        Content.setText(animal.getAnimalSexName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.BodyType);
        Content.setText(animal.getAnimalBodyTypeName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Neuter);
        Content.setText(animal.getAnimalSterilizationName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.RabiesVaccine);
        Content.setText(animal.getAnimalBacterinName());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container1, null));
        Title = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleTextView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setText(R.string.Remark);
        Content.setText(animal.getAnimalRemark());
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        ContentView.add(View.inflate(this, R.layout.text_container2, null));
        ImageView ImageTitle = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Glide.with(this).load(R.drawable.ic_caution)
                .apply(new RequestOptions()
                        .centerInside())
                .into(ImageTitle);
        Content.setText(R.string.CautionSubID);
        Content.setTextColor(this.getResources().getColor(R.color.colorPrimary));
        Content.setTypeface(null, Typeface.BOLD);
        AnimalInfoRelativeLayout.addView(ContentView.get(ContentView.size() - 1));

        //收容所資訊
        LinearLayout ShelterInfoRelativeLayout = (LinearLayout) findViewById(R.id.ShelterInfoLinearLayout);
        ShelterDetail.addShelterDetailView(AnimalDetailActivity.this, animal.getAnimalShelterPkId(), ShelterInfoRelativeLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void GoogleAnalytics() {
        Tracker mTracker;
        GoogleAnalytics application = (GoogleAnalytics) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("tw.com.bobolee.animal.AnimalDetail");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
