package com.jarvisdong.teaapp.ui.frg;

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

import com.jarvisdong.teaapp.R;
import com.jarvisdong.teaapp.R2;
import com.jarvisdong.uikit.adapter.CommonAdapter;
import com.jarvisdong.uikit.adapter.itemanager.ViewHolder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JarvisDong on 2018/5/26.
 *
 * @Description:
 * @see:
 */

public class WalletFragment extends MainBaseFragment {

    @BindView(R2.id.txt_title)
    TextView txtTitle;
    @BindView(R2.id.edit_search)
    EditText editSearch;
    @BindView(R2.id.img_msg)
    ImageView imgMsg;
    @BindView(R2.id.img_quit)
    ImageView imgQuit;
    @BindView(R2.id.img_wallet_icon)
    ImageView imgWalletIcon;
    @BindView(R2.id.txt_wallet_total_label)
    TextView txtWalletTotalLabel;
    @BindView(R2.id.txt_wallet_total)
    TextView txtWalletTotal;
    @BindView(R2.id.txt_wallet_withdraw_label)
    TextView txtWalletWithdrawLabel;
    @BindView(R2.id.txt_wallet_withdraw)
    TextView txtWalletWithdraw;
    @BindView(R2.id.line1)
    View viewLine1;
    @BindView(R2.id.line2)
    View viewLine2;
    @BindView(R2.id.txt_wallet_provider)
    TextView txtWalletProvider;
    @BindView(R2.id.txt_wallet_current)
    TextView txtWalletCurrent;
    @BindView(R2.id.txt_wallet_bank)
    TextView txtWalletBank;
    @BindView(R2.id.txt_wallet_bank_name)
    TextView txtWalletBankName;
    @BindView(R2.id.txt_wallet_bank_phone)
    TextView txtWalletBankPhone;
    @BindView(R2.id.txt_wallet_bank_card)
    TextView txtWalletBankCard;
    @BindView(R2.id.btn_apply)
    Button btnApply;
    @BindView(R2.id.txt_history)
    TextView txtHistory;
    @BindView(R2.id.recycler_view)
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
        ((TextView)viewLine1.findViewById(R.id.txt_line)).setText(getActivity().getResources().getString(R.string.line_txt_cash));
        ((TextView)viewLine2.findViewById(R.id.txt_line)).setText(getActivity().getResources().getString(R.string.line_txt_operate_record));
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

}
