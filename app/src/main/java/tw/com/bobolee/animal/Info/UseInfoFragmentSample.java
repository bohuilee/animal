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

package tw.com.bobolee.animal.Info;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import tw.com.bobolee.animal.R;

public class UseInfoFragmentSample extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.use_info, container, false);
        setUserVisibleHint(false);
        LinearLayout UseInfoLinearLayout = (LinearLayout) view.findViewById(R.id.UseInfoRelativeLayout);
        ImageView Title;
        TextView Content;
        List<View> ContentView = new ArrayList<>();

        /*
        MySQLite SQLite = MySQLite.getInstance(getContext());
        SQLiteDatabase db = SQLite.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + MySQLite.VersionJsonDataColumn
                + " FROM " + MySQLite.VersionTable
                + " WHERE " + MySQLite.VersionPageColumn + " = 0 ", null);
        String UpdateDate = "";
        while (cursor.moveToNext()) {
            UpdateDate = cursor.getString(0);
        }
        cursor.close();

        ContentView.add(View.inflate(getContext(), R.layout.text_container_useinfo, null));
        Title = (ImageView) ContentView.get(ContentView.size()-1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size()-1).findViewById(R.id.ContentTextView);
        Picasso.with(getContext()).load(R.drawable.ic_caution).fit().centerInside().into(Title);
        Content.setText("資料來源為政府資料開放平台 \n http://data.gov.tw 。");
        if(!UpdateDate.equals("")){
            Content.setText(Content.getText()+ " \n 資料更新日："+UpdateDate);
        }
        */

        ContentView.add(View.inflate(getContext(), R.layout.text_container_useinfo, null));
        Title = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Glide.with(getContext()).load(R.drawable.ic_caution)
                .apply(new RequestOptions()
                        .centerInside())
                .into(Title);
        Content.setText("請提供區域編號給所屬收容所的工作人員，獲得動物實際詳細資訊。");

        ContentView.add(View.inflate(getContext(), R.layout.text_container_useinfo, null));
        Title = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Glide.with(getContext()).load(R.drawable.ic_caution)
                .apply(new RequestOptions()
                        .centerInside())
                .into(Title);
        Content.setText("動物清單更新同時也會更新[我的最愛]清單，超過期限的將被刪除。");

        ContentView.add(View.inflate(getContext(), R.layout.text_container_useinfo, null));
        Title = (ImageView) ContentView.get(ContentView.size() - 1).findViewById(R.id.TitleImageView);
        Content = (TextView) ContentView.get(ContentView.size() - 1).findViewById(R.id.ContentTextView);
        Glide.with(getContext()).load(R.drawable.ic_caution)
                .apply(new RequestOptions()
                        .centerInside())
                .into(Title);
        Content.setText("決定領養前，請先自我評估。");

        for (int i = 0; i < ContentView.size(); i++) {
            UseInfoLinearLayout.addView(ContentView.get(i));
        }

        return view;
    }
}
