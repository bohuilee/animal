package tw.com.bobolee.animal.Search;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tw.com.bobolee.animal.CustomView.EmptyRecyclerView;
import tw.com.bobolee.animal.Database.MySQLite;
import tw.com.bobolee.animal.GlobalClass.FragmentUtils;
import tw.com.bobolee.animal.GlobalClass.GlobalVariable;
import tw.com.bobolee.animal.Http.RequestPostDataAsyncTask;
import tw.com.bobolee.animal.CustomView.MultiSelectionSpinner;
import tw.com.bobolee.animal.R;

public class SearchConditionFragmentSample extends Fragment {
    private FragmentManager mFragmentManager;
    private FragmentActivity myContext;
    static SimpleStringRecyclerViewAdapter RecyclerViewAdapter;
    RelativeLayout relativeLayout;
    private long mAcceptButtonLastClickTime = 0;

    String AreaWhereCondition;                  //地區
    String AnimalKindWhereCondition;            //種類
    String AnimalColourWhereCondition;          //毛色
    String AnimalSexWhereCondition;             //性別
    String AnimalBodyTypeWhereCondition;        //體型
    String AnimalSterilizationWhereCondition;   //結紮
    String AnimalBacterinWhereCondition;        //狂犬疫苗
    String AreaWhereConditionToSave = null;                //地區(儲存條件)
    String AnimalKindWhereConditionToSave = null;          //種類(儲存條件)
    String AnimalColourWhereConditionToSave = null;        //毛色(儲存條件)
    String AnimalSexWhereConditionToSave = null;           //性別(儲存條件)
    String AnimalBodyTypeWhereConditionToSave = null;      //體型(儲存條件)
    String AnimalSterilizationWhereConditionToSave = null; //結紮(儲存條件)
    String AnimalBacterinWhereConditionToSave = null;      //狂犬疫苗(儲存條件)
    String SavedAreaWhereCondition = null;                //地區(已儲存條件)
    String SavedAnimalKindWhereCondition = null;          //種類(已儲存條件)
    String SavedAnimalColourWhereCondition = null;        //毛色(已儲存條件)
    String SavedAnimalSexWhereCondition = null;           //性別(已儲存條件)
    String SavedAnimalBodyTypeWhereCondition = null;      //體型(已儲存條件)
    String SavedAnimalSterilizationWhereCondition = null; //結紮(已儲存條件)
    String SavedAnimalBacterinWhereCondition = null;      //狂犬疫苗(已儲存條件)

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

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (relativeLayout == null) {
            relativeLayout = (RelativeLayout) inflater.inflate(R.layout.search_condition_recyclerview, container, false);
            RecyclerView rv = (RecyclerView) relativeLayout.findViewById(R.id.recyclerview);
            setUserVisibleHint(false);
            rv.setLayoutFrozen(true);
            setupRecyclerView(rv);
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.saved_condition_frame, new SavedConditionFragment());
        ft.commitAllowingStateLoss();
        return relativeLayout;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mFragmentManager = myContext.getSupportFragmentManager();

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        RecyclerViewAdapter = new SimpleStringRecyclerViewAdapter(getActivity(), getSpinnerContent());
        recyclerView.setAdapter(RecyclerViewAdapter);

