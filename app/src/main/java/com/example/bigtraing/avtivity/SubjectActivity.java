package com.example.bigtraing.avtivity;

import android.content.Intent;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bigtraing.R;
import com.example.bigtraing.adapter.SubjectAdapter;
import com.example.bigtraing.base.BaseMvpActivity;
import com.example.bigtraing.model.LauchModel;
import com.example.frame.ApiConfig;
import com.example.frame.bean.BaseInfo;
import com.example.frame.bean.SpecialtyChooseEntity;
import com.example.frame.constants.ConstantKey;
import com.example.utils.newAdd.SharedPrefrenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.bigtraing.constant.JumpConstant.JUMP_KEY;
import static com.example.bigtraing.constant.JumpConstant.SPLASH_TO_SUB;
import static com.example.bigtraing.constant.JumpConstant.SUB_TO_LOGIN;


public class SubjectActivity extends BaseMvpActivity<LauchModel> {

    private List<SpecialtyChooseEntity> mListData = new ArrayList<>();
    @BindView(R.id.title_content)
    TextView titleContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SubjectAdapter mAdapter;
    @BindView(R.id.more_content)
    TextView moreContent;
    private String mFrom;

    @Override
    public LauchModel setModel() {
        return new LauchModel();
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_subject;
    }

    @Override
    public void setUpView() {
        titleContent.setText(getString(R.string.select_subject));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SubjectAdapter(mListData, this);
        recyclerView.setAdapter(mAdapter);
        moreContent.setText("完成");
        moreContent.setOnClickListener(v -> {
            if (mApplication.getSelectedInfo() == null) {
                showToast("请选择专业");
                return;
            }
            if (mFrom.equals(SPLASH_TO_SUB)) {
                if (mApplication.isLogin()) {
                    startActivity(new Intent(SubjectActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SubjectActivity.this, LoginActivity.class).putExtra(JUMP_KEY, SUB_TO_LOGIN));
                }
            }
            finish();
        });
    }

    @Override
    public void setUpData() {
        mFrom = getIntent().getStringExtra(JUMP_KEY);
        List<SpecialtyChooseEntity> info = SharedPrefrenceUtils.getSerializableList(this, ConstantKey.SUBJECT_LIST);
        if (info != null) {
            mListData.addAll(info);
            mAdapter.notifyDataSetChanged();
        } else
            mPresenter.getData(ApiConfig.SUBJECT);
    }

    @Override
    public void netSuccess(int whichApi, Object[] pD) {
        switch (whichApi) {
            case ApiConfig.SUBJECT:
                BaseInfo<List<SpecialtyChooseEntity>> info = (BaseInfo<List<SpecialtyChooseEntity>>) pD[0];
                mListData.addAll(info.result);
                mAdapter.notifyDataSetChanged();
                SharedPrefrenceUtils.putSerializableList(this, ConstantKey.SUBJECT_LIST, mListData);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPrefrenceUtils.putObject(this, ConstantKey.SUBJECT_SELECT, mApplication.getSelectedInfo());
    }

    @OnClick(R.id.back_image)
    public void onViewClicked() {
        finish();
    }
}
