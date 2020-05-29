package com.training.firebase;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * CREATED BY SANJAIKUMAR On 29-05-2020
 */
public class ToastGenerate {

    private static ToastGenerate ourInstance;
    private final Context context;

    public ToastGenerate (Context context) {
        this.context = context;
    }
    public static ToastGenerate getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ToastGenerate(context);
        return ourInstance;
    }

    //pass message and message type to this method
    public void createToastMessage(String message,int type){

//inflate the custom layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout toastLayout = (LinearLayout) layoutInflater.inflate(R.layout.layout_custome_toast,null);
        TextView toastShowMessage = (TextView) toastLayout.findViewById(R.id.textCustomToastTopic);

        switch (type){
            case 0:
                //if the message type is 0 fail toaster method will call
                createFailToast(toastLayout,toastShowMessage,message);
                break;
            case 1:
                //if the message type is 1 success toaster method will call
                createSuccessToast(toastLayout,toastShowMessage,message);
                break;

            case 2:
                createWarningToast( toastLayout, toastShowMessage, message);
                //if the message type is 2 warning toaster method will call
                break;
            default:
                createFailToast(toastLayout,toastShowMessage,message);

        }
    }

    //Failure toast message method
    private final void createFailToast(LinearLayout toastLayout,TextView toastMessage,String message){
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.button_alert_normal));
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context,toastLayout);
    }

    //warning toast message method
    private final void createWarningToast( LinearLayout toastLayout, TextView toastMessage, String message) {
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.warning_toast));
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context, toastLayout);
    }
    //success toast message method
    private final void createSuccessToast(LinearLayout toastLayout,TextView toastMessage,String message){
        toastLayout.setBackgroundColor(context.getResources().getColor(R.color.success_toast));

        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context,toastLayout);
    }

    private void showToast(Context context, View view){
        Toast toast = new Toast(this.context);
        toast.setGravity(Gravity.END,348,270);

       // toast.setGravity(Gravity.TOP,0,0); // show message in the top of the device
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }






}
