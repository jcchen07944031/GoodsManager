package jcchen.goodsmanager.view.container;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;

import jcchen.goodsmanager.R;
import jcchen.goodsmanager.entity.ColorInfo;
import jcchen.goodsmanager.entity.PurchaseInfo;
import jcchen.goodsmanager.entity.SizeInfo;
import jcchen.goodsmanager.entity.TypeInfo;
import jcchen.goodsmanager.presenter.impl.PurchasePresenterImpl;
import jcchen.goodsmanager.presenter.impl.SettingPresenterImpl;
import jcchen.goodsmanager.view.activity.MainActivity;
import jcchen.goodsmanager.view.adapter.SizePurchaseViewPagerAdapter;
import jcchen.goodsmanager.view.fragment.ColorSelectDialogFragment;
import jcchen.goodsmanager.view.fragment.PurchaseFragment;
import jcchen.goodsmanager.view.fragment.SizeSelectDialogFragment;
import jcchen.goodsmanager.view.listener.OnColorSelectedListener;
import jcchen.goodsmanager.view.listener.OnSizeSelectedListener;

public class PurchaseContainer extends ScrollView implements Container, OnColorSelectedListener, OnSizeSelectedListener {

    private Context context;

    private TypeInfo currentType;

    private ConstraintLayout purchaseBaseLayout;
    private LinearLayout numbersLayout, nameLayout, priceLayout, incomeTKLayout, flexibleLayout, colorLayout, sizeLayout;
    private Button colorSelect, sizeSelect, submit;
    private TextView sizeText, colorText;
    private ViewPager mViewPager;
    private EditText material, numbers, mall, position, name, listPrice, actualPrice, incomeK, incomeT;
    private LinePageIndicator pageIndicator;
    private Spinner flexible;

    private ColorSelectDialogFragment mColorSelectDialogFragment;
    private SizeSelectDialogFragment mSizeSelectDialogFragment;
    private SettingPresenterImpl mSettingPresenter;
    private PurchasePresenterImpl mPurchasePresenter;
    private SizePurchaseViewPagerAdapter mSizePurchaseViewPagerAdapter;

    private ArrayList<SizePurchaseViewPagerContainer> pageList;
    private ArrayList<ColorInfo> colorSelectList;
    private ArrayList<SizeInfo> sizeSelectList;

    private int Mode;

