/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gl2jni;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.util.Hashtable;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

public class GL2JNIActivity extends Activity {

    GL2JNIView mView;

    @Override protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        mView = new GL2JNIView(getApplication());
	setContentView(mView);
    mView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showOfferwall();
        }
    });

        Hashtable<String, Object> connectFlags = new Hashtable<String, Object>();
        connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING, "true"); // Disable this in production builds
        //connectFlags.put(TapjoyConnectFlag.USER_ID, "USER_ID_GOES_HERE"); // Important for self-managed currency

        Tapjoy.connect(getApplicationContext(), "u6SfEbh_TA-WMiGqgQ3W8QECyiQIURFEeKm0zbOggubusy-o5ZfXp33sTXaD", connectFlags, new TJConnectListener() {
            @Override
            public void onConnectSuccess() {
            }

            @Override
            public void onConnectFailure() {
            }
        });
    }

    @Override protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    private TJPlacement offerwallPlacement = null;
    private void showOfferwall() {
        if (offerwallPlacement != null) {
            return;
        }
        offerwallPlacement = Tapjoy.getPlacement("offerwall", new TJPlacementListener() {
            @Override
            public void onRequestSuccess(TJPlacement tjPlacement) {

            }

            @Override
            public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
                offerwallPlacement = null;
            }

            @Override
            public void onContentReady(TJPlacement tjPlacement) {
                tjPlacement.showContent();
            }

            @Override
            public void onContentShow(TJPlacement tjPlacement) {

            }

            @Override
            public void onContentDismiss(TJPlacement tjPlacement) {
                offerwallPlacement = null;
            }

            @Override
            public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

            }

            @Override
            public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {

            }

            @Override
            public void onClick(TJPlacement tjPlacement) {

            }
        });
        offerwallPlacement.requestContent();
    }
}
