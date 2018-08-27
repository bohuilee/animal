package tw.com.bobolee.animal.Http.Api;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.Shelter;

public interface ShelterApiService {
    String Controller = "Shelter/";

    @GET(Controller + "ShelterList")
    Observable<ApiResponse<ArrayList<Shelter>>> getShelterList(@Query("AreaId") String area);

    @GET(Controller + "ShelterData")
    Observable<ApiResponse<Shelter>> getShelterData(@Query("ShelterId") String shelterId);
}
