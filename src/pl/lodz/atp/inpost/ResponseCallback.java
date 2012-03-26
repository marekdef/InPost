package pl.lodz.atp.inpost;

public interface ResponseCallback extends ProgressCallback
{

    void onSuccess( String result );

    void onError( Exception e );
}
