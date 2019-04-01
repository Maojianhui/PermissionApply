package com.punuo.sys.app.permissionapply;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.punuo.sys.app.tools.PermissionUtils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.bt_login)
    Button btLogin;
    protected CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpSplash();
        init();
    }

    private void setUpSplash() {
        Subscription splash = Observable.timer(2000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> requestPermission());
        mCompositeSubscription.add(splash);
    }

    private void init() {
        Toast.makeText(this,"欢迎来到召唤师峡谷",Toast.LENGTH_SHORT).show();
    }

    private PermissionUtils.PermissionGrant mGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {

        }

        @Override
        public void onPermissionCancel() {
            Toast.makeText(MainActivity.this, getString(R.string.alirtc_permission), Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    private void requestPermission(){
        PermissionUtils.requestMultiPermissions(this,
                new String[]{
                        PermissionUtils.PERMISSION_CAMERA,
                        PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE,
                        PermissionUtils.PERMISSION_RECORD_AUDIO,
                        PermissionUtils.PERMISSION_READ_EXTERNAL_STORAGE}, mGrant);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PermissionUtils.CODE_MULTI_PERMISSION){
            PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, mGrant);
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PermissionUtils.REQUEST_CODE_SETTING){
            new Handler().postDelayed(this::requestPermission, 500);

        }
    }

    @OnClick({R.id.bt_login})
    public void onClick(View v)
    {
        switch (v.getId()){
            case R.id.bt_login:
                new AlertDialog.Builder(this)
                        .setMessage("全军出击")
                        .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this,"First blood",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
        }
    }

}

