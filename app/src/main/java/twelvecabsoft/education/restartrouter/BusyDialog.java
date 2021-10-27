// Copyright 2020 Igor Tolmachev, ITSamples.com. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"); /
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ==============================================================================

package twelvecabsoft.education.restartrouter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

class BusyDialog extends Dialog
{
    BusyDialog(Context context) {

        super(context);
    }

    private Handler mAnimateHandler;
    private ImageView mImageViewBusy;

    private int mCounter;
    private boolean mContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_busy);

        Window window = getWindow();
        if(window != null)
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mCounter = 0;
        mContinue = true;

        mImageViewBusy = (ImageView)findViewById(R.id.imageViewBusy);
        mAnimateHandler = new Handler();

        mAnimateHandler.removeCallbacks(mUpdateAnimation);
        mAnimateHandler.postDelayed(mUpdateAnimation, 100);
    }

    private Runnable mUpdateAnimation = new Runnable() {

        public void run() {
            switch(mCounter) {
                case 1:
                    mImageViewBusy.setImageResource(R.drawable.reload1);
                    break;
                case 2:
                    mImageViewBusy.setImageResource(R.drawable.reload2);
                    break;
                case 3:
                    mImageViewBusy.setImageResource(R.drawable.reload3);
                    break;
                case 4:
                    mImageViewBusy.setImageResource(R.drawable.reload4);
                    break;
                case 5:
                    mImageViewBusy.setImageResource(R.drawable.reload5);
                    break;
                case 6:
                    mImageViewBusy.setImageResource(R.drawable.reload6);
                    break;
                case 7:
                    mImageViewBusy.setImageResource(R.drawable.reload7);
                    break;
                case 8:
                    mImageViewBusy.setImageResource(R.drawable.reload8);
                    break;
                case 9:
                    mImageViewBusy.setImageResource(R.drawable.reload9);
                    break;
            }

            mCounter++;
            if(mCounter > 9)
                mCounter = 1;

            if(mContinue)
                mAnimateHandler.postDelayed(mUpdateAnimation, 70);
        }
    };
}
