package cn.cqs.wana.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.enums.FormEnum;
import cn.cqs.common.form.FormAdapter;
import cn.cqs.common.form.FormBean;
import cn.cqs.common.form.FormValue;
import cn.cqs.common.form.IFormConfigure;
import cn.cqs.common.form.SelectView;
import cn.cqs.common.utils.SpacesItemDecoration;
import cn.cqs.wana.R;

/**
 * Created by bingo on 2021/4/28.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 动态表单
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/28
 */
public class MultiFormActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick(R.id.btn_submit)
    public void clickEvent(View view){
        List<FormBean> data = formAdapter.getData();
        for (FormBean bean : data) {
            List<FormValue> values = bean.getValues();
            if (values != null){
                List<String> list = new ArrayList<>();
                for (FormValue value : values) {
                    if (value.isSelected()){
                        list.add(value.getValue());
                    }
                }
                LogUtils.e(TextUtils.join(",",list));
            }
        }
    }

    private FormAdapter formAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_form);
        initAdapter();
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }
    private void initAdapter() {
        formAdapter = new FormAdapter(getFormBeanList()){
            @Override
            protected void convertTypeTitle(BaseViewHolder helper, FormBean item) {
                super.convertTypeTitle(helper, item);
                helper.setText(R.id.tv_name,item.getName());
            }

            @Override
            protected void convertTypeInput(BaseViewHolder helper, FormBean item) {
                super.convertTypeInput(helper, item);
                helper.setText(R.id.tv_name,item.getName());
                helper.setVisible(R.id.tv_must,item.isMast());
            }

            @Override
            protected void convertTypeRadio(BaseViewHolder helper, FormBean item) {
                super.convertTypeRadio(helper, item);
                helper.setText(R.id.tv_name,item.getName());
                helper.setVisible(R.id.tv_must,item.isMast());
                SelectView selectView = helper.getView(R.id.selectView);
                List<FormValue> values = item.getValues();
                if (values != null){
                    selectView.setData(values);
                }
            }

            @Override
            protected void convertTypeCheckbox(BaseViewHolder helper, FormBean item) {
                super.convertTypeCheckbox(helper, item);
                helper.setText(R.id.tv_name,item.getName());
                helper.setVisible(R.id.tv_must,item.isMast());
                SelectView selectView = helper.getView(R.id.selectView);
                selectView.setRadio(false);
                List<FormValue> values = item.getValues();
                if (values != null){
                    selectView.setData(values);
                }
            }

            @Override
            protected void convertTypeSelect(BaseViewHolder helper, FormBean item) {
                super.convertTypeSelect(helper, item);
                helper.setText(R.id.tv_name,item.getName());
                helper.setVisible(R.id.tv_must,item.isMast());
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(this);
        itemDecoration.setParam(R.color.color_F6F8FC,1,10,10);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(formAdapter);
    }
    private List<FormBean> getFormBeanList(){
        List<FormBean> result = new ArrayList<>();
        result.add(new FormBean(FormEnum.Title,"基本信息"));
        result.add(new FormBean(FormEnum.Input, true, "姓名", "name", "", null, view -> {
            EditText editText = view.findViewById(R.id.et_input);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setHint("请输入姓名");
        }));
        result.add(new FormBean(FormEnum.Input, true, "密码", "sex", "", null, view -> {
            EditText editText = view.findViewById(R.id.et_input);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
            editText.setHint("请输入密码");
        }));
        result.add(new FormBean(FormEnum.Select,true,"爱好","like","",null,null));
        result.add(new FormBean(FormEnum.Radio,false,"性别","sex","", getSexConfig(),null));
        result.add(new FormBean(FormEnum.Checkbox, true, "开发语言", "language", "", getCheckboxConfig(), view -> {
            SelectView selectView = view.findViewById(R.id.selectView);
            selectView.setSpanCount(1);
        }));
        result.add(new FormBean(FormEnum.Image,true,"上传身份证照片","photo","",null,null));
        return result;
    }
    private List<FormValue> getSexConfig(){
        List<FormValue> result = new ArrayList<>();
        result.add(new FormValue("男","0"));
        result.add(new FormValue("女","1"));
        return result;
    }
    private List<FormValue> getCheckboxConfig(){
        List<FormValue> result = new ArrayList<>();
        result.add(new FormValue("JAVA","0"));
        result.add(new FormValue("Android","1"));
        result.add(new FormValue("Vue","2"));
        result.add(new FormValue("React","3"));
        result.add(new FormValue("PHP","4"));
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
