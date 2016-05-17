/**
 * Copyright (C) 2014 Vince Styling
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yahala.ui.Views.CircleImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yahala.messenger.R;

import java.lang.ref.WeakReference;

public class NaturalCircleImageView extends View {
    private int mImgRes;

    private int mMiddleCircleIndent;
    private int mBottomCircleIndent;

    private int mBottomCircleOnColor;
    private int mBottomCircleOffColor;

    private int mMiddleCircleOnColor;
    private int mMiddleCircleOffColor;

    private boolean mIsStatusOn;

    private WeakReference<Bitmap> mOnBitmap;
    private WeakReference<Bitmap> mOffBitmap;

    public NaturalCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);

        mImgRes = array.getResourceId(R.styleable.CircleImageView_src, 0);

        mBottomCircleOnColor = array.getColor(R.styleable.CircleImageView_bottomCircleOnColor, 0);
        mBottomCircleOffColor = array.getColor(R.styleable.CircleImageView_bottomCircleOffColor, 0);

        mMiddleCircleOnColor = array.getColor(R.styleable.CircleImageView_middleCircleOnColor, 0);
        mMiddleCircleOffColor = array.getColor(R.styleable.CircleImageView_middleCircleOffColor, 0);

        mBottomCircleIndent = array.getDimensionPixelOffset(R.styleable.CircleImageView_bottomCircleIndent, 0);
        mMiddleCircleIndent = array.getDimensionPixelOffset(R.styleable.CircleImageView_middleCircleIndent, 0);

        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || mImgRes == 0) {
            super.onDraw(canvas);
            return;
        }

        Bitmap bitmap =
                mIsStatusOn ?
                        mOnBitmap != null ? mOnBitmap.get() : null :
                        mOffBitmap != null ? mOffBitmap.get() : null;

        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(bitmap);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setFilterBitmap(true);
            paint.setAntiAlias(true);
            paint.setDither(true);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;


            int bottomCircleRadius = getWidth() / 2;
            if (mBottomCircleIndent > 0) {
                int oriColor = paint.getColor();
                paint.setColor(mIsStatusOn ? mBottomCircleOnColor : mBottomCircleOffColor);
                tmpCanvas.drawCircle(centerX, centerY, bottomCircleRadius, paint);
                paint.setColor(oriColor);
            }


            int middleCircleRadius = bottomCircleRadius - mBottomCircleIndent;
            if (mMiddleCircleIndent > 0) {
                int oriColor = paint.getColor();
                paint.setColor(mIsStatusOn ? mMiddleCircleOnColor : mMiddleCircleOffColor);
                tmpCanvas.drawCircle(centerX, centerY, middleCircleRadius, paint);
                paint.setColor(oriColor);
            }

            int contentRadius = middleCircleRadius - mMiddleCircleIndent;
            int headWidth = contentRadius * 2;

            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), mImgRes);

            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            float scaleWidth = ((float) headWidth) / width;
            float scaleHeight = ((float) headWidth) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width, height, matrix, false);

            BitmapShader shader = new BitmapShader(originalBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);

            tmpCanvas.drawCircle(centerX, centerX, contentRadius, paint);

            if (mIsStatusOn) {
                mOnBitmap = new WeakReference<Bitmap>(bitmap);
            } else {
                mOffBitmap = new WeakReference<Bitmap>(bitmap);
            }
        }

        if (bitmap != null) canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsStatusOn = true;
                invalidate();
                return true;
            case MotionEvent.ACTION_CANCEL:
                mIsStatusOn = false;
                invalidate();
                return false;
            case MotionEvent.ACTION_UP:
                mIsStatusOn = false;
                performClick();
                invalidate();
                return false;
        }
        return super.onTouchEvent(event);
    }

}
