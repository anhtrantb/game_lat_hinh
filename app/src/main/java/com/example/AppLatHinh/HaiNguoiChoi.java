package com.example.AppLatHinh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class HaiNguoiChoi extends AppCompatActivity {
    ImageButton nguoi1,nguoi2,choi_lai,house;
    TextView diem_nguoi_1,diem_nguoi_2;

    GridView gridview;
    private hinhanhadapter adapter;
    ArrayList<hinhanh> arrayimage =new ArrayList<>();//dung de hien thi ra man hinh
    ArrayList<hinhanh> ketqua = new ArrayList<>();//mang de kiem tra dap an
    ArrayList<Integer>  mangDaXong = new ArrayList<>();

    int luotchoi =1;
    int idAnh1, idAnh2;
    int vitricu = -1;
    int daMoluot2 = 1;
    int daloadxonganh =1;
    int nguoichoi =1;
    int diem1=0,diem2=0;
    //---------------
    Boolean music;//co bat loa hay khong
    AmThanh amthanh = new AmThanh(this);
    RandomPicture randomPicture = new RandomPicture();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hainguoichoi);
        nguoi1=findViewById(R.id.nguoi1);
        nguoi2=findViewById(R.id.nguoi2);
        diem_nguoi_1=findViewById(R.id.diem1);
        diem_nguoi_2=findViewById(R.id.diem2);
        choi_lai=findViewById(R.id.quaylai);
        house=findViewById(R.id.house);
        gridview=findViewById(R.id.gridview);
        music = getIntent().getBooleanExtra("amthanh",true);
        if(music){
            amthanh.loadAmThanh();
        }
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoHome();
            }
        });
        choi_lai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datMacDinh(30);
            }
        });
        adapter = new hinhanhadapter(this,R.layout.dong_hinh_anh,arrayimage);
        gridview.setAdapter(adapter);
        datMacDinh(30);//mac dinh 30 o
        
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int vitri, long l) {
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
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void run() {
                                idAnh2= ketqua.get(vitri).getId();
                                if(idAnh1==idAnh2){
                                    //xoa di- da thang--------------------
                                    if(music) amthanh.play_correct_sound();
                                    if(nguoichoi==1) {
                                        diem1++;
                                        diem_nguoi_1.setText(diem1+"");
                                    }
                                    else {
                                        diem2++;
                                        diem_nguoi_2.setText(diem2+"");
                                    }
                                    //-----------------
                                    arrayimage.get(vitri).setId(0);
                                    arrayimage.get(vitricu).setId(0);
                                    adapter.notifyDataSetChanged();
                                    mangDaXong.add(vitri);
                                    mangDaXong.add(vitricu);
                                    if(mangDaXong.size()==30){
                                        if(music) amthanh.play_win_sound();
                                        thongBaoKetQua(diem1,diem2);
                                    }
                                }else{//khong thang
                                    //chuyen luot choi ----------------------
                                    if(nguoichoi==1) {
                                        nguoichoi = 2;
                                        nguoi1.setBackgroundResource(0);
                                        nguoi2.setBackgroundResource(R.color.mau_luot_choi);
                                    }
                                    else if(nguoichoi==2) {
                                        nguoichoi = 1;
                                        nguoi1.setBackgroundResource(R.color.mau_luot_choi);
                                        nguoi2.setBackgroundResource(0);
                                    }
                                    //tro ve back ground cu-----------------
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
    //-------------------------------------------------------------------------------
    @SuppressLint("ResourceAsColor")
    private void datMacDinh(final int soluong) {
        nguoi1.setBackgroundResource(R.color.mau_luot_choi);
        nguoi2.setBackgroundResource(0);
        diem1 =0;diem2=0;
        diem_nguoi_2.setText("0");
        diem_nguoi_1.setText("0");
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
            }
        },2000);

        idAnh1 =0;idAnh2 =0;vitricu = -1;daMoluot2=1;
        mangDaXong.clear();
    }

    public boolean kiemTraDaCoTrongMang (ArrayList<Integer> arr, int giatri){
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
    public void thongBaoKetQua(int diem1, int diem2){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("kết quả");
        if(diem1>diem2)
            dialog.setMessage("người chơi 1 đã thắng \nchơi lại?");
        else if(diem1<diem2)
            dialog.setMessage("người chơi 2 đã thắng \nchơi lại?");
        else
            dialog.setMessage("kết quả hòa \nchơi lại?");
        dialog.setPositiveButton("có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                datMacDinh(30);
            }
        });
        dialog.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backtoHome();
            }
        });
        dialog.show();
    }
    public void backtoHome(){
        amthanh.pause();
        Intent intent =new Intent(HaiNguoiChoi.this,ManHinhChinh.class);
        startActivity(intent);
    }
}