package tw.com.bobolee.animal.Http.ApiAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import tw.com.bobolee.animal.Http.Api.RegionApiService;
import tw.com.bobolee.animal.Http.RetrofitClient;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.AreaRegion;

public class RegionApiAdapter {
    private static RegionApiService regionApiService;

    public RegionApiAdapter() {
        Retrofit retrofit = RetrofitClient.getClient();
        if(regionApiService == null) {
            regionApiService = retrofit.create(RegionApiService.class);
        }
    }

    public Observable<ApiResponse<ArrayList<AreaRegion>>> getRegionList() {
        return regionApiService.getRegionList();
    }
}
