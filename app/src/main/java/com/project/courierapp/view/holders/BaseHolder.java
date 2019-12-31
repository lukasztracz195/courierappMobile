package com.project.courierapp.view.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.courierapp.R;
import com.project.courierapp.model.converters.LocalDateTimeFormatter;
import com.project.courierapp.model.dtos.response.Response;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    public void setFields(List<Response> dataObject){
        this.dataObject = dataObject;
    }

    protected void setDateField(LocalDateTime localDateTime, int idTextView){
        Optional<LocalDateTime> optionalLocalDateTime = Optional.ofNullable(localDateTime);
        if(optionalLocalDateTime.isPresent()){
            Objects.requireNonNull((TextView) mapTextView.get(idTextView))
                    .setText(String.valueOf(
                            LocalDateTimeFormatter.format(localDateTime)));
        }else{
            Objects.requireNonNull((TextView) mapTextView.get(R.id.start_time_content))
                    .setText(StringUtils.EMPTY);
        }
    }
}
