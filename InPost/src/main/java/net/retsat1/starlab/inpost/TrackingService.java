package net.retsat1.starlab.inpost;

import java.io.Serializable;

import rx.Observable;
import rx.Observer;

/**
 * Created by marekdef on 20.04.15.
 */
public interface TrackingService extends Observer<String>{
    public Observable<Result> getObservable();

    public static class Result implements Serializable{
        public final String token;
        public final String result;

        public Result(String token, String result) {
            this.token = token;
            this.result = result;
        }
    }

    public static class Error implements  Serializable{
        public final String token;
        public final Exception exception;

        public Error(String token, Exception exception) {
            this.token = token;
            this.exception = exception;
        }
    }

}
