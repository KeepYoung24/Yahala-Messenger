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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

public class ClipPathImplCircleImageView extends CircleImageView {

    public ClipPathImplCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void cropBitmap(Canvas nativeCanvas, Bitmap contentBitmap, int contentAreaSize, Paint paint) {
        contentAreaSize -= mMiddleCircleIndent * 2;
        Bitmap cropBitmap = Bitmap.createBitmap(contentAreaSize, contentAreaSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cropBitmap);

        Path path = new Path();
        float radius = contentAreaSize / 2;
        path.addCircle(radius, radius, radius, Path.Direction.CW);
        canvas.clipPath(path);

        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);

        canvas.drawBitmap(contentBitmap, 0, 0, paint);

        int pos = mBottomCircleIndent + mMiddleCircleIndent;
        nativeCanvas.drawBitmap(cropBitmap, pos, pos, paint);
    }

}
