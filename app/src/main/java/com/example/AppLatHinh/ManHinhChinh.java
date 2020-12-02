package com.example.AppLatHinh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ManHinhChinh extends AppCompatActivity {
ImageView back,sound;
Button btnMotNguoiChoi,btnHaiNguoiChoi;
Boolean music =true;//dang bat
AmThanh amThanh = new AmThanh(this);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_hinh_chinh);
        back=(ImageView) findViewById(R.id.back1);
        sound=(ImageView) findViewById(R.id.sound1);
        if(music) amThanh.loadAmThanh();
        btnMotNguoiChoi= findViewById(R.id.btnMotNguoiChoi);
        btnHaiNguoiChoi=findViewById(R.id.btnHaiNguoiChoi);
        btnMotNguoiChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chuyenToiMan1=new Intent(ManHinhChinh.this,MotNguoiChoi.class);
                if(music){
                    amThanh.play_click_sound();
                    chuyenToiMan1.putExtra("amthanh",true);
                }else{
                    chuyenToiMan1.putExtra("amthanh",false);
                }
                startActivity(chuyenToiMan1);
            }
        });
        btnHaiNguoiChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chuyenToiMan2=new Intent(ManHinhChinh.this, HaiNguoiChoi.class);
                if(music){
                    amThanh.play_click_sound();
                    chuyenToiMan2.putExtra("amthanh",true);
                }else{
                    chuyenToiMan2.putExtra("amthanh",false);
                }
                startActivity(chuyenToiMan2);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thongBao();
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(music ){
                    music =false;
                    sound.setImageResource(R.drawable.nosound);

                }else if(music==false){
                    music =true;
                    sound.setImageResource(R.drawable.sound);
                }
            }
        });

    }
    public void thongBao(){
        final AlertDialog.Builder tb = new AlertDialog.Builder(this);
        tb.setTitle("cảnh báo");
        tb.setMessage("bạn có chắc chắc muốn thoát ứng dụng không");
        tb.setPositiveButton("có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        tb.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tb.setCancelable(true);
            }
        });
        tb.show();
    }

}