package tw.com.bobolee.animal.Animal;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tw.com.bobolee.animal.Http.ApiAdapter.AnimalApiAdapter;
import tw.com.bobolee.animal.Http.HandleErrorObserver;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.Model.ApiResponse;

public class NewAnimalList extends AnimalListFragmentSample {
    private static final int MaxPage = 20;
    public AnimalApiAdapter animalApiAdapter;

    @Override
    public void getAnimalList(int Page) {
        if(Page >= MaxPage){
            return;
        }

        animalApiAdapter = new AnimalApiAdapter();
        Observable<ApiResponse<ArrayList<Animal>>> observable = animalApiAdapter.getAnimalList("New", Integer.toString(Page+1));
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new HandleErrorObserver<ApiResponse<ArrayList<Animal>>>(getContext()) {
                    @Override
                    public void Next(ApiResponse<ArrayList<Animal>> response) {
                        RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), response.getData());
                    }
                });
    }
}




