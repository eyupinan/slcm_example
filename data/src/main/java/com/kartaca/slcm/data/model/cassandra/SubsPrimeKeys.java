package com.kartaca.slcm.data.model.cassandra;

import com.kartaca.slcm.data.enums.LogLevel;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
@PrimaryKeyClass
public class SubsPrimeKeys implements Serializable {
  @PrimaryKeyColumn(name = "subscriptionid", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private Long subscriptionid;
  @Indexed
  @PrimaryKeyColumn(name = "level", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  private LogLevel level;
  @Indexed
  @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
  private Date date;
    

  public Long getSubscriptionid() {
    return subscriptionid;
  }
  public Date getDate() {
    return date;
  }
  public void setDate(Date d){
      this.date=d;
  }
  public void setSubscriptionid(Long subscriptionid) {
    this.subscriptionid = subscriptionid;
  }

  public LogLevel getLevel() {
    return level;
  }

  public void setLevel(LogLevel level) {
    this.level = level;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (int) (prime * result + ((subscriptionid == 0) ? 0 : subscriptionid));
    result = prime * result + ((level == null) ? 0 : level.hashCode());
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SubsPrimeKeys other = (SubsPrimeKeys) obj;
    if(subscriptionid != other.subscriptionid){
        return false;
    }
    if (level == null) {
      if (other.level != null)
        return false;
    } else if (!level.equals(other.level))
      return false;
    return true;
  }
}
