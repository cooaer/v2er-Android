package me.ghui.v2er.module.topic;

import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.ghui.v2er.network.APIService;
import me.ghui.v2er.network.Constants;
import me.ghui.v2er.network.bean.SimpleInfo;
import me.ghui.v2er.network.bean.TopicInfo;

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

    @Override
    public Observable<SimpleInfo> thxReplier(String replyId, String t) {
        return APIService.get().thxReplier(replyId, t)
                .flatMap(simpleInfo -> APIService.get().thxMoney())
                .compose(mView.rx());
    }

    @Override
    public void thxCreator(String id, String t) {
        APIService.get().thxCreator(id, t)
                .flatMap(simpleInfo -> APIService.get().thxMoney())
                .compose(mView.rx())
                .subscribe(new Consumer<SimpleInfo>() {
                    @Override
                    public void accept(SimpleInfo simpleInfo) throws Exception {
                        mView.afterThxCreator();
                    }
                });
    }


    @Override
    public void starTopic(String topicId, String t) {
        APIService.get().starTopic(referer(topicId), topicId, t)
                .compose(mView.rx())
                .subscribe(new Consumer<TopicInfo>() {
                    @Override
                    public void accept(TopicInfo topicInfo) throws Exception {
                        Logger.d("ttopicInfo: " + topicInfo);
                        mView.afterStarTopic(topicInfo);
                    }
                });
    }

    @Override
    public void unStarTopic(String topicId, String t) {
        APIService.get().unStarTopic(referer(topicId), topicId, t)
                .compose(mView.rx())
                .subscribe(new Consumer<TopicInfo>() {
                    @Override
                    public void accept(TopicInfo topicInfo) throws Exception {
                        mView.afterUnStarTopic(topicInfo);
                    }
                });
    }

    private String referer(String topicId) {
        return Constants.BASE_URL + "/t/" + topicId;
    }


}