package me.ghui.v2er.network.bean;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import me.ghui.v2er.util.AvatarUtils;
import me.ghui.v2er.util.Check;
import me.ghui.v2er.util.DateUtils;

/**
 * Created by ghui on 25/03/2017.
 */

public class DailyHotInfo extends ArrayList<DailyHotInfo.Item> implements IBase, Serializable {

    public DailyHotInfo() {
    }

    public DailyHotInfo(@NonNull Collection<? extends Item> c) {
        super(c);
    }

    private String mResponseBody;

    @Override
    public String getResponse() {
        return mResponseBody;
    }

    @Override
    public void setResponse(String response) {
        mResponseBody = response;
    }

    @Override
    public boolean isValid() {
        if (size() <= 0) return true;
        return Check.notEmpty(get(0).id);
    }

    public static class Item implements Serializable {
        private String id;
        private String title;
        private String url;
        private String content;
        private int replies;
        @SerializedName("created")
        private long time;
        private Member member;
        private Node node;

        @Override
        public String toString() {
            return "Item{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    ", content='" + content + '\'' +
                    ", replies=" + replies +
                    ", time=" + time +
                    ", member=" + member +
                    ", node=" + node +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReplies() {
            return replies;
        }

        public void setReplies(int replies) {
            this.replies = replies;
        }

        private String timeText;

        public String getTime() {
            if (timeText != null && !timeText.isEmpty()) {
                return timeText;
            }
            return DateUtils.parseDate(time);
        }

        public void setTime(long time) {
            this.time = time;
        }

        public void setTime(String time) {
            this.timeText = time;
        }

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }

        public static class Member implements Serializable {
            private String id;
            @SerializedName("username")
            private String userName;
            @SerializedName("avatar_large")
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getAvatar() {
                return AvatarUtils.adjustAvatar(avatar);
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            @Override
            public String toString() {
                return "Member{" +
                        "id='" + id + '\'' +
                        ", userName='" + userName + '\'' +
                        ", avatar='" + avatar + '\'' +
                        '}';
            }
        }

        public static class Node implements Serializable {
            private String id;
            private String name;
            private String title;
            private String url;
            private int topics;
            @SerializedName("avatar_large")
            private String avatar;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getTopics() {
                return topics;
            }

            public void setTopics(int topics) {
                this.topics = topics;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            @Override
            public String toString() {
                return "Node{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        ", topics=" + topics +
                        ", avatar='" + avatar + '\'' +
                        '}';
            }
        }

    }

}
