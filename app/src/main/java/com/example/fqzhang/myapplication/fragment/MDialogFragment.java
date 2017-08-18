package com.example.fqzhang.myapplication.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.fqzhang.myapplication.R;
import com.example.fqzhang.myapplication.Util.FragmentExchangeController;

/**
 * A simple {@link Fragment} subclass.
 */
public class MDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView leftBtn,rightBtn;

    public MDialogFragment() {
    }

    public static MDialogFragment newInstance(){
        MDialogFragment fragment = new MDialogFragment();
        //fragment.setArguments
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getActivity());
        layout.setClickable(true);
        View view = inflater.inflate(R.layout.dialog_excute_layout,null);
        layout.addView(view);
        initView(view);
        return layout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    private void initView(View view) {
        leftBtn = (TextView) view.findViewById(R.id.left_btn);
        rightBtn = (TextView) view.findViewById(R.id.right_btn);
        setListener();
    }

    private void setListener() {
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_btn:
                dismiss();
                break;
            case R.id.right_btn:
                dismiss();
                break;
            default:
                break;
        }
    }
  /*  public void dismissSelf() {
        if (getActivity() instanceof CtripBaseActivity) {
            ((AppCompatActivity) getActivity()).getDialogFragmentTags().remove(getTag());
        }
        FragmentExchangeController.removeFragment(getFragmentManager(), this);
    }*/
}
