package tw.com.bobolee.animal.Http;

import tw.com.bobolee.animal.Model.ApiError;

public class ApiErrorException extends Exception {
    private ApiError error;

    public ApiErrorException(ApiError error) {
        super(error.getMsg());
        this.error = error;
    }

    public ApiErrorException(String message) {
        super(message);
    }

    public ApiErrorException(Throwable t) {
        super(t);
    }

    public ApiErrorException(String message, Throwable t) {
        super(message, t);
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
