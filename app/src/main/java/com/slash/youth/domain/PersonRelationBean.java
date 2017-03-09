package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/21.
 */
public class PersonRelationBean {
    private DataBean data;
    private int rescode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public static class DataBean {
        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            private int addMeFriendCount;
            private int friendCount;
            private int myAddFriendCount;
            private int myFansCount;
            private int myFollowCount;

            public int getAddMeFriendCount() {
                return addMeFriendCount;
            }

            public void setAddMeFriendCount(int addMeFriendCount) {
                this.addMeFriendCount = addMeFriendCount;
            }

            public int getFriendCount() {
                return friendCount;
            }

            public void setFriendCount(int friendCount) {
                this.friendCount = friendCount;
            }

            public int getMyAddFriendCount() {
                return myAddFriendCount;
            }

            public void setMyAddFriendCount(int myAddFriendCount) {
                this.myAddFriendCount = myAddFriendCount;
            }

            public int getMyFansCount() {
                return myFansCount;
            }

            public void setMyFansCount(int myFansCount) {
                this.myFansCount = myFansCount;
            }

            public int getMyFollowCount() {
                return myFollowCount;
            }

            public void setMyFollowCount(int myFollowCount) {
                this.myFollowCount = myFollowCount;
            }
        }
    }
}
