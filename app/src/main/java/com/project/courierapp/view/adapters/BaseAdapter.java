package com.project.courierapp.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.model.dtos.response.Response;
import com.project.courierapp.view.holders.BaseHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class BaseAdapter extends RecyclerView.Adapter<BaseHolder> implements Adapter{

    protected List<? extends Response> responses = new ArrayList<>();
    protected Response response;

    protected Context context;

    protected View view;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BaseAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    @Override
    public void downloadData() {

    }

    @Override
    public void updateData(Response response) {
        this.response =response;
    }

    public void updateData(List<? extends Response> responses) {
        this.responses = responses;
        super.notifyDataSetChanged();
    }

}
