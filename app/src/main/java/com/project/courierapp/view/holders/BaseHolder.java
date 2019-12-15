package com.project.courierapp.view.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.model.dtos.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class BaseHolder extends RecyclerView.ViewHolder implements Holder{

    protected Map<Integer,? super TextView> mapTextView = new HashMap<>();
    protected View itemView;
    protected Object dataObject;

    public BaseHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

     public void initTextViews(List<Integer> ids){
        for(int id : ids){
            TextView textView = itemView.findViewById(id);
            mapTextView.put(id, textView);
        }
    }

    public void setFields(Response dataObject){
        this.dataObject = dataObject;
    }

}
