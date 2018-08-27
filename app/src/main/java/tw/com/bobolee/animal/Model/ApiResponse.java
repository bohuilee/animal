package tw.com.bobolee.animal.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {

    @SerializedName("error")
    @Expose
    private ApiError error;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("data")
    @Expose
    private T data;

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}