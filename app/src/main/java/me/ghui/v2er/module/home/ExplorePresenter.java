package me.ghui.v2er.module.home;

import java.util.List;
import java.util.stream.Collectors;

import me.ghui.v2er.network.APIService;
import me.ghui.v2er.network.GeneralConsumer;
import me.ghui.v2er.network.bean.DailyHotInfo;
import me.ghui.v2er.network.bean.ExplorePageInfo;
import me.ghui.v2er.network.bean.NewsInfo;
import me.ghui.v2er.network.bean.NodesNavInfo;

/**
 * Created by ghui on 22/05/2017.
 */

public class ExplorePresenter implements ExploreContract.IPresenter {

    private ExploreContract.IView mView;
    private ExplorePageInfo pageInfo;

    public ExplorePresenter(ExploreContract.IView view) {
        mView = view;
        pageInfo = new ExplorePageInfo();
    }

    /**
     * 请求今日热议信息
     */
    private void requestDailyHotInfo() {
        APIService.get()
                .homeNews("hot")
                .compose(mView.<NewsInfo>rx())
                .subscribe(new GeneralConsumer<NewsInfo>(mView) {
                    @Override
                    public void onConsume(NewsInfo newsInfo) {
                        List<DailyHotInfo.Item> hotItems = newsInfo.getItems()
                                .stream()
                                .map(item -> convertNewsToDailyHot(item))
                                .collect(Collectors.toList());

                        DailyHotInfo dailyHotInfo = new DailyHotInfo(hotItems);
                        if (dailyHotInfo.isValid()) {
                            pageInfo.dailyHotInfo = dailyHotInfo;
                            mView.fillView(pageInfo);
                        }
                    }
                });
    }

    /**
     * 请求节点信息
     */
    private void requestNodesNavInfo() {
        APIService.get().nodesNavInfo()
                .compose(mView.rx())
                .subscribe(new GeneralConsumer<NodesNavInfo>(mView) {
                    @Override
                    public void onConsume(NodesNavInfo items) {
                        if (items.isValid()) {
                            pageInfo.nodesNavInfo = items;
                            mView.fillView(pageInfo);
                        }
                    }
                });
    }

    @Override
    public void start() {
        requestDailyHotInfo();
        requestNodesNavInfo();
    }

    private DailyHotInfo.Item convertNewsToDailyHot(NewsInfo.Item item) {
        DailyHotInfo.Item.Member member = new DailyHotInfo.Item.Member();
        member.setUserName(item.getUserName());
        member.setAvatar(item.getAvatar());

        DailyHotInfo.Item.Node node = new DailyHotInfo.Item.Node();
        node.setTitle(item.getTagName());
        node.setUrl(item.getTagLink());

        DailyHotInfo.Item newItem = new DailyHotInfo.Item();
        newItem.setId(item.getId());
        newItem.setTitle(item.getTitle());
        newItem.setTime(item.getTime());
        newItem.setReplies(item.getReplies());
        newItem.setUrl(item.getLinkPath());
        newItem.setMember(member);
        newItem.setNode(node);
        return newItem;
    }

}
