package com.project.courierapp.view.toasts;

import android.content.Context;
import android.widget.Toast;

public class ToastFactory {

    public static void createToast(Context context, String message){
        if(context != null) {
            Toast toast = Toast.makeText(context.getApplicationContext(), message,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
