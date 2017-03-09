package com.slash.youth.domain;

/**
 * Created by zss on 2016/12/3.
 */
public class SkillMamagerOneTempletBean {


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
        private ServiceBean service;

        public ServiceBean getService() {
            return service;
        }

        public void setService(ServiceBean service) {
            this.service = service;
        }

        public static class ServiceBean {
            private int anonymity;
            private int bp;
            private int count;
            private int cts;
            private String desc;
            private int endtime;
            private int id;
            private int instalment;
            private int iscomment;
            private double lat;
            private double lng;
            private int loop;
            private int pattern;
            private String pic;
            private String place;
            private int quote;
            private int quoteunit;
            private String remark;
            private int starttime;
            private String tag;
            private int timetype;
            private String title;
            private int uid;
            private int uts;

            public int getAnonymity() {
                return anonymity;
            }

            public void setAnonymity(int anonymity) {
                this.anonymity = anonymity;
            }

            public int getBp() {
                return bp;
            }

            public void setBp(int bp) {
                this.bp = bp;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public int getCts() {
                return cts;
            }

            public void setCts(int cts) {
                this.cts = cts;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getEndtime() {
                return endtime;
            }

            public void setEndtime(int endtime) {
                this.endtime = endtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getInstalment() {
                return instalment;
            }

            public void setInstalment(int instalment) {
                this.instalment = instalment;
            }

            public int getIscomment() {
                return iscomment;
            }

            public void setIscomment(int iscomment) {
                this.iscomment = iscomment;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public int getLoop() {
                return loop;
            }

            public void setLoop(int loop) {
                this.loop = loop;
            }

            public int getPattern() {
                return pattern;
            }

            public void setPattern(int pattern) {
                this.pattern = pattern;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getPlace() {
                return place;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public int getQuote() {
                return quote;
            }

            public void setQuote(int quote) {
                this.quote = quote;
            }

            public int getQuoteunit() {
                return quoteunit;
            }

            public void setQuoteunit(int quoteunit) {
                this.quoteunit = quoteunit;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getStarttime() {
                return starttime;
            }

            public void setStarttime(int starttime) {
                this.starttime = starttime;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public int getTimetype() {
                return timetype;
            }

            public void setTimetype(int timetype) {
                this.timetype = timetype;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getUts() {
                return uts;
            }

            public void setUts(int uts) {
                this.uts = uts;
            }
        }
    }
}
