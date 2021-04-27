package cn.cqs.wana.app.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.jeremyliao.liveeventbus.LiveEventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cqs.base.EditTextUtils;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.bean.EventBean;
import cn.cqs.common.utils.LiveEventBusUtils;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone)
    EditText phoneEt;
    @BindView(R.id.et_password)
    EditText passwordEt;
    @BindView(R.id.iv_eye)
    ImageView eyeIv;
    @BindView(R.id.tv_register)
    TextView registerTv;

    @OnClick({R.id.iv_eye,R.id.btn_login,R.id.tv_register,R.id.tv_forget})
    public void clickEvent(View view){
        switch (view.getId()){
            case R.id.iv_eye:
                EditTextUtils.changePasswordVisible(passwordEt);
                showEyeIcon = !showEyeIcon;
                eyeIv.setImageResource(showEyeIcon?R.mipmap.icon_eye_on:R.mipmap.icon_eye_close);
                break;
            case R.id.tv_register:
                toast("注册");
                LiveEventBusUtils.post(new EventBean(0,"注册"));
                //startActivity(new Intent(getActivity(),RegisterActivity.class));
                break;
            case R.id.tv_forget:
                toast("忘记密码");
                LiveEventBusUtils.post(new EventBean(1,"忘记密码"));
                //startActivity(new Intent(getActivity(),ForgetPasswordActivity.class));
                break;
            case R.id.btn_login:
                String phone = phoneEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11){
                    toast("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 20){
                    toast("请输入6-20字符长度的密码");
                    return;
                }
                toast("登录成功");
                break;
            default:
                break;
        }
    }
    //是否显示密码明文
    private boolean showEyeIcon = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }
}
