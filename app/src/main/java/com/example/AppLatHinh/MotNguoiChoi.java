package com.example.AppLatHinh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MotNguoiChoi extends AppCompatActivity {
    Button bt1;
    GridView gvhinhanh;
     private hinhanhadapter adapter;
    ArrayList<hinhanh> arrayimage = new ArrayList<>();//dung de hien thi ra man hinh
    ArrayList<hinhanh> ketqua ;//mang de kiem tra dap an
    ArrayList<Integer>  mangDaXong = new ArrayList<>();
    int luotchoi =1;
    int idAnh1, idAnh2;
    int vitricu = -1;
    int daMoluot2 = 1;
    int daloadxonganh =1;
    int manchoi =0,soOManChoi=4;
    float thoigianchoi=0,thoiGianManChoi=20000;
    TextView txtManChoi,time1;
    ImageButton nha;
    CountDownTimer count;
    RandomPicture randomPicture = new RandomPicture();
    //--------------
    AmThanh amthanh = new AmThanh(this);
    Boolean music;
    //---------
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mot_nguoi_choi);
       // bt1=(Button) findViewById(R.id.button);
        txtManChoi=findViewById(R.id.manchoi);
        time1=findViewById(R.id.time);
        nha=findViewById(R.id.nha);

        gvhinhanh = (GridView) findViewById(R.id.gridviewhinhanh);
        adapter = new hinhanhadapter(this, R.layout.dong_hinh_anh, arrayimage);
        gvhinhanh.setAdapter(adapter);

        nha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtohome();
            }
        });
        //am thanh
        //load âm thanh vào ứng dụng
        music = getIntent().getBooleanExtra("amthanh",false);
        if(music){
            amthanh.loadAmThanh();
        }
        //-----------------------
        chuyenManChoi();
        //----------------------------------
        gvhinhanh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int vitri, long l) {
                //moi khi click
                //kiem tra vitri hien tai khac vitri cu,va khac nhung da xong
                if(vitri!=vitricu && !kiemTraDaCoTrongMang(mangDaXong, vitri) &&daMoluot2==1&&daloadxonganh==1){
                    if(music) amthanh.play_flip_sound();
                    flipImg(view,vitri);
                    //kiem tra luot
                    if(luotchoi==1){//tai luot 1
                        idAnh1= ketqua.get(vitri).getId();
                        luotchoi=2;//chuyen sang luot thu 2
                        vitricu =vitri;
                    }else{//tai luot 2
                        //tao do tre 0,5 s
                        daMoluot2=0;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                idAnh2= ketqua.get(vitri).getId();
                                if(idAnh1==idAnh2){
                                    //xoa di
                                    if(music) amthanh.play_correct_sound();
                                    arrayimage.get(vitri).setId(0);
                                    arrayimage.get(vitricu).setId(0);
                                    adapter.notifyDataSetChanged();
                                    mangDaXong.add(vitri);
                                    mangDaXong.add(vitricu);
                                    if(mangDaXong.size()==soOManChoi){
                                        count.cancel();
                                        if(music) amthanh.play_win_sound();
                                        thongBaoQuaMan(thoiGianManChoi,thoigianchoi);
                                    }
                                }else{
                                    //tro ve back ground cu
                                    arrayimage.get(vitri).setId(R.drawable.background);
                                    arrayimage.get(vitricu).setId(R.drawable.background);
                                    adapter.notifyDataSetChanged();
                                }
                                luotchoi =1;
                                daMoluot2 =1;
                            }
                        },500);
                    }
                }
                else{
                    if(music) amthanh.play_error_sound();
                }
            }
        });
    }

    private void datMacDinh(final int soluong) {
        ketqua = randomPicture.mangketqua(soluong);
        luotchoi =1;
        //mo het ra
        arrayimage.clear();
        for(int i =0;i<soluong;i++){
            arrayimage.add(new hinhanh(ketqua.get(i).getId()));
        }
        adapter.notifyDataSetChanged();
        //sau 1 s moi ve background
        daloadxonganh=0;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                arrayimage.clear();
                for(int i=0;i<soluong;i++){
                    arrayimage.add(new hinhanh(R.drawable.background));
                }
                adapter.notifyDataSetChanged();//load lai giao dien
                daloadxonganh =1;
                demThoiGian(thoiGianManChoi);
            }
        },2000);

        idAnh1 =0;idAnh2 =0;vitricu = -1;daMoluot2=1;
        mangDaXong.clear();
    }

    public boolean kiemTraDaCoTrongMang (ArrayList<Integer> arr,int giatri){
        for(int i=0;i<arr.size();i++){
            if(giatri==arr.get(i)) return true;
        }
        return false;
  }
    public void flipImg(final View currentImgage, final int index){
        //-> hàm dùng để lật một ảnh theo vị trí item
        //dùng animation scale
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(currentImgage, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(currentImgage, "scaleX", 0f, 1f);
        //tạo độ mượt cho chuyển động
        oa1.setInterpolator(new LinearInterpolator());
        oa1.setDuration(200);
        oa2.setDuration(200);
        oa2.setInterpolator(new LinearInterpolator());
        //thêm sự kiện khi kết thúc chuyển động thì đổi ảnh khác
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                arrayimage.get(index).setId(ketqua.get(index).getId());
                oa2.start();
                //cập nhật lại giao diện
                adapter.notifyDataSetChanged();
            }
        });
        oa1.start();
    }
    private void chuyenManChoi(){
        manchoi = manchoi+1;//tự động chuyển màn
        txtManChoi.setText("manchoi:"+manchoi);

        switch(manchoi){
            case 1:
                thoiGianManChoi = 20000;
                soOManChoi=4;
                gvhinhanh.setPadding(180,300,180,300);
                gvhinhanh.setNumColumns(2);//2x2
                break;
            case 2:
                thoiGianManChoi = 35000;
                soOManChoi=6;
                gvhinhanh.setPadding(180,200,180,200);
                gvhinhanh.setNumColumns(2);//2x3
                break;
            case 3:
                thoiGianManChoi = 50000;
                soOManChoi=12;
                gvhinhanh.setPadding(80,100,80,100);
                gvhinhanh.setNumColumns(3);//3x4
                break;
            case 4:
                thoiGianManChoi = 75000;
                soOManChoi=20;
                gvhinhanh.setPadding(20,20,20,20);
                gvhinhanh.setNumColumns(4);//4x5
                break;
            case 5:
                thoiGianManChoi = 120000;
                soOManChoi=24;
                gvhinhanh.setPadding(0,0,0,0);
                gvhinhanh.setNumColumns(4);//4x6
                break;
            default://màn chơi lớn hơn 5
                thoiGianManChoi = 200000;
                soOManChoi=30;
                gvhinhanh.setPadding(0,0,0,0);
                gvhinhanh.setNumColumns(5);//5x6
                break;
        }
        datMacDinh(soOManChoi);
    }
    private void thongBaoHetThoiGian(){
        final Dialog thongbao = new Dialog(this);
        thongbao.setContentView(R.layout.het_thoi_gian);
        Button btnYes, btnNo;
        btnYes = thongbao.findViewById(R.id.btnYes);
        btnNo = thongbao.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datMacDinh(soOManChoi);
                thongbao.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtohome();
            }
        });
        thongbao.setCanceledOnTouchOutside(false);
        thongbao.show();

    }
    private int numberStar(float tongthoigian,float thoigiandachoi){
        float tile = thoigiandachoi/tongthoigian;
        if(tile<0.4){
            return 1;
        }else if(tile<0.7){
            return 2;
        }else if(tile<1){
            return 3;
        }else{
            return 0;
        }
    }
    private void thongBaoQuaMan(float tongthoigian,float thoigiandachoi){
        int numstar = numberStar(tongthoigian,thoigiandachoi);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.ket_thuc_man_choi);
        final ImageButton star1,star2,star3,btnHome,btnReplay, btnNext;
        star1= dialog.findViewById(R.id.star1);
        star2= dialog.findViewById(R.id.star2);
        star3= dialog.findViewById(R.id.star3);
        //hien thi sao
        Animation appear = AnimationUtils.loadAnimation(this,R.anim.appear);
        Animation appear2 = AnimationUtils.loadAnimation(this,R.anim.appear2);
        Animation appear3 = AnimationUtils.loadAnimation(this,R.anim.appear3);

        switch(numstar){
            case 0:
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                star1.setVisibility(View.VISIBLE);
                star1.setAnimation(appear);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                star1.setVisibility(View.VISIBLE);
                star1.setAnimation(appear);
                star2.setVisibility(View.VISIBLE);
                star2.setAnimation(appear2);
                star3.setVisibility(View.INVISIBLE);
                break;
            case 3:
                star1.setVisibility(View.VISIBLE);
                star1.setAnimation(appear);
                star2.setVisibility(View.VISIBLE);
                star2.setAnimation(appear2);
                star3.setVisibility(View.VISIBLE);
                star3.setAnimation(appear3);
                break;
        }
        btnHome = dialog.findViewById(R.id.btnHome);
        btnNext = dialog.findViewById(R.id.btnNext);
        btnReplay = dialog.findViewById(R.id.btnReplay);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backtohome();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chuyenManChoi();
                dialog.dismiss();
            }
        });
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            datMacDinh(soOManChoi);
            dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private  void demThoiGian(final float tongthoigian){
        count = new CountDownTimer((long) tongthoigian, 1000) {
            @Override
            public void onTick(long time) {
                // sự kiện mỗi bước nhảy
                SimpleDateFormat dinhdang = new SimpleDateFormat("mm:ss");
                time1.setText(dinhdang.format(time));
                thoigianchoi=time;
            }
            @Override
            public void onFinish() {
                if(music) amthanh.play_gameover_sound();
                time1.setText("00:00");
                thongBaoHetThoiGian();
            }
        };
        count.start();
    }
    private void backtohome(){
        amthanh.pause();
        Intent intent=new Intent(MotNguoiChoi.this,ManHinhChinh.class);
        startActivity(intent);
    }
}
