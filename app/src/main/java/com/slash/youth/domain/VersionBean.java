package com.slash.youth.domain;

/**
 * Created by zss on 2016/12/27.
 */
public class VersionBean {
    public int rescode;
    private DataBean data;
    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
        public static class DataBean {
        private int type;
        private long code;
        private String version;
        private long cts;
        private int forceupdate;
        private String content;
        private String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            this.code = code;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public long getCts() {
            return cts;
        }

        public void setCts(long cts) {
            this.cts = cts;
        }

        public int getForceupdate() {
            return forceupdate;
        }

        public void setForceupdate(int forceupdate) {
            this.forceupdate = forceupdate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
