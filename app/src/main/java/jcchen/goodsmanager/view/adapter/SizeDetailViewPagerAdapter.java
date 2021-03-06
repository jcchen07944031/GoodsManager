package jcchen.goodsmanager.view.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jcchen.goodsmanager.view.container.SizeDetailViewPagerContainer;

public class SizeDetailViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<SizeDetailViewPagerContainer> pageList;

    public SizeDetailViewPagerAdapter(Context context, ArrayList<SizeDetailViewPagerContainer> pageList) {
        this.context = context;
        this.pageList = pageList;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageList.get(position));
        return pageList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
