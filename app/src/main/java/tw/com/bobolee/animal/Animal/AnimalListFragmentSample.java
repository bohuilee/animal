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

package tw.com.bobolee.animal.Animal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tw.com.bobolee.animal.CustomView.EmptyRecyclerView;
import tw.com.bobolee.animal.Lib.EndlessRecyclerViewScrollListener;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.R;

public abstract class AnimalListFragmentSample extends Fragment {
    public AnimalListAdapter RecyclerViewAdapter;
    private static final int mStaggeredGridLayoutManagerDirection = LinearLayoutManager.VERTICAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        setUserVisibleHint(false);
        setupRecyclerView(view);

        return view;
    }

    public void addFavoriteList(String AnimalId, String AnimalAreaName, String AnimalSexName, String AnimalAlbumFile) {
        RecyclerViewAdapter.insertItem(new Animal(AnimalId, AnimalAreaName, AnimalSexName, AnimalAlbumFile));
    }

    public void removeFavoriteList(String AnimalId) {
        RecyclerViewAdapter.removeItem(AnimalId);
    }

    public void notifyFavoriteImgChange(String AnimalId) {
        RecyclerViewAdapter.notifyChangeByItem(AnimalId);
    }

    private void setupRecyclerView(View view) {
        EmptyRecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutFrozen(true);
        StaggeredGridLayoutManager StaggeredGridLM =
                new StaggeredGridLayoutManager(AnimalListAdapter.mStaggeredGridLayoutManagerDivision, mStaggeredGridLayoutManagerDirection);
        StaggeredGridLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(StaggeredGridLM);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        RecyclerViewAdapter = new AnimalListAdapter(getContext(), new ArrayList<Animal>());
        getAnimalList(0);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(StaggeredGridLM) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // fetch data
                getAnimalList(page);
            }
        });

        recyclerView.setAdapter(RecyclerViewAdapter);
    }

    public abstract void getAnimalList(int Page);
}
