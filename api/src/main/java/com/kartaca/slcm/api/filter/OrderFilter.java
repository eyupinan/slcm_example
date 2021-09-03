package com.kartaca.slcm.api.filter;

enum SortByOrder { 
    UPDATEDAT{
      public String toString() {
          return "updatedAt";
      }},
    CREATEDAT{
      public String toString() {
          return "createdAt";
      }}
}
public class OrderFilter{
    private OrderQueries query;
    private SortByOrder sortby;
    private int limit;
    private int offset;

    public String getSortby() {
        return sortby.toString();
    }

    public void setSortby(SortByOrder sortby) {
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
    public OrderQueries getQuery(){
        return query;
    }

    public void setQuery(OrderQueries query){
        this.query=query;
    }
    
    
}
