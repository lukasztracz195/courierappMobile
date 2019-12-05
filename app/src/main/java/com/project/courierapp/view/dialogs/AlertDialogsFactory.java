package com.project.courierapp.view.dialogs;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.courierapp.model.constans.Roles;
import com.project.courierapp.model.store.RolesStore;
import com.project.courierapp.view.activities.MainActivity;
import com.project.courierapp.view.fragments.BaseFragmentTags;
import com.project.courierapp.view.fragments.base_layer.LoginFragment;

import java.util.Objects;

public class AlertDialogsFactory {

    public static AlertDialog createLogoutAlertDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        return builder.setTitle("Confirm you log out")
                .setMessage("Are you sure, You want log out?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    ((MainActivity) Objects.requireNonNull(activity)).clearBackStack();
                    ((MainActivity) Objects.
                            requireNonNull(activity)).setBaseForBackStack(new LoginFragment(),
                            BaseFragmentTags.LoginFragment);
                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .create();
    }

    public static AlertDialog createExitAlertDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        return builder.setTitle("Confirm you exit")
                .setMessage("Are you sure, You want exit?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    Objects.requireNonNull(activity).finish();
                    System.exit(0);
                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .create();
    }

    public static AlertDialog createSaveAlertDialog(Activity activity) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        return builder.setTitle("Confirm you cancel")
                .setMessage("Are you sure, You want cancel this operations?")
                .setPositiveButton("Yes", (arg0, arg1) -> {
                    String role = RolesStore.getRole();
                    if(role.equals(Roles.MANAGER)){

                    }else if(role.equals(Roles.WORKER)){

                    }
                })
                .setNegativeButton("No", (dialog, which) -> {

                })
                .create();
    }
}
