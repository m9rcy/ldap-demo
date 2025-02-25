package com.example.ldap_demo.service;

import com.unboundid.ldap.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;

@Service
public class LdapSearchService {

    public static final String FILTER_ATTRIBUTE = "sAMAccountName";
    //public static final String FILTER_ATTRIBUTE = "uid";
    @Autowired
    private LdapTemplate ldapTemplate;

    public List<String> searchBySamAccountNameUsingSpringLdap(String samAccountName) {
        LdapQuery query = LdapQueryBuilder.query()
                .where(FILTER_ATTRIBUTE).is(samAccountName);
        return ldapTemplate.search(query, (Attributes attrs) -> {
            return attrs.get("mail").get().toString();
        });
    }

    public List<String> searchBySamAccountNameUsingUnboundId(String samAccountName) throws LDAPException {
        LDAPConnection connection = new LDAPConnection("localhost", 10389);
        String baseDN = "dc=example,dc=com";
        String filter = "(sAMAccountName=" + samAccountName + ")";
        SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, filter, "mail");

        List<String> results = new ArrayList<>();
        SearchResult searchResult = connection.search(searchRequest);
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            results.add(entry.getAttributeValue("mail"));
        }

        connection.close();
        return results;
    }

    public List<String> searchBySamAccountNameUsingSpringLdap1(String samAccountName) {
        LdapQuery query = LdapQueryBuilder.query()
                .where("sAMAccountName").is(samAccountName);
        return ldapTemplate.search(query, (Attributes attrs) -> {
            return attrs.get("mail").get().toString();
        });
    }

    public List<String> searchBySamAccountNameUsingUnboundId1(String samAccountName) throws LDAPException {
        // Configure SASL DIGEST-MD5 bind properties
        DIGESTMD5BindRequestProperties properties = new DIGESTMD5BindRequestProperties(
                "johndoe", // Username
                "password" // Password
        );

        // Create the SASL DIGEST-MD5 bind request
        DIGESTMD5BindRequest digestMD5BindRequest = new DIGESTMD5BindRequest(properties);

        // Connect to the LDAP server
        LDAPConnection connection = new LDAPConnection("localhost", 10389);

        // Perform the SASL bind
        BindResult bindResult = connection.bind(digestMD5BindRequest);
        if (!bindResult.getResultCode().equals(ResultCode.SUCCESS)) {
            throw new LDAPException(bindResult.getResultCode(), "SASL bind failed: " + bindResult.getResultString());
        }

        // Perform the search
        String baseDN = "dc=example,dc=com";
        String filter = "(sAMAccountName=" + samAccountName + ")";
        SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, filter, "mail");

        List<String> results = new ArrayList<>();
        SearchResult searchResult = connection.search(searchRequest);
        for (SearchResultEntry entry : searchResult.getSearchEntries()) {
            results.add(entry.getAttributeValue("mail"));
        }

        connection.close();
        return results;
    }
}