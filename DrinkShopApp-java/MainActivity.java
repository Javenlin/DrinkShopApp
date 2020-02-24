package com.osiog.myoldmancare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.facebook.accountkit.ui.LoginType;

public class MainActivity extends AppCompatActivity {

    //VAR_MIN

    //VAR_MAX

    //INICOMPO


    private void INICOMPO() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_main);

        INICOMPO();

    }

//    private View.OnClickListener onClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            switch (v.getId()){
//                case R.id.btn_continue:
//                    startLoginPage(LoginType.PHONE);
//                    break;
//            }
//
//        }
//    };




}
