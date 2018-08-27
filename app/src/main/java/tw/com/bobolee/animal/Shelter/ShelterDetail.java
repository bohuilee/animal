package tw.com.bobolee.animal.Shelter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.ShelterApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Model.Shelter;

public class ShelterDetail {

    public static void addShelterDetailView(final Activity activity, final String shelterId, final LinearLayout container) {
        ShelterApiAdapter shelterApiAdapter;
        shelterApiAdapter = new ShelterApiAdapter();

        Observable<ApiResponse<Shelter>> observable = shelterApiAdapter.getShelterData(shelterId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<Shelter>>(activity) {
                    @Override
                    public void Next(ApiResponse<Shelter> response) {
                        addView(activity, response.getData(), container);
                    }
                });
    }

    public static void addShelterDetailView(final Activity activity, Shelter shelter, LinearLayout container) {
        addView(activity, shelter, container);
    }

    private static void addView(final Activity activity, Shelter shelter, LinearLayout Container) {
        if(shelter == null) {
            return;
        }
        View shelterDetailView = View.inflate(activity, R.layout.shelter_detail, null);
        TextView ShelterNameTextView = (TextView) shelterDetailView.findViewById(R.id.ShelterNameTextView);
        ShelterNameTextView.setText(shelter.getShelterName());

        LinearLayout ShelterInfoLinearLayout = (LinearLayout) shelterDetailView.findViewById(R.id.ShelterInfoLinearLayout);
        ImageView Title;
        TextView Content;
        ImageView ImageButton;
        List<View> ContentView = new ArrayList<>();

        ContentView.add(View.inflate(activity, R.layout.text_container3, null));
        Title = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_address", "drawable", activity.getPackageName())));
        final String Address = shelter.getShelterAddress();
        Content.setText(Address);
        ImageButton = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.PhoneCallImageButton);
        Glide.with(activity).load(R.drawable.ic_location)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(ImageButton);
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?z=0&mode=d&q=" + Address);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivity(mapIntent);
                } else {
                    Toast.makeText(activity, "無相對應地圖程式可開啟。", Toast.LENGTH_LONG).show();
                }
            }
        });


        ContentView.add(View.inflate(activity, R.layout.text_container3, null));
        Title = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_telephone", "drawable", activity.getPackageName())));
        final String PhoneNumber = shelter.getShelterPhoneNumber();
        Content.setText(PhoneNumber);
        ImageButton = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.PhoneCallImageButton);
        Glide.with(activity).load(R.drawable.ic_phonecall)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(ImageButton);
        ImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                callIntent.setData(Uri.parse("tel:" + PhoneNumber));
                activity.startActivity(callIntent);
            }
        });


        //新增view至layout
        for (int i = 0; i < ContentView.size(); i++) {
            ShelterInfoLinearLayout.addView(ContentView.get(i));
        }

        LinearLayout ShelterOpenTimeLinearLayout = (LinearLayout) shelterDetailView.findViewById(R.id.ShelterOpenTimeLinearLayout);
        List<View> ContentView2 = new ArrayList<>();
        //開放時間
        String OpenTimeString = shelter.getShelterOpenTime();
        String[] OpenTimeArray = OpenTimeString.split("/", -1);
        if (OpenTimeArray.length == 1) {
            ContentView2.add(View.inflate(activity, R.layout.text_container2, null));
            Title = (ImageView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.TitleImageView);
            Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_caution", "drawable", activity.getPackageName())));
            Content = (TextView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.ContentTextView);
            Content.setText(OpenTimeArray[0]);
        } else {
            for (int i = 1; i <= 7; i++) {
                ContentView2.add(View.inflate(activity, R.layout.text_container2, null));
                Title = (ImageView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.TitleImageView);
                Content = (TextView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.ContentTextView);

                switch (i) {
                    case 1:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_monday", "drawable", activity.getPackageName())));
                        break;
                    case 2:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_tuesday", "drawable", activity.getPackageName())));
                        break;
                    case 3:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_wednesday", "drawable", activity.getPackageName())));
                        break;
                    case 4:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_thursday", "drawable", activity.getPackageName())));
                        break;
                    case 5:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_friday", "drawable", activity.getPackageName())));
                        break;
                    case 6:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_saturday", "drawable", activity.getPackageName())));
                        //Content.setTextColor(activity.getResources().getColor(R.color.colorGreen));
                        break;
                    case 7:
                        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_sunday", "drawable", activity.getPackageName())));
                        break;
                }
                if (OpenTimeArray[i - 1].equals("")) {
                    OpenTimeArray[i - 1] = "不開放";
                }
                Content.setText(OpenTimeArray[i - 1]);
            }
        }

        ContentView2.add(View.inflate(activity, R.layout.text_container2, null));
        Title = (ImageView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView2.get(ContentView2.size() - 1).findViewById(R.id.ContentTextView);
        Title.setImageDrawable(ContextCompat.getDrawable(activity, activity.getResources().getIdentifier("ic_caution", "drawable", activity.getPackageName())));
        Content.setText("如要前往，請務必先以電話確認。");
        Content.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        Content.setTypeface(null, Typeface.BOLD);

        //新增view至layout
        for (int i = 0; i < ContentView2.size(); i++) {
            ShelterOpenTimeLinearLayout.addView(ContentView2.get(i));
        }

        Container.addView(shelterDetailView);
    }
}
