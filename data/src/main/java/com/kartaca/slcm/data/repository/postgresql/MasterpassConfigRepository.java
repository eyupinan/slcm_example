package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.model.postgresql.MasterpassModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterpassConfigRepository extends AbstractBaseRepository<MasterpassModel, Long> {
    
}
