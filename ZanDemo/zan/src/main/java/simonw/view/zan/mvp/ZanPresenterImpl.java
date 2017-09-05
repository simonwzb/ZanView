package simonw.view.zan.mvp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import simonw.view.zan.R;
import simonw.view.zan.ZanBean;


/**
 * creator simon.wei
 * date 2017/8/22.
 */

public class ZanPresenterImpl implements IZanPresenter {
    protected Context mCtx;
    private IZanView activity;
    private List<Drawable> mDrawbleList = new ArrayList<>();

    public ZanPresenterImpl(IZanView iZanView) {
        if (iZanView instanceof Activity) {
            this.mCtx = ((Activity) iZanView);
        } else if (iZanView instanceof FrameLayout) {
            this.mCtx = ((Fragment) iZanView).getActivity();
        }
        this.activity = iZanView;
    }

    @Override
    public void setDefaultDrawableList() {
        mDrawbleList.add(mCtx.getResources().getDrawable(R.drawable.z1));
        mDrawbleList.add(mCtx.getResources().getDrawable(R.drawable.z2));
        mDrawbleList.add(mCtx.getResources().getDrawable(R.drawable.z3));
        mDrawbleList.add(mCtx.getResources().getDrawable(R.drawable.z4));
        mDrawbleList.add(mCtx.getResources().getDrawable(R.drawable.z5));
    }


    @Override
    public void setDrawbleList(List<Drawable> drawbleList) {
        if (mDrawbleList.size() > 0) {
            mDrawbleList.clear();
        }
        mDrawbleList.addAll(drawbleList);
    }

    @SuppressLint("HandlerLeak")
    private Handler mZanHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleMsg(msg);
        }
    };

    private void handleMsg(Message msg) {
        if (mDrawbleList.size() == 5) {
            ZanBean zanBean = new ZanBean(((BitmapDrawable) mDrawbleList.get(new Random().nextInt(4))).getBitmap(), msg.arg1, msg.arg2);
            activity.onAddZan(zanBean);
        }
    }

    @Override
    public void startTimerZan(final int width, final int height) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (mZanHandler != null) {
                    Message msg = mZanHandler.obtainMessage();
                    msg.arg1 = width;
                    msg.arg2 = height;
                    msg.sendToTarget();
                }
            }
        }, 500, 500);
    }

    @Override
    public void popZan(final int width, final int height) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mZanHandler != null) {
                    Message msg = mZanHandler.obtainMessage();
                    msg.arg1 = width;
                    msg.arg2 = height;
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void start() {
        mZanHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }
        };
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
        if (mZanHandler != null) {
            mZanHandler.removeCallbacksAndMessages(null);
            mZanHandler = null;
        }
    }

    protected int dip2px(float dpValue) {
        final float scale = mCtx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
