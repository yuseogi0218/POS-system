package com.yuseogi.batchserver.component;

import java.util.List;

public interface SchedulerJdbcTemplateRepository {

    List<Long> findAllStoreId();
}
