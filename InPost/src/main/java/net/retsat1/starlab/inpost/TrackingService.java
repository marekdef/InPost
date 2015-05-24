package net.retsat1.starlab.inpost;

import java.io.Serializable;

import rx.Observable;

/**
 * Created by marekdef on 20.04.15.
 */
public interface TrackingService {

    public Observable<Result> getResultStream();

    public void sendQuery(String trackingNumber);

    public Observable<TrackingService.Error> getErrorStream();

    public static class Result implements Serializable {

        public final String number;

        public final String result;

        public Result(String number, String result) {
            this.number = number;
            this.result = result;
        }
    }

    public static class Error implements Serializable {
        public final String number;

        public final Exception throwable;

        public Error(String number, Exception throwable) {
            this.number = number;
            this.throwable = throwable;
        }
    }
}
