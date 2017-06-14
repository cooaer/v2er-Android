package me.ghui.v2er.injector.module;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import dagger.Module;
import dagger.Provides;
import me.ghui.v2er.R;
import me.ghui.v2er.adapter.base.CommonLoadMoreAdapter;
import me.ghui.v2er.adapter.base.ViewHolder;
import me.ghui.v2er.injector.scope.PerFragment;
import me.ghui.v2er.module.drawer.star.TopicStarContract;
import me.ghui.v2er.module.drawer.star.TopicStarFragment;
import me.ghui.v2er.module.drawer.star.TopicStarPresenter;
import me.ghui.v2er.module.node.NodeTopicActivity;
import me.ghui.v2er.module.user.UserHomeActivity;
import me.ghui.v2er.network.Constants;
import me.ghui.v2er.network.bean.TopicStarInfo;
import me.ghui.v2er.widget.LoadMoreRecyclerView;

/**
 * Created by ghui on 17/05/2017.
 */

@Module
public class TopicStarModule {
    private TopicStarFragment mView;

    public TopicStarModule(TopicStarFragment view) {
        mView = view;
    }

    @Provides
    public LoadMoreRecyclerView.Adapter<TopicStarInfo.Item> provideAdapter() {
        return new CommonLoadMoreAdapter<TopicStarInfo.Item>(mView.getContext(), R.layout.common_list_item) {
            @Override
            protected void convert(ViewHolder holder, TopicStarInfo.Item item, int position) {
                Glide.with(mContext)
                        .load(Constants.HTTPS_SCHEME + item.getAvatar())
                        .into((ImageView) holder.getView(R.id.avatar_img));
                holder.setText(R.id.user_name_tv, item.getUserName());
                holder.setText(R.id.time_tv, item.getTime());
                holder.setText(R.id.tagview, item.getTag());
                holder.setText(R.id.title_tv, item.getTitle());
                holder.setText(R.id.comment_num_tv, "评论" + item.getCommentNum());
            }

            @Override
            protected void bindListener(ViewHolder holder, int viewType) {
                super.bindListener(holder, viewType);
                holder.setOnClickListener(v ->
                                UserHomeActivity.open(getItem(holder.index()).getUserName(), mContext),
                        R.id.avatar_img, R.id.user_name_tv);
                holder.setOnClickListener(v ->
                        NodeTopicActivity.open(getItem(holder.index()).getTagLink(), mContext), R.id.tagview);
            }
        };
    }

    @PerFragment
    @Provides
    public TopicStarContract.IPresenter providePresenter() {
        return new TopicStarPresenter(mView);
    }

}