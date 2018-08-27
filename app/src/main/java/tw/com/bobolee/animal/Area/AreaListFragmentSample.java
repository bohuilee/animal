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

package tw.com.bobolee.animal.Area;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;

import tw.com.bobolee.animal.CustomView.EmptyRecyclerView;
import tw.com.bobolee.animal.GlobalClass.FragmentUtils;
import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.Model.Area;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Shelter.ShelterList;

public abstract class AreaListFragmentSample extends Fragment {
    public SimpleStringRecyclerViewAdapter RecyclerViewAdapter;
    private static final int mStaggeredGridLayoutManagerDivision = 1;
    private static final int mStaggeredGridLayoutManagerDirection = LinearLayoutManager.VERTICAL;
    private SQLiteDatabase db;
    private static FragmentManager mFragmentManager;
    private String mAreaRegionId;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {};
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAreaRegionId = getArguments().getString("AreaRegionId");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        setUserVisibleHint(false);
        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        EmptyRecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutFrozen(true);

        MySQLite SQLite;
        SQLite = MySQLite.getInstance(getActivity().getApplicationContext());
        db = SQLite.getWritableDatabase();
        mFragmentManager = getFragmentManager();

        StaggeredGridLayoutManager StaggeredGridLM = new StaggeredGridLayoutManager(mStaggeredGridLayoutManagerDivision, mStaggeredGridLayoutManagerDirection);
        StaggeredGridLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(StaggeredGridLM);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));

        RecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(getContext(),new ArrayList<Area>());
        recyclerView.setAdapter(RecyclerViewAdapter);
        getAreaList(mAreaRegionId);
    }

    public abstract void getAreaList(String AreaRegionId);

    static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private List<Area> mAreas;
        private Context mContext;

        public SimpleStringRecyclerViewAdapter(Context context, List<Area> Areas) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mAreas = Areas;
            mContext = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder{
            public final View mView;
            public final ImageView mPic;
            public final TextView AreaNameTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPic = (ImageView) view.findViewById(R.id.AreaImageView);
                AreaNameTextView = (TextView) view.findViewById(R.id.AreaNameTextView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.area, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.AreaNameTextView.setText(mAreas.get(position).getAreaName());

            final String AreaId = mAreas.get(position).getAreaId();

            Glide.with(mContext).load(mAreas.get(position).getAreaPic())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.mPic);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction=mFragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ShelterList ShelterListFragment = ShelterList.newInstance(AreaId);
                    transaction.replace(R.id.ViewPagerRelativeLayout, ShelterListFragment,"ShelterListFragment");
                    transaction.addToBackStack("AreaListFragment");
                    transaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAreas.size();
        }

        public void insertItem(int Position, List<Area> Areas) {
            for (int i = 0; i < Areas.size(); i++) {
                mAreas.add(Position + i, Areas.get(i));
                this.notifyItemInserted(Position + i);
            }
        }
    }
}
