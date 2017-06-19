package me.ghui.v2er.module.drawer.star;

import com.orhanobut.logger.Logger;

import me.ghui.v2er.network.APIService;
import me.ghui.v2er.network.GeneralConsumer;
import me.ghui.v2er.network.bean.TopicStarInfo;

/**
 * Created by ghui on 18/05/2017.
 */

public class TopicStarPresenter implements TopicStarContract.IPresenter {

    private TopicStarContract.IView mView;

    public TopicStarPresenter(TopicStarContract.IView view) {
        mView = view;
    }

    @Override
    public void start() {
        loadMore(1);
    }

    @Override
    public void loadMore(int page) {
        APIService.get()
                .topicStarInfo(page)
                .compose(mView.rx())
                .subscribe(new GeneralConsumer<TopicStarInfo>() {
                    @Override
                    public void onConsume(TopicStarInfo topicStarInfo) {
                        mView.fillView(topicStarInfo, page > 1);
                    }
                });
    }
}
