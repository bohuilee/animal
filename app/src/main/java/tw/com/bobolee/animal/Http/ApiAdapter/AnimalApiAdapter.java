package tw.com.bobolee.animal.Http.ApiAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import tw.com.bobolee.animal.Http.Api.AnimalApiService;
import tw.com.bobolee.animal.Http.RetrofitClient;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;

public class AnimalApiAdapter {
    private static AnimalApiService animalApiService;

    public AnimalApiAdapter() {
        Retrofit retrofit = RetrofitClient.getClient();
        if(animalApiService == null) {
            animalApiService = retrofit.create(AnimalApiService.class);
        }
    }

    public Observable<ApiResponse<ArrayList<Animal>>> getAnimalList(String type, String page) {
        return animalApiService.getAnimalList(type, page);
    }

    public Observable<ApiResponse<Animal>> getAnimalDetail(String animalId) {
        return animalApiService.getAnimalDetail(animalId);
    }
}
