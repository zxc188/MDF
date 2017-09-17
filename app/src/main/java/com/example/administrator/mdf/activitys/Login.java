package com.example.administrator.mdf.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mdf.apis.APIsourse;
import com.example.administrator.mdf.apis.User;
import com.example.administrator.mdf.requesbody.Loginbody;
import com.example.administrator.mdf.requesbody.Registerbody;
import com.example.administrator.mdf.responsebody.ErrorMessage;
import com.example.administrator.mdf.responsebody.LoginResponsebody;
import com.example.administrator.mdf.responsebody.RegisterResponsebody;

import com.example.administrator.mdf.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {
    private Button btlogin;
    private Button btregister;
    private AutoCompleteTextView ed_username;
    private EditText ed_password;
    private SharedPreferences sharedPreferences;
    private CheckBox remberpassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        sharedPreferences = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        btlogin=(Button)findViewById(R.id.bt_login);
        btregister=(Button)findViewById(R.id.bt_register);
        ed_username=(AutoCompleteTextView)findViewById(R.id.ed_user_name);
        ed_password=(EditText)findViewById(R.id.ed_password);
        remberpassword=(CheckBox)findViewById(R.id.checkBox);
        if(remberpassword.isChecked()){

        }
        //注册
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User service = APIsourse.getInstance().getAPIObject(User.class);
                Registerbody registerbody=new Registerbody();
                registerbody.setEmail(ed_username.getText().toString());
                registerbody.setNew_password(ed_password.getText().toString());
                registerbody.setDisplay_name("zxc188");
                registerbody.setFirst_name("zxc");
                registerbody.setLast_name("188");
                final Call<RegisterResponsebody> regis=service.register(registerbody);
                regis.enqueue(new Callback<RegisterResponsebody>() {
                    @Override
                    public void onResponse(Call<RegisterResponsebody> call, Response<RegisterResponsebody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(Login.this,response.body().toString(),Toast.LENGTH_LONG).show();
                        }else{
                            if(ed_password.length()<6){
                                Toast.makeText(Login.this, "您的密码必须至少6位", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Login.this, "您的邮箱不正确或邮箱已经被注册", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponsebody> call, Throwable t) {
                    }
                });

            }
        });

        //登录
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User service1 = APIsourse.getInstance().getAPIObject(User.class);
                Loginbody loginody = new Loginbody();
                loginody.setEmail(ed_username.getText().toString());
                loginody.setPassword(ed_password.getText().toString());
                final Call<LoginResponsebody> log=service1.login(loginody);
                log.enqueue(new Callback<LoginResponsebody>() {
                    @Override
                    public void onResponse(Call<LoginResponsebody> call, Response<LoginResponsebody> response) {
                        if (response.isSuccessful()) {
                            Log.i("login","请求成功");
                            sharedPreferences.edit().putString(ed_username.getText().toString(), ed_password.getText().toString()).commit();
                            Toast.makeText(Login.this, response.body().getSession_token(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ErrorMessage e = APIsourse.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponsebody> call, Throwable t) {
                        System.out.println("error：" + t.toString());
                        Toast.makeText(Login.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
