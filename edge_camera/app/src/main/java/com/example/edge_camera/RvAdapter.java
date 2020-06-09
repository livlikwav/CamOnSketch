package com.example.edge_camera;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {
    private ArrayList<CvModel> data; // 모델화된 데이터들을 리스트로 받아옴
    private Context context;

    RvAdapter(ArrayList<CvModel> data, Context context) { // 생성자
        this.data = data;
        this.context = context;
    }

    @Override
    public RvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Adapter 내부에 정의된 ViewHolder에 정의된 레이아웃을 inflate해서 반환
        return new RvAdapter.ViewHolder(inflater.inflate(R.layout.rv_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RvAdapter.ViewHolder holder, int position) {
        // ViewHolder에 정의된 이미지뷰에 데이터의 이미지 경로의 이미지 출력
        Glide.with(context).load(data.get(position).getImageURI()).override(1024).into(holder.rv_image);
    }

    @Override
    public int getItemCount() { // 아이템의 개수 반환
        return data.size() ;
    }


    // ViewHolder 클래스 정의를 통해 Adapter에서 사용할 뷰들을 연결
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rv_image;

        ViewHolder(View itemView) {
            super(itemView);
            rv_image = itemView.findViewById(R.id.rv_image);
        }
    }
}
