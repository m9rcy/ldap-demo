package com.example.ldap_demo.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LdapServiceTest {

    @Mock
    private LdapTemplate ldapTemplate;

    @InjectMocks
    private LdapService ldapService;

    @Test
    void getEmailAddress_shouldReturnEmail_whenUserExistsAndHasEmail() throws Exception {
        // Arrange
        String username = "testuser";
        String expectedEmail = "testuser@example.com";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenReturn(List.of(expectedEmail));

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }

    @Test
    void getEmailAddress_shouldReturnNull_whenUserExistsButNoEmail() throws Exception {
        // Arrange
        String username = "testuser";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenReturn(List.of());

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertNull(actualEmail);
    }

    @Test
    void getEmailAddress_shouldReturnNull_whenUserDoesNotExist() {
        // Arrange
        String username = "nonexistent";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenReturn(List.of());

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertNull(actualEmail);
    }

    @Test
    void getEmailAddress_shouldReturnNull_whenGenericExceptionOccurs() {
        // Arrange
        String username = "testuser";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenThrow(new RuntimeException("LDAP Error"));

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertNull(actualEmail);
    }

    @Test
    void getEmailAddress_shouldReturnNull_whenConnectionTimeoutOccurs() {
        // Arrange
        String username = "testuser";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenThrow(new RuntimeException("LDAP Connection timed out"));

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertNull(actualEmail);
    }

    @Test
    void getEmailAddress_shouldReturnFirstEmail_whenMultipleEmailsExist() throws Exception {
        // Arrange
        String username = "testuser";
        String expectedEmail = "primary@example.com";
        String secondaryEmail = "secondary@example.com";

        when(ldapTemplate.search(
                eq(""),
                eq(new EqualsFilter("sAMAccountName", username).encode()),
                any(SearchControls.class),
                any(AttributesMapper.class)))
                .thenReturn(List.of(expectedEmail, secondaryEmail));

        // Act
        String actualEmail = ldapService.getEmailAddress(username);

        // Assert
        assertEquals(expectedEmail, actualEmail);
    }
}