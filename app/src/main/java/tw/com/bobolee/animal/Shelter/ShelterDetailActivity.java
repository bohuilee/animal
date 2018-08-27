package tw.com.bobolee.animal.Shelter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.GoogleAnalytics.GoogleAnalytics;
import tw.com.bobolee.animal.Http.ApiAdapter.ShelterApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Model.Shelter;

public class ShelterDetailActivity extends AppCompatActivity {
    private ShelterApiAdapter shelterApiAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_detail);

        shelterApiAdapter = new ShelterApiAdapter();
        GoogleAnalytics();

        Bundle b = getIntent().getExtras();
        String ShelterId = b.getString("ShelterId");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Observable<ApiResponse<Shelter>> observable = shelterApiAdapter.getShelterData(ShelterId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<Shelter>>(ShelterDetailActivity.this) {
                    @Override
                    public void Next(ApiResponse<Shelter> response) {
                        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
                        LinearLayout ShelterInfoRelativeLayout = (LinearLayout) findViewById(R.id.ShelterInfoLinearLayout);

                        Glide.with(ShelterDetailActivity.this).load(response.getData().getShelterPic())
                                .apply(new RequestOptions()
                                        .error(R.drawable.animal_prints)
                                        .centerCrop())
                                .into(imageView);

                        //收容所資訊
                        ShelterDetail.addShelterDetailView(ShelterDetailActivity.this, response.getData(), ShelterInfoRelativeLayout);
                    }
                });
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
        mTracker.setScreenName("tw.com.bobolee.animal.Model.Shelter.ShelterDetail");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
