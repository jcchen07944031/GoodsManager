package jcchen.goodsmanager.view.container;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import jcchen.goodsmanager.R;
import jcchen.goodsmanager.entity.ColorInfo;
import jcchen.goodsmanager.presenter.impl.SettingPresenterImpl;
import jcchen.goodsmanager.view.fragment.EditSettingDialogFragment;
import jcchen.goodsmanager.view.listener.OnSettingEditListener;
import jcchen.goodsmanager.view.widget.RecyclerHelper.OnSwipeListener;
import jcchen.goodsmanager.view.widget.RecyclerHelper.RecyclerHelper;

public class ColorSettingContainer extends FrameLayout implements Container {

    private final int FIRST_CARD = 0;
    private final int DEFAULT_CARD = 1;

    private Context context;

    private RecyclerHelper mRecyclerHelper;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mRecyclerViewAdapter;

    private ArrayList<ColorInfo> colorList;

    private SettingPresenterImpl mSettingPresenter;
    private EditSettingDialogFragment mEditSettingDialogFragment;

    public ColorSettingContainer(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ColorSettingContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void init() {
        mSettingPresenter = new SettingPresenterImpl(context);
        colorList = mSettingPresenter.getColorList();

        mRecyclerViewAdapter = new ColorSettingContainer.RecyclerViewAdapter();
        mRecyclerHelper = new RecyclerHelper<ColorInfo>(colorList, (RecyclerView.Adapter) mRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(mRecyclerHelper);
        mRecyclerView = new RecyclerView(context);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, ((LinearLayoutManager) mRecyclerView.getLayoutManager()).getOrientation()));
        mRecyclerView.setBackground(ContextCompat.getDrawable(context, R.color.colorAppBackground));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        addView(mRecyclerView);

        mRecyclerHelper.setRecyclerItemDragEnabled(true).setOnDragListener(new jcchen.goodsmanager.view.widget.RecyclerHelper.OnDragListener() {
            @Override
            public void onDragItemEnd() {
                mSettingPresenter.saveColor(colorList);
            }
        });
        mRecyclerHelper.setRecyclerItemSwipeEnabled(true).setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipeItemEnd(final int position) {
                new androidx.appcompat.app.AlertDialog.Builder(context)
                        .setMessage(context.getResources().getString(
                                R.string.delete_confirm_message) +
                                " [" + ((ColorInfo) colorList.get(position)).getName() + "]")
                        .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                colorList.remove(position);
                                mRecyclerViewAdapter.notifyItemRemoved(position);
                                mSettingPresenter.saveColor(colorList);
                            }
                        })
                        .setNegativeButton(R.string.confirm_no, null)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                mRecyclerViewAdapter.notifyItemChanged(position);
                            }
                        })
                        .show();
            }
        });
        mRecyclerHelper.addDisablePos(0);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void showItem(Object object) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void postResult() {

    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements OnSettingEditListener {

        private int selectedPos;

        RecyclerViewAdapter() {
            colorList.add(0, null); // First Card.
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case FIRST_CARD:
                    return new RecyclerViewAdapter.ViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.setting_add_cardview, parent, false));
                case DEFAULT_CARD:
                    return new RecyclerViewAdapter.ViewHolder(LayoutInflater.from(context)
                            .inflate(R.layout.color_setting_cardview, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder viewHolder, final int position) {
            switch (getItemViewType(position)) {
                case FIRST_CARD:
                    viewHolder.Add.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedPos = viewHolder.getAdapterPosition();
                            mEditSettingDialogFragment = new EditSettingDialogFragment();
                            mEditSettingDialogFragment.setResID(R.layout.color_edit_setting_layout);
                            mEditSettingDialogFragment.setListener(mRecyclerViewAdapter);
                            mEditSettingDialogFragment.show(((Activity) context).getFragmentManager(), EditSettingDialogFragment.TAG);
                        }
                    });
                    break;
                case DEFAULT_CARD:
                    viewHolder.Name.setText(colorList.get(position).getName());
                    viewHolder.Code.setText(colorList.get(position).getCode());
                    viewHolder.Edit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedPos = viewHolder.getAdapterPosition();
                            mEditSettingDialogFragment = new EditSettingDialogFragment();
                            mEditSettingDialogFragment.setResID(R.layout.color_edit_setting_layout);
                            mEditSettingDialogFragment.setListener(mRecyclerViewAdapter);
                            mEditSettingDialogFragment.show(((Activity) context).getFragmentManager(), EditSettingDialogFragment.TAG);
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return colorList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == 0) ? FIRST_CARD : DEFAULT_CARD;
        }

        @Override
        public void onEditStart(final View view) {
            switch (getItemViewType(selectedPos)) {
                case FIRST_CARD:
                    ((TextView) view.findViewById(R.id.color_edit_setting_title)).setText(R.string.add);
                    break;
                case DEFAULT_CARD:
                    ((TextView) view.findViewById(R.id.color_edit_setting_title)).setText(R.string.edit);
                    ((EditText) view.findViewById(R.id.color_edit_setting_name)).setText(colorList.get(selectedPos).getName());
                    ((EditText) view.findViewById(R.id.color_edit_setting_code)).setText(colorList.get(selectedPos).getCode());
                    break;
            }

            view.findViewById(R.id.color_edit_setting_confirm).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditEnd(view);
                    mEditSettingDialogFragment.dismiss();
                }
            });
            view.findViewById(R.id.color_edit_setting_cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditSettingDialogFragment.dismiss();
                }
            });
        }

        @Override
        public void onEditEnd(View view) {

            ColorInfo colorInfo = new ColorInfo(((EditText) view.findViewById(R.id.color_edit_setting_name)).getText().toString(),
                    ((EditText) view.findViewById(R.id.color_edit_setting_code)).getText().toString());

            switch (getItemViewType(selectedPos)) {
                case FIRST_CARD:
                    colorList.add(colorInfo);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case DEFAULT_CARD:
                    colorList.set(selectedPos, colorInfo);
                    mRecyclerViewAdapter.notifyItemChanged(selectedPos);
                    break;
            }

            mSettingPresenter.saveColor(colorList);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView Name, Code;
            public ImageView Add, Edit;
            public ViewHolder(View view) {
                super(view);
                Name = (TextView) view.findViewById(R.id.color_setting_name);
                Code = (TextView) view.findViewById(R.id.color_setting_code);
                Add = (ImageView) view.findViewById(R.id.setting_card_add);
                Edit = (ImageView) view.findViewById(R.id.color_setting_img);
            }
        }
    }
}
