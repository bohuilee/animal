package tw.com.bobolee.animal.Http.ApiAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import tw.com.bobolee.animal.Http.Api.AnimalApiService;
import tw.com.bobolee.animal.Http.Api.ShelterApiService;
import tw.com.bobolee.animal.Http.RetrofitClient;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;
import tw.com.bobolee.animal.Model.Shelter;

public class ShelterApiAdapter {
    private static ShelterApiService shelterApiService;

    public ShelterApiAdapter() {
        Retrofit retrofit = RetrofitClient.getClient();
        if(shelterApiService == null) {
            shelterApiService = retrofit.create(ShelterApiService.class);
        }
    }

    public Observable<ApiResponse<ArrayList<Shelter>>> getShelterList(String area) {
        return shelterApiService.getShelterList(area);
    }

    public Observable<ApiResponse<Shelter>> getShelterData(String shelterId) {
        return shelterApiService.getShelterData(shelterId);
    }
}
