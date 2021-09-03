package com.kartaca.slcm.data.repository.cassandra;

import com.kartaca.slcm.data.model.cassandra.OrderLog;
import com.kartaca.slcm.data.model.cassandra.OrderPrimeKeys;
import java.util.Date;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLogRepository  extends CassandraRepository<OrderLog,OrderPrimeKeys> {
    List<OrderLog> findByPkOrderid(int orderid);
    List<OrderLog> findByPkOrderidAndPkLevel(int orderid, String level);
    List<OrderLog> findByPkOrderidAndPkDate(int orderid, Date date);
    List<OrderLog> findByPkOrderidAndPkLevelAndPkDate(int orderid, String level, Date date);
}
