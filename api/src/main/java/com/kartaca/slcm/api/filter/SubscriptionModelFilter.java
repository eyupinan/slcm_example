package com.kartaca.slcm.api.filter;

enum SortByModel {
    MODELID{
      public String toString() {
          return "modelid";
      }}
}
public class SubscriptionModelFilter{
    private SubscriptionModelQueries query;
    private SortByModel sortby;
    private int limit;
    private int offset;

    public String getSortby() {
        return sortby.toString();
    }

    public void setSortby(SortByModel sortby) {
        this.sortby = sortby;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    public SubscriptionModelQueries getQuery(){
        return query;
    }

    public void setQuery(SubscriptionModelQueries query){
        this.query=query;
    }
    
    
}
