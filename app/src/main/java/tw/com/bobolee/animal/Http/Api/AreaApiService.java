package tw.com.bobolee.animal.Http.Api;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.Area;
import tw.com.bobolee.animal.Model.Shelter;

public interface AreaApiService {
    String Controller = "Area/";

    @GET(Controller + "AreaList")
    Observable<ApiResponse<ArrayList<Area>>> getAreaList(@Query("AreaRegionId") String areaRegionId);
}
