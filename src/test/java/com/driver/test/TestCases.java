package com.driver.test;


import com.driver.model.Admin;
import com.driver.services.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;



@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class TestCases {

    private final AdminServiceImpl adminService;

    @Autowired
    public TestCases(AdminServiceImpl adminService) {
        this.adminService = adminService;
    }

    @Test
    public void testRegister() {
        // Call the register method with some dummy values
        Admin admin = adminService.register("admin23232", "admin123");

        // Assert that the returned admin is not null
        assertNotNull(admin);
    }
}



