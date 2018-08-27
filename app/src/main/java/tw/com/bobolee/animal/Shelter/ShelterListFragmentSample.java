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

package tw.com.bobolee.animal.Shelter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.GlobalClass.FragmentUtils;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Model.Shelter;

public abstract class ShelterListFragmentSample extends Fragment {
    public SimpleStringRecyclerViewAdapter RecyclerViewAdapter;
    private static final int mStaggeredGridLayoutManagerDivision = 1;
    private static final int mStaggeredGridLayoutManagerDirection = LinearLayoutManager.VERTICAL;
    private SQLiteDatabase db;
    private String mAreaId;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAreaId = getArguments().getString("AreaId");
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

        StaggeredGridLayoutManager StaggeredGridLM =
                new StaggeredGridLayoutManager(mStaggeredGridLayoutManagerDivision, mStaggeredGridLayoutManagerDirection);
        StaggeredGridLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(StaggeredGridLM);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));
        RecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(getContext(), new ArrayList<Shelter>());
        recyclerView.setAdapter(RecyclerViewAdapter);
        getShelterList(mAreaId);
    }

    public abstract void getShelterList(String mAreaId);

    static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private final TypedValue mTypedValue = new TypedValue();
        private List<Shelter> mShelters;
        private Context mContext;

        public SimpleStringRecyclerViewAdapter(Context context, List<Shelter> Shelters) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mShelters = Shelters;
            mContext = context;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mPic;
            public final TextView ShelterNameTextView;
            public final TextView ShelterAddressTextView;
            public final TextView ShelterPhoneTextView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mPic = (ImageView) view.findViewById(R.id.ShelterImageView);
                ShelterNameTextView = (TextView) view.findViewById(R.id.ShelterNameTextView);
                ShelterAddressTextView = (TextView) view.findViewById(R.id.ShelterAddressTextView);
                ShelterPhoneTextView = (TextView) view.findViewById(R.id.ShelterPhoneTextView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shelter, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.ShelterNameTextView.setText(mShelters.get(position).getShelterName());
            holder.ShelterAddressTextView.setText(mShelters.get(position).getShelterAddress());
            holder.ShelterPhoneTextView.setText(mShelters.get(position).getShelterPhoneNumber());

            final String ShelterId = mShelters.get(position).getShelterId();

            Glide.with(mContext).load(mShelters.get(position).getShelterPic())
                    .apply(new RequestOptions()
                            .centerCrop())
                    .into(holder.mPic);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ShelterDetailActivity.class);
                    intent.putExtra("ShelterId", ShelterId);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mShelters.size();
        }

        public void insertItem(int Position, ArrayList<Shelter> Shelters) {
            for (int i = 0; i < Shelters.size(); i++) {
                mShelters.add(Position + i, Shelters.get(i));
                this.notifyItemInserted(Position + i);
            }
        }
    }
}
