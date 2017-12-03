package com.example.madhuri.accessibilitytest;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;



public class MyAccessibilityService extends AccessibilityService {

    private static final String TAG = "MyAccessibilityService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        debug("On accessibility event");
        if (AccessibilityEvent.eventTypeToString(event.getEventType()).contains("WINDOW")) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            getURL(nodeInfo);
        }
    }

    @Override
    public void onInterrupt() {

    }

    public void getURL(AccessibilityNodeInfo info) {
        //Use the node info tree to identify the proper content.
        //For now we'll just log it to logcat.
//        Log.d(TAG, toStringHierarchy(info, 0));

        if(info == null)
            return;
        if(info.getText() != null && info.getText().length() > 0)
            if(info.getText().toString().equalsIgnoreCase("https://billeasy.in"))
            {
                // Use package name which we want to check
                boolean isAppInstalled = appInstalledOrNot("in.billeasy.billeasy");

                Log.d(TAG, "====> app installed: "+isAppInstalled);

                if(isAppInstalled) {

                    Log.d(TAG, "====> inside installed: ");
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("in.billeasy.billeasy");
                    startActivity(LaunchIntent);

                    Log.i(TAG,"Application is already installed.");
                } else {
                    // Do whatever we want to do if application not installed
                    // For example, Redirect to play store

                    final String appPackageName = "com.whatsapp"; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                    Log.i(TAG,"Application is not currently installed.");
                }

            }


            Log.e(TAG,info.getText() + " class: "+info.getClassName());
        for(int i=0;i<info.getChildCount();i++){
            AccessibilityNodeInfo child = info.getChild(i);
            getURL(child);
//            child.recycle();
        }


    }

    private static void debug(Object object) {
        Log.d(TAG, object.toString());
    }






    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

}
