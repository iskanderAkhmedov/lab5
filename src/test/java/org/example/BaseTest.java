package org.example;

import org.example.service.corporatesettlement.AccountService;
import org.example.service.corporatesettlement.InstanceService;
import org.example.storage.repository.corporatesettlement.AccountPoolRepository;
import org.example.storage.repository.corporatesettlement.AccountRepository;
import org.example.storage.repository.corporatesettlement.AgreementRepository;
import org.example.storage.repository.corporatesettlement.TppProductRegisterRepository;
import org.example.storage.repository.corporatesettlement.TppProductRepository;
import org.example.storage.repository.corporatesettlement.TppRefAccountTypeRepository;
import org.example.storage.repository.corporatesettlement.TppRefProductClassRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
public abstract class BaseTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected TppProductRegisterRepository tppProductRegisterRepository;
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccountPoolRepository accountPoolRepository;
    @Autowired
    protected TppProductRepository tppProductRepository;
    @Autowired
    protected InstanceService instanceService;
    @Autowired
    protected TppRefProductClassRepository tppRefProductClassRepository;
    @Autowired
    protected AgreementRepository agreementRepository;
    @Autowired
    protected TppRefAccountTypeRepository tppRefAccountTypeRepository;

    private static final String REMOVE_DATA_SQL;
    private static final String EXAMPLE_DATA_SQL;

    static {
        var fileRemove = new File("src/test/resources/sql/remove_data.sql");
        byte[] binaryDataRemove;
        try {
            binaryDataRemove = FileCopyUtils.copyToByteArray(fileRemove);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from file: " + "src/test/resources/sql/remove_date.sql", e);
        }
        REMOVE_DATA_SQL = new String(binaryDataRemove, StandardCharsets.UTF_8);

        var fileCreate = new File("src/test/resources/sql/example_data.sql");
        byte[] binaryDataCreate;
        try {
            binaryDataCreate = FileCopyUtils.copyToByteArray(fileCreate);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from file: " + "src/test/resources/sql/example_data.sql", e);
        }
        EXAMPLE_DATA_SQL = new String(binaryDataCreate, StandardCharsets.UTF_8);
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(EXAMPLE_DATA_SQL);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute(REMOVE_DATA_SQL);
    }
}
