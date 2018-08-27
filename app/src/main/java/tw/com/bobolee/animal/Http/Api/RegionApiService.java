package tw.com.bobolee.animal.Http.Api;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.AreaRegion;

public interface RegionApiService {
    String Controller = "Region/";

    @GET(Controller + "RegionList")
    Observable<ApiResponse<ArrayList<AreaRegion>>> getRegionList();
}
