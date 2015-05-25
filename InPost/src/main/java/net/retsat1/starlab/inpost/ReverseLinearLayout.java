package net.retsat1.starlab.inpost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by marekdef on 25.05.15.
 */
public class ReverseLinearLayout extends LinearLayout {
    public ReverseLinearLayout(Context context) {
        super(context);
    }

    public ReverseLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public View getChildAt(int index) {
        return super.getChildAt(super.getChildCount() - index - 1);
    }
}
