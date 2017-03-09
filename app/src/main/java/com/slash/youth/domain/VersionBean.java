package com.slash.youth.domain;

/**
 * Created by zss on 2016/12/27.
 */
public class VersionBean {

    private VersionDataBean data;
    private int rescode;

    public VersionDataBean getData() {
        return data;
    }

    public void setData(VersionDataBean data) {
        this.data = data;
    }

    public int getRescode() {
        return rescode;
    }

    public void setRescode(int rescode) {
        this.rescode = rescode;
    }

    public static class VersionDataBean {
        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private long code;
            private String content;
            private long cts;
            private int forceupdate;
            private long id;
            private int type;
            private String url;
            private String version;

            public long getCode() {
                return code;
            }

            public void setCode(long code) {
                this.code = code;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }
    }
}
