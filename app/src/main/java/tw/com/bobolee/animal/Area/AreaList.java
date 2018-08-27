package tw.com.bobolee.animal.Area;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.AnimalApiAdapter;
import tw.com.bobolee.animal.Http.ApiAdapter.AreaApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.Area;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Http.sendGetDataToInternet;

public class AreaList extends AreaListFragmentSample {
    public static AreaList newInstance(String AreaRegionId) {
        AreaList myFragment = new AreaList();

        Bundle args = new Bundle();
        args.putString("AreaRegionId", AreaRegionId);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void getAreaList(String areaRegionId) {
        AreaApiAdapter areaApiAdapter = new AreaApiAdapter();
        Observable<ApiResponse<ArrayList<Area>>> observable = areaApiAdapter.getAreaList(areaRegionId);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<ArrayList<Area>>>(getContext()) {
                    @Override
                    public void Next(ApiResponse<ArrayList<Area>> response) {
                        RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), response.getData());
                    }
                });
    }
}
