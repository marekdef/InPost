package net.retsat1.starlab.inpost.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.retsat1.starlab.inpost.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {
    private static final String TRACKING_NUMBER = "trackingNumber";

    private static final String TRACKING_RESULT = "trackingResult";

    private String trackingNumber;

    private String trackingResult;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trackingNumber
     *         Tracking Number
     * @param trackingResult
     *         Tracking Result
     *
     * @return A new instance of fragment ResultFragment.
     */
    public static ResultFragment newInstance(String trackingNumber, String trackingResult) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(TRACKING_NUMBER, trackingNumber);
        args.putString(TRACKING_RESULT, trackingResult);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trackingNumber = getArguments().getString(TRACKING_NUMBER);
            trackingResult = getArguments().getString(TRACKING_RESULT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_result, container, false);
        final TextView tracking_number = (TextView) view.findViewById(R.id.tracking_number);
        final TextView tracking_result = (TextView) view.findViewById(R.id.tracking_result);

        tracking_number.setText(trackingNumber);
        tracking_result.setText(trackingResult);
        return view;
    }
}
