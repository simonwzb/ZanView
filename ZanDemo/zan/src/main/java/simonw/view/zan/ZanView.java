package simonw.view.zan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class ZanView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;

    /**
     * 心的个数
     */
    private ArrayList<ZanBean> zanBeen = new ArrayList<>();
    private Paint p;
    /**
     * 负责绘制的工作线程
     */
    private DrawThread drawThread;

    public ZanView(Context context) {
        this(context, null);
        //setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public ZanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    public ZanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setZOrderMediaOverlay(true);
        this.setZOrderOnTop(true);
        /**设置画布  背景透明*/
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        p = new Paint();
        //抗锯齿
        p.setAntiAlias(true);
        drawThread = new DrawThread();
    }

    /**
     * 点赞动作  添加心的函数 控制画面最大心的个数
     */
    public void addZanXin(ZanBean zanBean) {
        zanBeen.add(zanBean);
        if (zanBeen.size() > 40) {
            zanBeen.remove(0);
        }
        start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (drawThread != null) {
            drawThread.interrupt();
            drawThread = null;
        }
        drawThread = new DrawThread();
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (drawThread != null) {
            drawThread.isRun = false;
            drawThread = null;
            new ClearThread().start();
        }
    }

    class ClearThread extends Thread {
        boolean isNeedClear = true;

        @Override
        public void run() {
            super.run();
            if (isNeedClear) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        if (surfaceHolder != null) {
                            canvas = surfaceHolder.lockCanvas();
                            if (canvas != null) {
                                /**清除画面*/
                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                isNeedClear = false;
            }
        }
    }

    class DrawThread extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            super.run();
            /**绘制的线程 死循环 不断的跑动*/
            while (isRun) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        if (surfaceHolder != null) {
                            canvas = surfaceHolder.lockCanvas();
                            if (canvas != null) {
                                /**清除画面*/
                                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                                boolean isEnd = true;

                                /**对所有心进行遍历绘制*/
                                for (int i = 0; i < zanBeen.size(); i++) {
                                    isEnd = zanBeen.get(i).isEnd;
                                    zanBeen.get(i).draw(canvas, p);
                                }
                                /**这里做一个性能优化的动作，由于线程是死循环的 在没有心需要的绘制的时候会结束线程*/
                                if (isEnd) {
                                    isRun = false;
                                    drawThread = null;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null && isAlive()) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    /**用于控制绘制帧率*/
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        if (drawThread != null) {

//            for (int i = 0; i < zanBeen.size(); i++) {
//                zanBeen.get(i).pause();
//            }
            for (int i = 0; i < zanBeen.size(); i++) {
                zanBeen.get(i).stop();
            }

            drawThread.isRun = false;
            drawThread = null;
            new ClearThread().start();
        }

    }

    public void start() {
        if (drawThread == null) {
//            for (int i = 0; i < zanBeen.size(); i++) {
//                zanBeen.get(i).resume();
//            }
            drawThread = new DrawThread();
            drawThread.start();
        }
    }
}