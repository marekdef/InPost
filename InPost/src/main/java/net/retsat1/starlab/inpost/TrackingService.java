package net.retsat1.starlab.inpost;

/**
 * Created by marekdef on 20.04.15.
 */
public interface TrackingService {
    public void checkTracking(String token, Callback callback);


    public static interface Callback {
        void onResult(String result);

        void onError(Exception e);
    }
}
