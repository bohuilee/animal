package tw.com.bobolee.animal.Search;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Http.sendPostDataToInternet;

public class SearchAnimalList extends SearchAnimalListFragmentSample {

    public static SearchAnimalList newInstance(String WhereCondition) {
        SearchAnimalList myFragment = new SearchAnimalList();

        Bundle args = new Bundle();
        args.putString("WhereCondition", WhereCondition);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public ArrayList<Animal> getSearchAnimalList(String mWhereCondition, int Page) {
        ArrayList<Animal> list = new ArrayList<>();

        String ResultString;
        String uriAPI = getResources().getString(R.string.api_url) + "Search/SearchResultList";
        String[] Params = new String[]{"Page:" + (Page + 1), "WhereCondition:" + mWhereCondition};
        sendPostDataToInternet sPDT1 = new sendPostDataToInternet(uriAPI, Params);
        ResultString = sPDT1.run();
        try {
            JSONArray dataArray = new JSONArray(ResultString);
            for (int i = 0; i < dataArray.length(); i++) {
                Gson gson = new Gson();
                list.add(gson.fromJson(dataArray.getString(i), Animal.class));
            }
        } catch (Exception e) {
            Log.e("MYAPP", "", e);
        }

        return list;
    }
}
