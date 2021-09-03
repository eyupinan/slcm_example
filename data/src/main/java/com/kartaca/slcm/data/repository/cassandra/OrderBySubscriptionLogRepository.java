package com.kartaca.slcm.data.repository.cassandra;

import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionLog;
import com.kartaca.slcm.data.model.cassandra.OrderBySubscriptionPrimeKeys;
import java.util.Date;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBySubscriptionLogRepository  extends CassandraRepository<OrderBySubscriptionLog,OrderBySubscriptionPrimeKeys> {
    List<OrderBySubscriptionLog> findByPkSubscriptionid(int subscriptionid);
    List<OrderBySubscriptionLog> findByPkSubscriptionidAndPkOrderid(int subscriptionid, int orderid);
    List<OrderBySubscriptionLog> findByPkSubscriptionidAndPkOrderidAndPkLevel(int id, int orderid, String level);
    List<OrderBySubscriptionLog> findByPkSubscriptionidAndPkOrderidAndPkDate(int id, int orderid, Date date);
    List<OrderBySubscriptionLog> findByPkSubscriptionidAndPkOrderidAndPkLevelAndPkDate(int id, int orderid, String level, Date date);
}
