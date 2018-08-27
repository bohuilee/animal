package tw.com.bobolee.animal.Http.ApiAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import tw.com.bobolee.animal.Http.Api.AreaApiService;
import tw.com.bobolee.animal.Http.Api.ShelterApiService;
import tw.com.bobolee.animal.Http.RetrofitClient;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.Area;
import tw.com.bobolee.animal.Model.Shelter;

public class AreaApiAdapter {
    private static AreaApiService areaApiService;

    public AreaApiAdapter() {
        Retrofit retrofit = RetrofitClient.getClient();
        if(areaApiService == null) {
            areaApiService = retrofit.create(AreaApiService.class);
        }
    }

    public Observable<ApiResponse<ArrayList<Area>>> getAreaList(String areaRegionId) {
        return areaApiService.getAreaList(areaRegionId);
    }
}
