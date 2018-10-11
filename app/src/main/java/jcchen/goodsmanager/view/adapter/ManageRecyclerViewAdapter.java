package jcchen.goodsmanager.view.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

import jcchen.goodsmanager.R;
import jcchen.goodsmanager.entity.PurchaseInfo;

public class ManageRecyclerViewAdapter extends RecyclerView.Adapter<ManageRecyclerViewAdapter.ViewHolder> {

    private Context context;

    private Vector<PurchaseInfo> purchaseList;

    private ViewHolder selectedCard;

    public ManageRecyclerViewAdapter(Context context, Vector<PurchaseInfo> purchaseList) {
        this.context = context;
        if(purchaseList == null)
            purchaseList = new Vector<>();
        this.purchaseList = (Vector<PurchaseInfo>) purchaseList.clone();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manage_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        viewHolder.Card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedCard = viewHolder;
                selectCard(viewHolder);
                return true;
            }
        });
        viewHolder.Name.setText(purchaseList.get(position).getName());
        viewHolder.Type.setText(purchaseList.get(position).getType());
        viewHolder.Numbers.setText(purchaseList.get(position).getNumbers());
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout Card;
        public TextView Name, Type, Numbers;
        public ViewHolder(View view) {
            super(view);
            Card = view.findViewById(R.id.manage_card);
            Name = view.findViewById(R.id.manage_goods_name);
            Type = view.findViewById(R.id.manage_type);
            Numbers = view.findViewById(R.id.manage_numbers);
        }
    }

    private void selectCard(ViewHolder viewHolder) {
        viewHolder.Card.setElevation(8 * context.getResources().getDisplayMetrics().density);
    }

    private void resumeCard() {
        selectedCard.Card.setElevation(1 * context.getResources().getDisplayMetrics().density);
    }
}