        Button AcceptButton = (Button) relativeLayout.findViewById(R.id.accepta);

        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mAcceptButtonLastClickTime < 1000) {
                    return;
                }
                mAcceptButtonLastClickTime = SystemClock.elapsedRealtime();

                int ResultCount = 0;
                try {
                    String url = myContext.getResources().getString(R.string.api_url) + "Search/SearchResultCount";
                    String[] Params = new String[]{"WhereCondition:" + getWhereCondition()};
                    RequestPostDataAsyncTask mAsyncTask = new RequestPostDataAsyncTask(url, Params);
                    String ResultString = mAsyncTask.execute(url).get();
                    JSONObject json = new JSONObject(ResultString);
                    ResultCount = Integer.valueOf(json.getString("count"));
                    Log.d("ResultCount", Integer.toString(ResultCount));
                } catch (Exception e) {
                    Log.e("My App", "Error SearchResultCount RequestPostDataAsyncTask");
                }
                if (ResultCount > 0) {
                    FragmentTransaction transaction = mFragmentManager.beginTransaction();
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    String WhereCondition = getWhereCondition();
                    saveWhereCondition();
                    SearchAnimalList SearchAnimalListFragment = SearchAnimalList.newInstance(WhereCondition);
                    transaction.replace(R.id.ViewPagerRelativeLayout, SearchAnimalListFragment);
                    transaction.addToBackStack("SearchConditionFragmentSample");
                    transaction.commit();
                } else {
                    Toast.makeText(myContext, myContext.getResources().getString(R.string.NoResult), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class SpinnerContent {
        String TitleText;
        ArrayList<String> ContentArray;

        SpinnerContent(String TitleText, ArrayList<String> ContentArray) {
            this.TitleText = TitleText;
            this.ContentArray = ContentArray;
        }

        String getTitleText() {
            return TitleText;
        }

        ArrayList<String> getContentArray() {
            return ContentArray;
        }
    }

    public ArrayList<SpinnerContent> getSpinnerContent() {
        ArrayList<SpinnerContent> SpinnerContent = new ArrayList<>();

        SpinnerContent.add(new SpinnerContent("地區", GlobalVariable.getAreaList()));
        SpinnerContent.add(new SpinnerContent("種類", GlobalVariable.getAnimalKindList()));
        SpinnerContent.add(new SpinnerContent("毛色", GlobalVariable.getAnimalColourList()));
        SpinnerContent.add(new SpinnerContent("性別", GlobalVariable.getAnimalSexList()));
        SpinnerContent.add(new SpinnerContent("體型", GlobalVariable.getAnimalBodyTypeList()));
        SpinnerContent.add(new SpinnerContent("結紮", GlobalVariable.getAnimalSterilizationList()));
        SpinnerContent.add(new SpinnerContent("狂犬疫苗", GlobalVariable.getAnimalBacterinList()));

        return SpinnerContent;
    }

    class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {
        private final TypedValue mTypedValue = new TypedValue();
        private Context mContext;
        private ArrayList<SpinnerContent> mSpinnerContent;

        SimpleStringRecyclerViewAdapter(Context context, ArrayList<SpinnerContent> SpinnerContent) {
            mSpinnerContent = SpinnerContent;
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mContext = context;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mTitle;
            final MultiSelectionSpinner mContent;
            final ImageButton mDeleteImageButton;
            final Button mIM;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mTitle = (TextView) view.findViewById(R.id.TitleTextView);
                mContent = (MultiSelectionSpinner) view.findViewById(R.id.ContentSpinner);
                mDeleteImageButton = (ImageButton) view.findViewById(R.id.DeleteImageButton);
                mIM = (Button) mView.findViewById(R.id.accept);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_condition_recyclerview_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //Spinner(多選選單)
            holder.mTitle.setText(mSpinnerContent.get(position).getTitleText());
            holder.mContent.setItems(mSpinnerContent.get(position).getContentArray());
            holder.mContent.setListener(this, position);
            holder.mContent.setSelection(new int[]{});
            Glide.with(mContext).load(R.drawable.ic_delete)
                    .apply(new RequestOptions()
                            .override(30, 30))
                    .into(holder.mDeleteImageButton);
            holder.mDeleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mContent.setSelection(new int[]{});
                    switch (holder.getAdapterPosition()) {
                        case 0:
                            AreaWhereConditionToSave = "1=1";
                            break;
                        case 1:
                            AnimalKindWhereConditionToSave = "1=1";
                            break;
                        case 2:
                            AnimalColourWhereConditionToSave = "1=1";
                            break;
                        case 3:
                            AnimalSexWhereConditionToSave = "1=1";
                            break;
                        case 4:
                            AnimalBodyTypeWhereConditionToSave = "1=1";
                            break;
                        case 5:
                            AnimalSterilizationWhereConditionToSave = "1=1";
                            break;
                        case 6:
                            AnimalBacterinWhereConditionToSave = "1=1";
                            break;
                    }
                }
            });
            String SavedWhereCondition = null;
            switch (holder.getAdapterPosition()) {
                case 0:
                    SavedWhereCondition = SavedAreaWhereCondition;
                    break;
                case 1:
                    SavedWhereCondition = SavedAnimalKindWhereCondition;
                    break;
                case 2:
                    SavedWhereCondition = SavedAnimalColourWhereCondition;
                    break;
                case 3:
                    SavedWhereCondition = SavedAnimalSexWhereCondition;
                    break;
                case 4:
                    SavedWhereCondition = SavedAnimalBodyTypeWhereCondition;
                    break;
                case 5:
                    SavedWhereCondition = SavedAnimalSterilizationWhereCondition;
                    break;
                case 6:
                    SavedWhereCondition = SavedAnimalBacterinWhereCondition;
                    break;
            }
            if (SavedWhereCondition != null) {

                String[] SavedWhereConditionArray = android.text.TextUtils.split(SavedWhereCondition, ",");
                List<String> SavedWhereConditionList = Arrays.asList(SavedWhereConditionArray);
                holder.mContent.setSelection(SavedWhereConditionList);
            }
        }

        @Override
        public int getItemCount() {
            return mSpinnerContent.size();
        }

        @Override
        public void selectedIndices(List<Integer> indices, int Position) {
        }

        @Override
        public void selectedStrings(List<String> strings, int Position) {
            String WhereConditionToSave = android.text.TextUtils.join(",", strings);
            if (WhereConditionToSave.equals("")) {
                WhereConditionToSave = "1=1";
            }
            if (Position == 2) {
                //毛色特殊處理
                if (strings.size() == 0) {
                    AnimalColourWhereCondition = "1=1";
                } else {
                    String WhereCondition = "";
                    String elseWhereCondition = "";
                    for (int i = 0; i < strings.size(); i++) {
                        if (strings.get(i).equals("其他")) {
                            for (int j = 0; j < GlobalVariable.getAnimalColourList().size(); j++) {
                                if (GlobalVariable.getAnimalColourList().get(j).equals("其他")) {
                                    continue;
                                }
                                if (elseWhereCondition.equals("")) {
                                    elseWhereCondition = MySQLite.AnimalColourColumn + " NOT LIKE '%" + GlobalVariable.getAnimalColourList().get(j) + "%'";
                                } else {
                                    elseWhereCondition = elseWhereCondition + " AND " + MySQLite.AnimalColourColumn + " NOT LIKE '%" + GlobalVariable.getAnimalColourList().get(j) + "%'";
                                }
                            }
                            if (WhereCondition.equals("")) {
                                WhereCondition = "(" + elseWhereCondition + ")";
                            } else {
                                WhereCondition = WhereCondition + " OR " + "(" + elseWhereCondition + ")";
                            }
                        } else {
                            if (WhereCondition.equals("")) {
                                WhereCondition = MySQLite.AnimalColourColumn + " LIKE '%" + strings.get(i) + "%'";
                            } else {
                                WhereCondition = WhereCondition + " OR " + MySQLite.AnimalColourColumn + " LIKE '%" + strings.get(i) + "%'";
                            }
                        }
                    }
                    AnimalColourWhereCondition = "(" + WhereCondition + ")";
                }
                AnimalColourWhereConditionToSave = WhereConditionToSave;
            } else {
                String WhereCondition = "";
                if (strings.size() > 0) {
                    for (int i = 0; i < strings.size(); i++) {
                        if (WhereCondition.equals("")) {
                            WhereCondition = "'" + strings.get(i) + "'";
                        } else {
                            WhereCondition = WhereCondition + "," + "'" + strings.get(i) + "'";
                        }
                    }
                    WhereCondition = " IN (" + WhereCondition + ")";
                }
                switch (Position) {
                    case 0://所在地
                        if (WhereCondition.equals("")) {
                            AreaWhereCondition = "1=1";
                        } else {
                            AreaWhereCondition = MySQLite.AnimalAreaNameColumn + WhereCondition;
                        }
                        AreaWhereConditionToSave = WhereConditionToSave;
                        break;
                    case 1://種類
                        if (WhereCondition.equals("")) {
                            AnimalKindWhereCondition = "1=1";
                        } else {
                            AnimalKindWhereCondition = MySQLite.AnimalKindColumn + WhereCondition;
                        }
                        AnimalKindWhereConditionToSave = WhereConditionToSave;
                        break;
                    case 3://性別
                        if (WhereCondition.equals("")) {
                            AnimalSexWhereCondition = "1=1";
                        } else {
                            AnimalSexWhereCondition = MySQLite.AnimalSexNameColumn + WhereCondition;
                        }
                        AnimalSexWhereConditionToSave = WhereConditionToSave;
                        break;
                    case 4://體型
                        if (WhereCondition.equals("")) {
                            AnimalBodyTypeWhereCondition = "1=1";
                        } else {
                            AnimalBodyTypeWhereCondition = MySQLite.AnimalBodyTypeNameColumn + WhereCondition;
                        }
                        AnimalBodyTypeWhereConditionToSave = WhereConditionToSave;
                        break;
                    case 5://結紮
                        if (WhereCondition.equals("")) {
                            AnimalSterilizationWhereCondition = "1=1";
                        } else {
                            AnimalSterilizationWhereCondition = MySQLite.AnimalSterilizationNameColumn + WhereCondition;
                        }
                        AnimalSterilizationWhereConditionToSave = WhereConditionToSave;
                        break;
                    case 6://狂犬疫苗
                        if (WhereCondition.equals("")) {
                            AnimalBacterinWhereCondition = "1=1";
                        } else {
                            AnimalBacterinWhereCondition = MySQLite.AnimalBacterinNameColumn + WhereCondition;
                        }
                        AnimalBacterinWhereConditionToSave = WhereConditionToSave;
                        break;
                }
            }
        }

        void updateWhereConditionState(Integer Id) {
            SQLiteDatabase db = GlobalVariable.getDB();
            Cursor cursor;
            cursor = db.rawQuery("SELECT "
                    + MySQLite.AreaWhereConditionColumn + ","
                    + MySQLite.AnimalKindWhereConditionColumn + ","
                    + MySQLite.AnimalColourWhereConditionColumn + ","
                    + MySQLite.AnimalSexWhereConditionColumn + ","
                    + MySQLite.AnimalBodyTypeWhereConditionColumn + ","
                    + MySQLite.AnimalSterilizationWhereConditionColumn + ","
                    + MySQLite.AnimalBacterinWhereConditionColumn + ","
                    + MySQLite.WhereConditionIdColumn
                    + " FROM " + MySQLite.WhereConditionTable
                    + " WHERE " + MySQLite.WhereConditionIdColumn + " = " + Id, null);

            while (cursor.moveToNext()) {
                SavedAreaWhereCondition = cursor.getString(0);
                SavedAnimalKindWhereCondition = cursor.getString(1);
                SavedAnimalColourWhereCondition = cursor.getString(2);
                SavedAnimalSexWhereCondition = cursor.getString(3);
                SavedAnimalBodyTypeWhereCondition = cursor.getString(4);
                SavedAnimalSterilizationWhereCondition = cursor.getString(5);
                SavedAnimalBacterinWhereCondition = cursor.getString(6);
                notifyDataSetChanged();
            }
            cursor.close();
        }
    }

    private String getWhereCondition() {
        String WhereCondition;

        WhereCondition = AreaWhereCondition                     //地區
                + " AND " + AnimalKindWhereCondition            //種類
                + " AND " + AnimalColourWhereCondition          //毛色
                + " AND " + AnimalSexWhereCondition             //性別
                + " AND " + AnimalBodyTypeWhereCondition        //體型
                + " AND " + AnimalSterilizationWhereCondition   //結紮
                + " AND " + AnimalBacterinWhereCondition;       //狂犬疫苗

        return WhereCondition;
    }

    private void saveWhereCondition() {
        SQLiteDatabase db = GlobalVariable.getDB();
        ContentValues values = new ContentValues();

        if (!AreaWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AreaWhereConditionColumn, AreaWhereConditionToSave);
        }
        if (!AnimalKindWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalKindWhereConditionColumn, AnimalKindWhereConditionToSave);
        }
        if (!AnimalColourWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalColourWhereConditionColumn, AnimalColourWhereConditionToSave);
        }
        if (!AnimalSexWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalSexWhereConditionColumn, AnimalSexWhereConditionToSave);
        }
        if (!AnimalBodyTypeWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalBodyTypeWhereConditionColumn, AnimalBodyTypeWhereConditionToSave);
        }
        if (!AnimalSterilizationWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalSterilizationWhereConditionColumn, AnimalSterilizationWhereConditionToSave);
        }
        if (!AnimalBacterinWhereConditionToSave.equals("1=1")) {
            values.put(MySQLite.AnimalBacterinWhereConditionColumn, AnimalBacterinWhereConditionToSave);
        }

        if (values.size() > 0) {
            Cursor cursor;
            cursor = db.rawQuery("SELECT " + MySQLite.WhereConditionIdColumn + " FROM " + MySQLite.WhereConditionTable + " ORDER BY " + MySQLite.WhereConditionIdColumn, null);
            if (cursor.getCount() >= 5) {
                cursor.moveToNext();
                db.delete(MySQLite.WhereConditionTable, MySQLite.WhereConditionIdColumn + "=" + cursor.getInt(0), null);
            }
            db.insert(MySQLite.WhereConditionTable, null, values);

            cursor.close();
        }
    }

    public static class SavedConditionFragment extends Fragment {
        public SavedConditionViewAdapter SavedConditionViewAdapter;
        private static final int mStaggeredGridLayoutManagerDivision = 1;
        private static final int mStaggeredGridLayoutManagerDirection = LinearLayoutManager.VERTICAL;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_item_list, container, false);
            setUserVisibleHint(false);
            setupRecyclerView(view);
            return view;
        }

        private void setupRecyclerView(View view) {
            EmptyRecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutFrozen(true);
            StaggeredGridLayoutManager StaggeredGridLM = new StaggeredGridLayoutManager(mStaggeredGridLayoutManagerDivision, mStaggeredGridLayoutManagerDirection);
            StaggeredGridLM.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
            recyclerView.setLayoutManager(StaggeredGridLM);
            recyclerView.setEmptyView(view.findViewById(R.id.empty_view));
            SavedConditionViewAdapter = new SavedConditionViewAdapter(getSavedWhereConditions());
            recyclerView.setAdapter(SavedConditionViewAdapter);
        }


        class SavedConditionViewAdapter extends RecyclerView.Adapter<SavedConditionViewAdapter.ViewHolder> {
            private List<WhereConditionObj> mWhereConditions;

            SavedConditionViewAdapter(List<WhereConditionObj> WhereConditions) {
                mWhereConditions = WhereConditions;
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                final View mView;
                final TextView ConditionTextView;
                ImageView mDeleteImageButton;

                ViewHolder(View view) {
                    super(view);
                    mView = view;
                    ConditionTextView = (TextView) view.findViewById(R.id.ConditionTextView);
                    mDeleteImageButton = (ImageButton) view.findViewById(R.id.DeleteImageButton);
                }
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view;
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_condition_recyclerview_item, parent, false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(final ViewHolder holder, int position) {
                holder.ConditionTextView.setText(mWhereConditions.get(holder.getAdapterPosition()).getWhereCondition());

                Glide.with(getActivity()).load(R.drawable.ic_delete)
                        .apply(new RequestOptions()
                                .override(30, 30))
                        .into(holder.mDeleteImageButton);
                holder.mDeleteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteSavedWhereCondition(holder.getAdapterPosition());
                    }
                });

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RecyclerViewAdapter.updateWhereConditionState(mWhereConditions.get(holder.getAdapterPosition()).getId());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mWhereConditions.size();
            }

            private void deleteSavedWhereCondition(int position) {
                SQLiteDatabase db = GlobalVariable.getDB();
                db.delete(MySQLite.WhereConditionTable, MySQLite.WhereConditionIdColumn + "='" + mWhereConditions.get(position).getId() + "'", null);
                mWhereConditions.remove(position);
                notifyItemRemoved(position);
            }
        }

        class WhereConditionObj {
            int Id;
            String WhereCondition;

            WhereConditionObj(int id, String whereCondition) {
                Id = id;
                WhereCondition = whereCondition;
            }

            public int getId() {
                return Id;
            }

            String getWhereCondition() {
                return WhereCondition;
            }
        }

        private ArrayList<WhereConditionObj> getSavedWhereConditions() {
            SQLiteDatabase db = GlobalVariable.getDB();
            Cursor cursor;
            cursor = db.rawQuery("SELECT "
                    + MySQLite.AreaWhereConditionColumn + ","
                    + MySQLite.AnimalKindWhereConditionColumn + ","
                    + MySQLite.AnimalColourWhereConditionColumn + ","
                    + MySQLite.AnimalSexWhereConditionColumn + ","
                    + MySQLite.AnimalBodyTypeWhereConditionColumn + ","
                    + MySQLite.AnimalSterilizationWhereConditionColumn + ","
                    + MySQLite.AnimalBacterinWhereConditionColumn + ","
                    + MySQLite.WhereConditionIdColumn
                    + " FROM " + MySQLite.WhereConditionTable
                    + " ORDER BY " + MySQLite.WhereConditionIdColumn + " DESC ", null);

            ArrayList<WhereConditionObj> SavedWhereConditions = new ArrayList<>();
            while (cursor.moveToNext()) {
                String SavedAreaWhereCondition = cursor.getString(0);
                String SavedAnimalKindWhereCondition = cursor.getString(1);
                String SavedAnimalColourWhereCondition = cursor.getString(2);
                String SavedAnimalSexWhereCondition = cursor.getString(3);
                String SavedAnimalBodyTypeWhereCondition = cursor.getString(4);
                String SavedAnimalSterilizationWhereCondition = cursor.getString(5);
                String SavedAnimalBacterinWhereCondition = cursor.getString(6);

                String condition = "";

                if (SavedAreaWhereCondition != null) {
                    condition = condition + "[" + SavedAreaWhereCondition + "]";
                }
                if (SavedAnimalKindWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalKindWhereCondition + "]";
                }
                if (SavedAnimalColourWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalColourWhereCondition + "]";
                }
                if (SavedAnimalSexWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalSexWhereCondition + "]";
                }
                if (SavedAnimalBodyTypeWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalBodyTypeWhereCondition + "]";
                }
                if (SavedAnimalSterilizationWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalSterilizationWhereCondition + "]";
                }
                if (SavedAnimalBacterinWhereCondition != null) {
                    condition = condition + "[" + SavedAnimalBacterinWhereCondition + "]";
                }

                SavedWhereConditions.add(new WhereConditionObj(cursor.getInt(7), condition));
            }
            cursor.close();

            return SavedWhereConditions;
        }
    }
}
