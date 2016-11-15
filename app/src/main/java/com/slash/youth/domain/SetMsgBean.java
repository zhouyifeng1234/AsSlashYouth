package com.slash.youth.domain;

/**
 * Created by zss on 2016/11/11.
 */
public class SetMsgBean {

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
        private DataBean1 data;

       public DataBean1 getData() {
            return data;
        }

        public void setData(DataBean1 data) {
            this.data = data;
        }

        public static class DataBean1 {
            private long cts;
            private int dnd;
            private int id;
            private int uid;
            private long uts;

            public long getCts() {
                return cts;
            }

            public void setCts(long cts) {
                this.cts = cts;
            }

            public int getDnd() {
                return dnd;
            }

            public void setDnd(int dnd) {
                this.dnd = dnd;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public long getUts() {
                return uts;
            }

            public void setUts(long uts) {
                this.uts = uts;
            }
        }
    }
}
