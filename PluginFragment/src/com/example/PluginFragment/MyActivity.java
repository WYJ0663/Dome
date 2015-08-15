package com.example.PluginFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FrameLayout frameLayout = new FrameLayout(this);
        int id = 0x7f090040;
        frameLayout.setId(id);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setBackgroundColor(Color.parseColor("#dd0000"));
        setContentView(frameLayout,params);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        PluginFragment fragment1 = new PluginFragment();
        // android.R.id.content refers to the content
        // view of the activity
        fragmentTransaction.replace(id,fragment1);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}
