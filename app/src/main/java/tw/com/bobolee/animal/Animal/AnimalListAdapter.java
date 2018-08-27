package tw.com.bobolee.animal.Animal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import tw.com.bobolee.animal.MainActivity;
import tw.com.bobolee.animal.Model.Animal;
import tw.com.bobolee.animal.R;
import tw.com.bobolee.animal.Utils.AnimalShare;
import tw.com.bobolee.animal.Utils.CatchScreen;

/**
 * Created by user on 2018/8/5.
 */

public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.ViewHolder> {
    public static final int mStaggeredGridLayoutManagerDivision = 2;
    private final TypedValue mTypedValue = new TypedValue();
    private List<Animal> mAnimals;
    private Context mContext;

    public AnimalListAdapter(Context context, List<Animal> Animals) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mAnimals = Animals;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mPic;
        final TextView AnimalAreaNameTextView;
        final TextView AnimalSexNameTextView;
        final ImageButton AnimalFavoriteButton;
        final ImageButton AnimalShareButton;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPic = (ImageView) view.findViewById(R.id.AnimalAlbumFileImgView);

            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels - 50;
            mPic.getLayoutParams().height = width / mStaggeredGridLayoutManagerDivision;

            AnimalAreaNameTextView = (TextView) view.findViewById(R.id.AnimalAreaNameTextView);
            AnimalSexNameTextView = (TextView) view.findViewById(R.id.AnimalSexNameTextView);
            AnimalFavoriteButton = (ImageButton) view.findViewById(R.id.AnimalFavoriteButton);
            AnimalShareButton = (ImageButton) view.findViewById(R.id.AnimalShareButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal, parent, false);
        return new ViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.AnimalAreaNameTextView.setText(mAnimals.get(position).getAnimalAreaName());
        holder.AnimalSexNameTextView.setText(mAnimals.get(position).getAnimalSexName());

        Glide.with(mContext).load(mAnimals.get(position).isFavorite() ? R.drawable.ic_heart_full : R.drawable.ic_heart_empty)
                .apply(new RequestOptions()
                        .centerInside())
                .into(holder.AnimalFavoriteButton);

        final String AnimalId = mAnimals.get(position).getAnimalId();

        holder.AnimalFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnimals.get(position).isFavorite()) {
                    mAnimals.get(position).removeFavorite();
                    Glide.with(mContext).load((R.drawable.ic_heart_empty))
                            .apply(new RequestOptions()
                                    .centerInside())
                            .into(holder.AnimalFavoriteButton);
                    if (MainActivity.mFavoriteAnimalList.getUserVisibleHint() && MainActivity.mFavoriteAnimalList.isAdded()) {
                        if (MainActivity.mNewAnimalList.isAdded()) {
                            MainActivity.mNewAnimalList.notifyFavoriteImgChange(AnimalId);
                        }
                        if (MainActivity.mExpiringAnimalList.isAdded()) {
                            MainActivity.mExpiringAnimalList.notifyFavoriteImgChange(AnimalId);
                        }
                    }
                    if (MainActivity.mFavoriteAnimalList.isAdded()) {
                        MainActivity.mFavoriteAnimalList.removeFavoriteList(AnimalId);
                    }

                } else {
                    mAnimals.get(position).addFavorite();
                    Glide.with(mContext).load((R.drawable.ic_heart_full))
                            .apply(new RequestOptions()
                                    .centerInside())
                            .into(holder.AnimalFavoriteButton);
                    if (MainActivity.mFavoriteAnimalList.getUserVisibleHint() && MainActivity.mFavoriteAnimalList.isAdded()) {
                        if (MainActivity.mNewAnimalList.isAdded()) {
                            MainActivity.mNewAnimalList.notifyFavoriteImgChange(AnimalId);
                        }
                        if (MainActivity.mExpiringAnimalList.isAdded()) {
                            MainActivity.mExpiringAnimalList.notifyFavoriteImgChange(AnimalId);
                        }
                    }
                    if (MainActivity.mFavoriteAnimalList.isAdded()) {
                        MainActivity.mFavoriteAnimalList.addFavoriteList(mAnimals.get(position).getAnimalId(),
                                mAnimals.get(position).getAnimalAreaName(),
                                mAnimals.get(position).getAnimalSexName(),
                                mAnimals.get(position).getAnimalAlbumFile());
                    }
                }
            }
        });

        Glide.with(mContext).load(R.drawable.ic_share)
                .apply(new RequestOptions()
                        .centerInside())
                .into(holder.AnimalShareButton);
        holder.AnimalShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimalShare AnimalShare = new AnimalShare(mContext, AnimalId);

            }
        });

        String url = (mAnimals.get(position).getAnimalAlbumFile());
        Glide.with(mContext).load(url)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.animal_prints)
                        .placeholder(R.drawable.animal_prints)
                        .centerCrop())
                .into(holder.mPic);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, AnimalDetailActivity.class);
                intent.putExtra("AnimalId", AnimalId);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAnimals.size();
    }

    public void insertItem(int Position, ArrayList<Animal> Animals) {
        for (int i = 0; i < Animals.size(); i++) {
            mAnimals.add(Position + i, Animals.get(i));
            this.notifyItemInserted(Position + i);
        }
    }

    public void insertItem(Animal Animal) {
        mAnimals.add(Animal);
        this.notifyItemInserted(mAnimals.size() - 1);
    }

    public void removeItem(String AnimalId) {
        for (int i = mAnimals.size() - 1; i >= 0; i--) {
            if (mAnimals.get(i).getAnimalId().equals(AnimalId)) {
                mAnimals.remove(i);
                notifyItemRemoved(i);
                if (mAnimals.size() > 0) {
                    notifyItemRangeChanged(i, mAnimals.size() - i);
                }
            }
        }
    }

    public void notifyChangeByItem(String AnimalId) {
        for (int i = 0; i < mAnimals.size(); i++) {
            if (mAnimals.get(i).getAnimalId().equals(AnimalId)) {
                notifyItemChanged(i);
            }
        }
    }
}