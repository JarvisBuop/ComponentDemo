package com.ostak.justplayteacher.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;
import com.ostak.justplayteacher.MyApp;
import com.ostak.justplayteacher.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JarvisDong on 2018/5/26.
 *
 * @Description:
 * @see:
 */

public class WalletFragment extends MainBaseFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.img_msg)
    ImageView imgMsg;
    @BindView(R.id.img_quit)
    ImageView imgQuit;
    @BindView(R.id.img_wallet_icon)
    ImageView imgWalletIcon;
    @BindView(R.id.txt_wallet_total_label)
    TextView txtWalletTotalLabel;
    @BindView(R.id.txt_wallet_total)
    TextView txtWalletTotal;
    @BindView(R.id.txt_wallet_withdraw_label)
    TextView txtWalletWithdrawLabel;
    @BindView(R.id.txt_wallet_withdraw)
    TextView txtWalletWithdraw;
    @BindView(R.id.line1)
    View viewLine1;
    @BindView(R.id.line2)
    View viewLine2;
    @BindView(R.id.txt_wallet_provider)
    TextView txtWalletProvider;
    @BindView(R.id.txt_wallet_current)
    TextView txtWalletCurrent;
    @BindView(R.id.txt_wallet_bank)
    TextView txtWalletBank;
    @BindView(R.id.txt_wallet_bank_name)
    TextView txtWalletBankName;
    @BindView(R.id.txt_wallet_bank_phone)
    TextView txtWalletBankPhone;
    @BindView(R.id.txt_wallet_bank_card)
    TextView txtWalletBankCard;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.txt_history)
    TextView txtHistory;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    CommonAdapter mAdapter;
    ArrayList mDataList = new ArrayList();

    public static WalletFragment newInstance(int containId) {

        Bundle args = new Bundle();

        WalletFragment fragment = new WalletFragment();
        fragment.setContainerId(containId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initView(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        ((TextView)viewLine1.findViewById(R.id.txt_line)).setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_cash));
        ((TextView)viewLine2.findViewById(R.id.txt_line)).setText(MyApp.getAppInstansce().getResources().getString(R.string.line_txt_operate_record));
        initRecycler();

        initFakeData();
    }

    private void initFakeData() {
        mDataList.add(new Object());
        mDataList.add(new Object());
        mDataList.add(new Object());
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new CommonAdapter(mContext,R.layout.item_wallet,mDataList) {
            @Override
            protected void convert(ViewHolder holder, Object o, int position) {
                holder.setText(R.id.course_one,"2018-19-19 17:33:33");
                holder.setText(R.id.course_two,"金额:222");
                holder.setText(R.id.course_three,"处理中");
            }
        };
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick({R.id.img_msg, R.id.img_quit, R.id.btn_apply, R.id.txt_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_msg:
                break;
            case R.id.img_quit:
                break;
            case R.id.btn_apply:
                break;
            case R.id.txt_history:
                break;
        }
    }
}
