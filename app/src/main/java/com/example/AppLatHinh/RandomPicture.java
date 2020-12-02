package com.example.AppLatHinh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomPicture {
    public boolean kiemTraDaCoTrongMang (ArrayList<Integer> arr,int giatri){
        for(int i=0;i<arr.size();i++){
            if(giatri==arr.get(i)) return true;
        }
        return false;
    }
    public ArrayList<hinhanh> mangketqua(int soluong) {
        // tra ve mang ket qua theo so luong yeu cau
        ArrayList<hinhanh> kq = new ArrayList<>();

        int mangToanBoAnh[]={R.drawable.a1,R.drawable.a2,R.drawable.a3,R.drawable.a4,
                R.drawable.a5,R.drawable.a6,R.drawable.a7,R.drawable.a8,
                R.drawable.a9,R.drawable.a10,R.drawable.a11,R.drawable.a12,
                R.drawable.a13,R.drawable.a14,R.drawable.a15,R.drawable.a16,
                R.drawable.a17,R.drawable.a18,R.drawable.a19,R.drawable.a20,
                R.drawable.a21,R.drawable.a22,R.drawable.a23,R.drawable.a24,
                R.drawable.a25,R.drawable.a26,R.drawable.a27,R.drawable.a28,
                R.drawable.a29,R.drawable.a30,R.drawable.a31,R.drawable.a32,
                R.drawable.a33,R.drawable.a34,R.drawable.a35,R.drawable.a36,
                R.drawable.a37,R.drawable.a38,R.drawable.a39,R.drawable.a40,
                R.drawable.a41,R.drawable.a42,R.drawable.a43,R.drawable.a44,
                R.drawable.a45,R.drawable.a46,R.drawable.a47,R.drawable.a48,
                R.drawable.a49,R.drawable.a50};

        //gia su co 10 o
        int motNuaSoLuong =  soluong/2;//5
        //0 1 2 3 4 5
        int motNuaMang[] = new int[motNuaSoLuong] ;

        ArrayList<Integer>  mangNgauNhien = new ArrayList<>();
        for(int i=0;i<motNuaSoLuong;i++){//i 0->5
            //tao so ngau nhien
            Random random = new Random();
            int number = random.nextInt(mangToanBoAnh.length);//(0->4)
            //chac chan khong trung anh
            while(kiemTraDaCoTrongMang(mangNgauNhien,number)){
                number = random.nextInt(mangToanBoAnh.length);
            }
            mangNgauNhien.add(number);
            //cho vao mang
            motNuaMang[i]=mangToanBoAnh[number];
        }
        //mang gap doi
        // int mangHoanThien[] = new int[soluong];
        for(int j =0;j<soluong;j++){//j 0->10
            //0->5
            if(j<motNuaSoLuong){
                kq.add(new hinhanh(motNuaMang[j]));
                //mangHoanThien[j]= motNuaMang[j];
            }else{//6- >10
                kq.add(new hinhanh(motNuaMang[j-motNuaSoLuong]));
                //mangHoanThien[j] = motNuaMang[j-motNuaSoLuong];
            }
        }
        //trom mang
        Collections.shuffle(kq);
        //tra ve
        return kq;
    }
}

