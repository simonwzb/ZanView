package simonw.view.zandemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import simonw.view.zan.ZanBean;
import simonw.view.zan.ZanView;
import simonw.view.zan.mvp.IZanPresenter;
import simonw.view.zan.mvp.IZanView;
import simonw.view.zan.mvp.ZanPresenterImpl;
import simonw.view.zandemo.util.DensityUtils;

public class DemoActivity extends AppCompatActivity implements IZanView, View.OnClickListener {
    public RelativeLayout layout_main;
    public Button btn_brgin;
    public Button btn_end;
    public Button btn_add;
    public Button btn_close;
    public ZanView zanView;
    public TextView tv_close;

    private int mWidth;
    private int mHeight;
    private boolean mIsFirst = true;
    private float mLastX;
    private float mLastY;

    private IZanPresenter mZanPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);

        btn_brgin = findViewById(R.id.btn_brgin);
        btn_end = findViewById(R.id.btn_end);
        btn_add = findViewById(R.id.btn_add);
        btn_close = findViewById(R.id.btn_close);
        zanView = findViewById(R.id.v_zan);
        tv_close = findViewById(R.id.tv_close);
        btn_brgin.setOnClickListener(this);
        btn_end.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_close.setOnClickListener(this);
        layout_main = findViewById(R.id.layout_main);
        layout_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastX = ev.getRawX();
                        mLastY = ev.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE: {
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        float mX = ev.getRawX();
                        float mY = ev.getRawY();
                        float disX = Math.abs(mX - mLastX);
                        float disY = Math.abs(mY - mLastY);
                        if (disX > 100 && disX > disY) {
                            if (mX - mLastX > 0) {
                                //向右滑动
                                layout_main.setVisibility(View.GONE);
                                mZanPresenter.destroy();
                            } else {
                                //向左滑动
                                layout_main.setVisibility(View.VISIBLE);
                                mZanPresenter.start();
                            }
                        }
                    }
                    break;
                }
                return true;
            }
        });

        //1. 初始化presenter和view
        mZanPresenter = new ZanPresenterImpl(this);
        //2. 设置图片集合，可自定义
        mZanPresenter.setDefaultDrawableList();
        //3. 得到控件的宽高，以备以后使用
        mWidth = (int) DensityUtils.dp2px(this, 120f);
        mHeight = (int) DensityUtils.dp2px(this, 180f);
    }

    @Override
    public void onAddZan(ZanBean zan) {
        zanView.addZanXin(zan);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注意，如果没有结束页执行这段代码，onDestroy应执行一次
        /*mZanPresenter.destroy();
        if (zanView != null) {
            zanView.stop();
            zanView = null;
        }*/
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_brgin) {
            if (mIsFirst) {
                //启动定时器模式
                zanView.start();
                mZanPresenter.startTimerZan(mWidth, mHeight);
                mIsFirst = false;
            } else {
                mZanPresenter.start();
            }
        } else if (id == R.id.btn_end) {
            //停止
            mZanPresenter.destroy();
        } else if (id == R.id.btn_add) {
            //增加一个飘赞
            mZanPresenter.popZan(mWidth, mHeight);
        } else if (id == R.id.btn_close) {
            //直播结束
            tv_close.setVisibility(View.VISIBLE);
            //注意这里，否则飘赞会覆盖结束页
            mZanPresenter.destroy();
            if (zanView != null) {
                zanView.stop();
                zanView = null;
            }
        }
    }
}
