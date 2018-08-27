package tw.com.bobolee.animal.Shelter;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.ShelterApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Http.sendGetDataToInternet;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Model.Shelter;

public class ShelterList extends ShelterListFragmentSample {
    public static ShelterList newInstance(String AreaId) {
        ShelterList myFragment = new ShelterList();

        Bundle args = new Bundle();
        args.putString("AreaId", AreaId);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void getShelterList(String AreaId) {
        ShelterApiAdapter shelterApiAdapter = new ShelterApiAdapter();

        Observable<ApiResponse<ArrayList<Shelter>>> observable = shelterApiAdapter.getShelterList(AreaId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<ArrayList<Shelter>>>(getContext()) {
                    @Override
                    public void Next(ApiResponse<ArrayList<Shelter>> response) {
                        RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), response.getData());
                    }
                });
    }
}
