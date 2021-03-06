package jcchen.goodsmanager.view.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import jcchen.goodsmanager.R;
import jcchen.goodsmanager.entity.ColorInfo;
import jcchen.goodsmanager.presenter.impl.SettingPresenterImpl;
import jcchen.goodsmanager.view.adapter.ColorListViewAdapter;
import jcchen.goodsmanager.view.listener.OnColorSelectedListener;
import jcchen.goodsmanager.view.widget.RoundedImageView;

public class ColorSelectDialogFragment extends DialogFragment {

    public static final String TAG = "ColorSelectDialogFragment";

    private SearchView mSearchView;
    private ListView mListView;
    private RoundedImageView mRoundedImageView;
    private TextView mTextView;

    private ColorListViewAdapter adapter;
    private SettingPresenterImpl mSettingPresenter;
    private OnColorSelectedListener listener;

    private ArrayList<ColorInfo> colorSelectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.color_select_dialog_layout, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTextView = (TextView) view.findViewById(R.id.color_preview);

        adapter = new ColorListViewAdapter(getActivity(), mSettingPresenter.getColorList());
        if (colorSelectList == null)
            colorSelectList = new ArrayList<>();
        for (int i = 0; i < colorSelectList.size(); i++) {
            long id = adapter.getItemId(colorSelectList.get(i).getName());
            if(id == -1)
                colorSelectList.remove(i);
            else
                adapter.setSelected((int) id, true);
        }

        mListView = (ListView) view.findViewById(R.id.color_list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.isSelected((int) adapter.getItemId(i))) {
                    adapter.setSelected((int) adapter.getItemId(i), false);
                    for (int j = 0; j < colorSelectList.size(); j++) {
                        if (colorSelectList.get(j).getName().equals(((ColorInfo) mListView.getItemAtPosition(i)).getName())) {
                            colorSelectList.remove(j);
                            break;
                        }
                    }
                } else {
                    adapter.setSelected((int) adapter.getItemId(i), true);
                    colorSelectList.add((ColorInfo) mListView.getItemAtPosition(i));
                }
                updateTextView();
            }
        });

        mRoundedImageView = (RoundedImageView) view.findViewById(R.id.color_confirm_button);
        mRoundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onColorSelected(colorSelectList);
                dismiss();
            }
        });

        mSearchView = (SearchView) view.findViewById(R.id.color_search);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setFocusable(false);
        mSearchView.setQueryHint(getResources().getString(R.string.search));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide input window.
                if (imm != null)
                    imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Custom SearchView.
        EditText searchEditText = (EditText) mSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextOnSecondary));
        searchEditText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextHint));
        searchEditText.setGravity(Gravity.CENTER);

        updateTextView();
    }

    public void setPresenter(SettingPresenterImpl presenter) {
        this.mSettingPresenter = presenter;
    }

    public void setListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }

    public void loadSavedData(ArrayList<ColorInfo> colorSelectList) {
        if (adapter == null)
            adapter = new ColorListViewAdapter(getActivity(), mSettingPresenter.getColorList());
        if (colorSelectList == null)
            colorSelectList = new ArrayList<>();
        this.colorSelectList = new ArrayList<>();
        for (int i = 0; i < colorSelectList.size(); i++)
            if (adapter.isExist(colorSelectList.get(i)))
                this.colorSelectList.add(colorSelectList.get(i));
    }

    private void updateTextView() {
        if (adapter == null)
            adapter = new ColorListViewAdapter(getActivity(), mSettingPresenter.getColorList());
        colorSelectList = adapter.sort(colorSelectList);

        String text = getResources().getString(R.string.color) + " : ";
        for(int i = 0; i < colorSelectList.size(); i++) {
            if(i > 0)
                text = text.concat("/");
            text = text.concat(colorSelectList.get(i).getName());
        }
        if(colorSelectList.size() == 0)
            text = text.concat("(" + getResources().getString(R.string.none_select) + ")");
        mTextView.setText(text);
    }
}
