package com.example.PluginFragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2015-08-14.
 */
public class PluginFragment extends Fragment {

View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        TextView t = new TextView(getActivity());
//        t.setText("我是测试插件");
//
//        t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "我是Frgment插件", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        FrameLayout frameLayout = new FrameLayout(getActivity());
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//
//        frameLayout.setBackgroundColor(Color.parseColor("#ddffff"));
//
//
//        frameLayout.addView(t);

        return inflater.inflate(R.layout.main, container, false);
    }



}
