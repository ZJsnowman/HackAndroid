package com.iboxpay.hackandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FakeCashboxLoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_cashbox_login);
        mLoginBtn = (Button) findViewById(R.id.btn_login_submit);
        mLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_submit:
                moveTaskToBack(true);
                break;
            default:

        }
    }
}
