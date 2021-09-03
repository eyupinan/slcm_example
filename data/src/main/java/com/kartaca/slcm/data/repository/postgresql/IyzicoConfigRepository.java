package com.kartaca.slcm.data.repository.postgresql;

import com.kartaca.slcm.data.model.postgresql.IyzicoModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IyzicoConfigRepository extends AbstractBaseRepository<IyzicoModel, Long> {
}

