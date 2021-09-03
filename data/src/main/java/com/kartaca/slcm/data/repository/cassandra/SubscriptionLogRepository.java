package com.kartaca.slcm.data.repository.cassandra;

import com.kartaca.slcm.data.model.cassandra.SubsPrimeKeys;
import com.kartaca.slcm.data.model.cassandra.SubscriptionLog;
import java.util.Date;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionLogRepository  extends CassandraRepository<SubscriptionLog,SubsPrimeKeys> {
    List<SubscriptionLog> findByPkSubscriptionid(int subscriptionid);
    List<SubscriptionLog> findByPkSubscriptionidAndPkLevel(int id, String level);
    List<SubscriptionLog> findByPkSubscriptionidAndPkDate(int id, Date date);
    List<SubscriptionLog> findByPkSubscriptionidAndPkLevelAndPkDate(int id, String level, Date date);
}
