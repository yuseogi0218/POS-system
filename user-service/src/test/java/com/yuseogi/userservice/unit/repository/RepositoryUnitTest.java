package com.yuseogi.userservice.unit.repository;

import com.yuseogi.userservice.infrastructure.security.config.WebSecurityConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(
    showSql = false,
    excludeAutoConfiguration = {WebSecurityConfig.class}
)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryUnitTest {
}
