/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*新品列表*/

package tw.com.bobolee.animal.Search;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import tw.com.bobolee.animal.Animal.AnimalListAdapter;
import tw.com.bobolee.animal.CustomView.EmptyRecyclerView;
import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.Lib.EndlessRecyclerViewScrollListener;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.R;

public class SearchAnimalListFragmentSample extends Fragment {
    public AnimalListAdapter RecyclerViewAdapter;
    private static final int mStaggeredGridLayoutManagerDirection = LinearLayoutManager.VERTICAL;
    private String mWhereCondition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWhereCondition = getArguments().getString("WhereCondition");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        setUserVisibleHint(false);
        setupRecyclerView(view);

        return view;
    }

    private void setupRecyclerView(View view) {
        MySQLite SQLite;
        SQLite = MySQLite.getInstance(getActivity().getApplicationContext());
        SQLiteDatabase db = SQLite.getWritableDatabase();

        EmptyRecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutFrozen(true);
        StaggeredGridLayoutManager StaggeredGridLM =
                new StaggeredGridLayoutManager(AnimalListAdapter.mStaggeredGridLayoutManagerDivision, mStaggeredGridLayoutManagerDirection);
        StaggeredGridLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(StaggeredGridLM);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        RecyclerViewAdapter = new AnimalListAdapter(getContext(), new ArrayList<Animal>());
        GetDataAsyncTask mAsyncTask = new GetDataAsyncTask();
        mAsyncTask.execute(0);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(StaggeredGridLM) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // fetch data
                GetDataAsyncTask mAsyncTask = new GetDataAsyncTask();
                mAsyncTask.execute(page);
                Log.e("My App", "Load page:" + page);
            }
        });

        recyclerView.setAdapter(RecyclerViewAdapter);
    }

    private class GetDataAsyncTask extends AsyncTask<Integer, Void, Void> {
        private int Page;
        ArrayList<Animal> List = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            Page = params[0];
            List = getSearchAnimalList(mWhereCondition, Page);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (List.size() == 0) {
                Toast.makeText(getContext(), "沒有更多動物了!", Toast.LENGTH_LONG).show();
            } else {
                RecyclerViewAdapter.insertItem(RecyclerViewAdapter.getItemCount(), List);
            }
        }
    }

    public ArrayList<Animal> getSearchAnimalList(String mWhereCondition, int Page) {
        return new ArrayList<>();
    }
}