    public PurchaseContainer(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PurchaseContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        purchaseBaseLayout = (ConstraintLayout) findViewById(R.id.purchase_base_layout);
        purchaseBaseLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) ((MainActivity) context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(((MainActivity) context).getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        numbersLayout = (LinearLayout) findViewById(R.id.purchase_numbers_layout);
        numbers = (EditText) findViewById(R.id.purchase_numbers);
        numbers.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    mall.requestFocus();
                    return true;
                }
                return false;
            }
        });
        mall = (EditText) findViewById(R.id.purchase_mall);
        mall.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    position.requestFocus();
                    return true;
                }
                return false;
            }
        });
        position = (EditText) findViewById(R.id.purchase_position);
        position.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    name.requestFocus();
                    return true;
                }
                return false;
            }
        });

        nameLayout = (LinearLayout) findViewById(R.id.purchase_name_layout);
        name = (EditText) findViewById(R.id.purchase_name);
        name.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    listPrice.requestFocus();
                    return true;
                }
                return false;
            }
        });

        priceLayout = (LinearLayout) findViewById(R.id.purchase_price_layout);
        listPrice = (EditText) findViewById(R.id.purchase_list_price);
        listPrice.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    actualPrice.requestFocus();
                    return true;
                }
                return false;
            }
        });
        actualPrice = (EditText) findViewById(R.id.purchase_actual_price);
        actualPrice.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    incomeK.requestFocus();
                    return true;
                }
                return false;
            }
        });

        incomeTKLayout = (LinearLayout) findViewById(R.id.purchase_income_k_t_layout);
        incomeK = (EditText) findViewById(R.id.purchase_income_k);
        incomeK.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    incomeT.requestFocus();
                    return true;
                }
                return false;
            }
        });
        incomeT = (EditText) findViewById(R.id.purchase_income_t);
        incomeT.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    purchaseBaseLayout.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) ((MainActivity) context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(((MainActivity) context).getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        flexibleLayout = (LinearLayout) findViewById(R.id.flexible_layout);
        flexible = (Spinner) findViewById(R.id.flexible_spinner);
        ArrayAdapter<CharSequence> mArrayAdapter = ArrayAdapter.createFromResource(context,
                R.array.flexible_array, R.layout.spinner_text);
        mArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        flexible.setAdapter(mArrayAdapter);
        flexible.setSelection(0);

        colorSelect = (Button) findViewById(R.id.purchase_color);
        colorSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorSelectDialogFragment.loadSavedData(colorSelectList);
                mColorSelectDialogFragment.show(((Activity) context).getFragmentManager(), ColorSelectDialogFragment.TAG);
            }
        });

        material = (EditText) findViewById(R.id.purchase_material);

        colorLayout = (LinearLayout) findViewById(R.id.purchase_color_layout);
        colorText = (TextView) findViewById(R.id.purchase_color_text);

        sizeSelect = (Button) findViewById(R.id.purchase_size_select);
        sizeSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSizeSelectDialogFragment.loadSavedData(sizeSelectList);
                mSizeSelectDialogFragment.show(((Activity) context).getFragmentManager(), SizeSelectDialogFragment.TAG);
            }
        });

        sizeLayout = (LinearLayout) findViewById(R.id.purchase_size_layout);
        sizeText = (TextView) findViewById(R.id.purchase_size_text);

        mViewPager = (ViewPager) findViewById(R.id.purchase_pager);
        mViewPager.setAdapter(mSizePurchaseViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager inputMethodManager = (InputMethodManager) ((MainActivity) context).getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(((MainActivity) context).getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pageIndicator = (LinePageIndicator) findViewById(R.id.page_indicator);
        pageIndicator.setViewPager(mViewPager);

        submit = (Button) findViewById(R.id.purchase_submit);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setMessage(R.string.purchase_confirm_message)
                        .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (checkNecessaryField()) {
                                    if (Mode == PurchaseFragment.MODE_ADD) {
                                        mPurchasePresenter.savePurchaseInfo(collectPurchaseInfo());
                                    }
                                    else if (Mode == PurchaseFragment.MODE_EDIT) {
                                        mPurchasePresenter.updatePurchaseInfo(((MainActivity) context).getSelectedCard(), collectPurchaseInfo());
                                    }
                                    onBackPressed();
                                }
                            }
                        })
                        .setNegativeButton(R.string.confirm_no, null)
                        .show();

            }
        });
    }

    @Override
    public void init() {
        mPurchasePresenter = new PurchasePresenterImpl(context);
        mSettingPresenter = new SettingPresenterImpl(context);

        // Color select.
        mColorSelectDialogFragment = new ColorSelectDialogFragment();
        mColorSelectDialogFragment.setPresenter(mSettingPresenter);
        mColorSelectDialogFragment.setListener(this);

        // Size select.
        mSizeSelectDialogFragment = new SizeSelectDialogFragment();
        mSizeSelectDialogFragment.setPresenter(mSettingPresenter);
        mSizeSelectDialogFragment.setListener(this);

        pageList = new ArrayList<>();
        pageList.add(new SizePurchaseViewPagerContainer(context));
        pageList.add(new SizePurchaseViewPagerContainer(context));
        pageList.add(new SizePurchaseViewPagerContainer(context));
        mSizePurchaseViewPagerAdapter = new SizePurchaseViewPagerAdapter(context, pageList);

        colorSelectList = new ArrayList<>();
        sizeSelectList = new ArrayList<>();
    }

    @Override
    public void showItem(Object object) {
        currentType = (TypeInfo) object;
        for (int i = 0; i < pageList.size(); i++)
            pageList.get(i).showItem(object);
    }

    @Override
    public void onBackPressed() {
        ((MainActivity) context).onBackPressed();
    }

    @Override
    public void postResult() {

    }

    @Override
    public void onColorSelected(ArrayList<ColorInfo> colorSelectList) {
        String color = "";
        for (int i = 0; i < colorSelectList.size(); i++) {
            if (i > 0)
                color = color.concat("/");
            color = color.concat(colorSelectList.get(i).getName());
        }
        if (colorSelectList.size() == 0)
            color = getResources().getString(R.string.none_select);
        colorText.setText(color);
        this.colorSelectList = colorSelectList;
    }

    @Override
    public void onSizeSelected(ArrayList<SizeInfo> sizeSelectList) {
        String size = "";
        for (int i = 0; i < sizeSelectList.size(); i++) {
            if (i > 0)
                size = size.concat("/");
            size = size.concat(sizeSelectList.get(i).getName());
        }
        if (sizeSelectList.size() == 0)
            size = "F";
        sizeText.setText(size);
        this.sizeSelectList = sizeSelectList;
    }

    public void clear() {
        new AlertDialog.Builder(context)
                .setMessage(R.string.purchase_clear_confirm)
                .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // numbers.setText("");
                        mall.setText("");
                        position.setText("");
                        name.setText("");
                        listPrice.setText("");
                        actualPrice.setText("");
                        incomeK.setText("");
                        incomeT.setText("");
                        flexible.setSelection(0);
                        sizeSelectList.clear();
                        onSizeSelected(sizeSelectList);
                        colorSelectList.clear();
                        onColorSelected(colorSelectList);
                        material.setText("");
                        for (int j = 0; j < pageList.size(); j++)
                            pageList.get(j).showItem(new PurchaseInfo.SizeStruct());
                    }
                })
                .setNegativeButton(R.string.confirm_no, null)
                .show();
    }

    private boolean checkNecessaryField() {
        return true;
    }

    private PurchaseInfo collectPurchaseInfo() {
        PurchaseInfo mPurchaseInfo = new PurchaseInfo();
        mPurchaseInfo.setTypeInfo(currentType);
        mPurchaseInfo.setNumbers(numbers.getText().toString());
        mPurchaseInfo.setMall(mall.getText().toString());
        mPurchaseInfo.setPosition(position.getText().toString());
        mPurchaseInfo.setName(name.getText().toString());
        mPurchaseInfo.setListPrice(MainActivity.safeParseInt(listPrice.getText().toString()));
        mPurchaseInfo.setActualPrice(MainActivity.safeParseInt(actualPrice.getText().toString()));
        mPurchaseInfo.setIncomeK(MainActivity.safeParseInt(incomeK.getText().toString()));
        mPurchaseInfo.setIncomeT(MainActivity.safeParseInt(incomeT.getText().toString()));
        mPurchaseInfo.setFlexible(flexible.getSelectedItem().toString());
        mPurchaseInfo.setColorList(colorSelectList);
        mPurchaseInfo.setMaterial(material.getText().toString());
        mPurchaseInfo.setSizeList(sizeSelectList);
        mPurchaseInfo.setSizeStructList(mSizePurchaseViewPagerAdapter.collectSizeStruct());
        return mPurchaseInfo;
    }

    public void loadPurchaseInfo(int Mode) {
        this.Mode = Mode;
        PurchaseInfo purchaseInfo = ((MainActivity) context).getSelectedCard();
        if (purchaseInfo != null && Mode == PurchaseFragment.MODE_EDIT) {
            numbers.setText(purchaseInfo.getNumbers());
            mall.setText(purchaseInfo.getMall());
            position.setText(purchaseInfo.getPosition());
            name.setText(purchaseInfo.getName());
            listPrice.setText(purchaseInfo.getListPrice() + "");
            actualPrice.setText(purchaseInfo.getActualPrice() + "");
            incomeK.setText(purchaseInfo.getIncomeK() + "");
            incomeT.setText(purchaseInfo.getIncomeT() + "");
            for (int i = 0; i < context.getResources().getStringArray(R.array.flexible_array).length; i++) {
                flexible.setSelection(0);
                if (purchaseInfo.getFlexible().equals(context.getResources().getStringArray(R.array.flexible_array)[i])) {
                    flexible.setSelection(i);
                    break;
                }
            }
            onSizeSelected(purchaseInfo.getSizeList());
            onColorSelected(purchaseInfo.getColorList());
            material.setText(purchaseInfo.getMaterial());
            for (int i = 0; i < pageList.size(); i++)
                pageList.get(i).showItem(purchaseInfo.getSizeStructList().get(i));
        }
        else {
            int nextNumber = 1;
            ArrayList<PurchaseInfo> purchaseList = mPurchasePresenter.getPurchaseList();
            for (int i = 0; i < purchaseList.size(); i++) {
                String num = purchaseList.get(i).getNumbers();
                if (num.contains(mSettingPresenter.getDate().encode())) {
                    nextNumber = Math.max(nextNumber, Integer.valueOf(num.substring(num.length() - 2, num.length())) + 1);
                }
            }
            numbers.setText(mSettingPresenter.getDate().encode() + String.format("%02d", nextNumber));
        }
    }
}
