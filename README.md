# ZanView
飘赞View，基于SurfaceView，代码MVP风格

* * *
![Alt text](/preview0.gif)
* * *
基本使用：
<pre><code>
        //1. 初始化presenter和view
        zanView = findViewById(R.id.***);
        mZanPresenter = new ZanPresenterImpl(this);
        //2. 设置图片集合，可自定义
        mZanPresenter.setDefaultDrawableList();
        //3. 得到控件的宽高，以备以后使用
        mWidth = (int) DensityUtils.dp2px(this, 120f);
        mHeight = (int) DensityUtils.dp2px(this, 180f);
</code></pre>
<pre><code>
        //启动定时器模式
        zanView.start();
        mZanPresenter.startTimerZan(mWidth, mHeight);
</code></pre>
<pre><code>
        //增加一个飘赞
        mZanPresenter.popZan(mWidth, mHeight);
</code></pre>
<pre><code>
        //停止
        mZanPresenter.destroy();
</code></pre>
<pre><code>
          //直播结束或onDestroy()
          mZanPresenter.destroy();
          if (zanView != null) {
              zanView.stop();
              zanView = null;
          }
</code></pre>
