package com.yuseogi.pos.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(showSql = true)
@ActiveProfiles("test")
public class RepositoryUnitTest {
}
