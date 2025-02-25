package com.example.ldap_demo;

import com.example.ldap_demo.service.LdapSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class LdapSearchServiceTest {

    @Autowired
    private LdapSearchService ldapSearchService;

    @Test
    public void testSearchBySamAccountNameUsingSpringLdap() {
        List<String> results = ldapSearchService.searchBySamAccountNameUsingSpringLdap("johndoe");
        assertEquals(1, results.size(), "Search should return exactly one result for 'johndoe'");
        assertEquals("johndoe@example.com", results.get(0), "Email should match for 'johndoe'");
        System.out.println("Spring LDAP Search Results: " + results);
    }

    @Test
    public void testSearchBySamAccountNameUsingUnboundId() throws Exception {
        List<String> results = ldapSearchService.searchBySamAccountNameUsingUnboundId("janesmith");
        assertEquals(1, results.size(), "Search should return exactly one result for 'janesmith'");
        assertEquals("janesmith@example.com", results.get(0), "Email should match for 'janesmith'");
        System.out.println("UnboundID Search Results: " + results);
    }
}