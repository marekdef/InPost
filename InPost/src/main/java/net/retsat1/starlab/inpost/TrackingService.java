package net.retsat1.starlab.inpost;

import java.io.Serializable;

import rx.Observable;

/**
 * Created by marekdef on 20.04.15.
 */
public interface TrackingService {

    public Observable<Result> getObservable();

    public void sendQuery(String trackingNumber);

    public static class Result implements Serializable {

        public final String number;

        public final String result;

        public Result(String number, String result) {
            this.number = number;
            this.result = result;
        }
    }
}
