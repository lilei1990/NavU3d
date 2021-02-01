package com.huida.navu3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.amap.api.location.CoordinateConverter;
import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Operator;
import com.esri.core.geometry.OperatorFactoryLocal;
import com.esri.core.geometry.OperatorOffset;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;

import java.util.ArrayList;

/**
 * 入线灵敏度测试界面
 * Created by liaozhankun on 2017/9/22.
 */

public class SensitivityTestView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder; // 用于控制SurfaceView
    private Paint p; // 声明一支画笔
    private Canvas mCanvas; // 声明一张画布
    private int r = 20; // 圆的坐标和半径
    private int width = getWidth();
    private int height = getHeight();

    public SensitivityTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void init() {

        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        p = new Paint(); // 创建一个画笔对象
        p.setStrokeWidth(5);
        p.setTextSize(25);
        p.setColor(Color.GREEN); // 设置画笔的颜色为白色
        setFocusable(true); // 设置焦点

    }



    public void doDrawLine( ArrayList<Polyline> mPoints) {
        mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
        mCanvas.drawRGB(0, 0, 0); // 把画布填充为黑色

        Path path = new Path();

        p.setStyle(Paint.Style.STROKE);
        for (Polyline mPoint : mPoints) {
            int pointCount = mPoint.getPointCount();

            for (int i = 0; i < pointCount; i++) {
                Point point = mPoint.getPoint(i);
                float x = (float) point.getX();
                float y = (float) point.getY();
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
        }

        mCanvas.drawPath(path, p);
        mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
//        doDrawLine(x.toInt(), y.toInt());

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }


}

