package com.example.DialogFragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;

public class EditNameDialogFragment extends DialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_edit_name, container);

        return view;
    }

//    @Override
//    public void onResume() {
//        Display display;
//        WindowManager windowManager = (WindowManager) getActivity()
//                .getSystemService(getActivity().WINDOW_SERVICE);
//        display = windowManager.getDefaultDisplay();
//
//
//        getDialog().getWindow().setLayout(display.getWidth(),
//                display.getHeight());
//        super.onResume();
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.ActionSheetDialogStyle);

    }

    @Override
    public void onStart() {
        super.onStart();


        getDialog().setCanceledOnTouchOutside(false);

        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 1), dm.heightPixels);


        final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        getDialog().getWindow().setAttributes(layoutParams);
    }
}
