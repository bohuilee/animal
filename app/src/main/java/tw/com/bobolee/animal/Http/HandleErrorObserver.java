package tw.com.bobolee.animal.Http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import tw.com.bobolee.animal.Model.ApiError;
import tw.com.bobolee.animal.Model.ApiResponse;

public abstract class HandleErrorObserver<T extends ApiResponse> implements Observer<T> {
    private Context context;

    public HandleErrorObserver(Context context) {
        this.context = context;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public void onNext(final T response) {
        if (response.getError().getCode() == 0 && response.getData() != null) {
            Next(response);
        } else {
            Toast.makeText(context, response.getError().getMsg(), Toast.LENGTH_SHORT).show();
            HandleError(response.getError());
            Log.d("HandleErrorObserver", "onNext getError : " + response.getError().getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d("HandleErrorObserver", "onError : " + e.getMessage());
        if (e instanceof IOException) {
            Log.d("HandleErrorObserver", "onError : " + e.getMessage());
            Toast.makeText(context, "請檢查網路設定", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ApiErrorException) {
            ApiErrorException errorException = (ApiErrorException) e;
            Toast.makeText(context, errorException.getError().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "未知錯誤", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete() {
    }

    public abstract void Next(T response);

    public void HandleError(ApiError e) {
    }
}
