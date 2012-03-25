package pl.lodz.atp.inpost.http;

public class AsyncHttpResponse<T> {

    private Exception mException;
    private T mResponse;

    public boolean isError() {
        if (mException == null) {
            return false;
        } else {
            return true;
        }
    }

    public T getResponse() {
        return mResponse;
    }

    public Exception getError() {
        return mException;
    }

    public void setError( Exception e ) {
        mException = e;
    }

    public void setRespnse( T response ) {
        this.mResponse = response;
    }
}
