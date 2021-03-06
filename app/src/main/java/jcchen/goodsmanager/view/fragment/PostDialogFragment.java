package jcchen.goodsmanager.view.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DialogFragment;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import jcchen.goodsmanager.R;
import jcchen.goodsmanager.entity.PurchaseInfo;
import jcchen.goodsmanager.view.container.Container;

public class PostDialogFragment extends DialogFragment {

    public static final String TAG = "PostDialogFragment";

    private TextView postText;

    private PurchaseInfo mPurchaseInfo;

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_dialog_layout, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.post_base_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.post_bottom_sheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if (mPurchaseInfo != null)
            ((Container) view.findViewById(R.id.post_bottom_sheet)).showItem(mPurchaseInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setSelectedItem(PurchaseInfo purchaseInfo) {
        mPurchaseInfo = purchaseInfo;
    }
}
