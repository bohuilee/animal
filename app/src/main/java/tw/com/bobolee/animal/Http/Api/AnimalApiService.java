package tw.com.bobolee.animal.Http.Api;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;

public interface AnimalApiService {
    String Controller = "Animal/";

    @GET(Controller + "AnimalList")
    Observable<ApiResponse<ArrayList<Animal>>> getAnimalList(@Query("Type") String type, @Query("Page") String page);

    @GET(Controller + "AnimalData")
    Observable<ApiResponse<Animal>> getAnimalDetail(@Query("AnimalId") String animalId);
}
