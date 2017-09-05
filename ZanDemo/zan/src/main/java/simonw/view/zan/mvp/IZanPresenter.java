package simonw.view.zan.mvp;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * creator simon.wei
 * date 2017/8/22.
 */

public interface IZanPresenter extends IPresenter {
    void setDefaultDrawableList();

    void setDrawbleList(List<Drawable> drawbleList);

    void startTimerZan(int width, int height);

    void popZan(int width, int height);
}
