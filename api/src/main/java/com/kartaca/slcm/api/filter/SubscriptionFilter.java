package com.kartaca.slcm.api.filter;

enum SortBySubscription { 
    UPDATEDAT{
      public String toString() {
          return "updatedAt";
      }},
    CREATEDAT{
      public String toString() {
          return "createdAt";
      }}
}
public class SubscriptionFilter{
    private SubscriptionQueries query;
    private SortBySubscription sortby;
    private int limit;
    private int offset;
    public String getSortby() {
        return sortby.toString();
    }

    public void setSortby(SortBySubscription sortby) {
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
    
    public SubscriptionQueries getQuery(){
        return query;
    }

    public void setQuery(SubscriptionQueries query){
        this.query=query;
    }
    
    
}
