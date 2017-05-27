package me.ghui.v2ex.module.topic;

import com.orhanobut.logger.Logger;

import me.ghui.v2ex.network.APIService;

/**
 * Created by ghui on 04/05/2017.
 */

public class TopicPresenter implements TopicContract.IPresenter {

    private TopicContract.IView mView;

    public TopicPresenter(TopicContract.IView view) {
        mView = view;
    }

    @Override
    public void start() {
    }

    @Override
    public void loadData(String topicId, int page) {
        // TODO: 25/05/2017 翻页时，过滤掉topic content 
        APIService.get().topicDetails(topicId, page)
                .compose(mView.rx())
                .subscribe(topicInfo -> {
                    Logger.d("topicInfo: " + topicInfo);
                    mView.fillView(topicInfo, page > 1);
                });
    }

    @Override
    public void loadData(String topicId) {
        loadData(topicId, 1);
    }

}
