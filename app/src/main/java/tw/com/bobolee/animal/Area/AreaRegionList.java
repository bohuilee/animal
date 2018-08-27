package tw.com.bobolee.animal.Area;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.RegionApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.AreaRegion;

public class AreaRegionList extends AreaRegionListFragmentSample {

    @Override
    public void getAreaRegionList() {
        RegionApiAdapter regionApiAdapter = new RegionApiAdapter();
        Observable<ApiResponse<ArrayList<AreaRegion>>> observable = regionApiAdapter.getRegionList();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<ArrayList<AreaRegion>>>(getContext()) {
                    @Override
                    public void Next(ApiResponse<ArrayList<AreaRegion>> response) {
                        RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), response.getData());
                    }
                });
    }
}
