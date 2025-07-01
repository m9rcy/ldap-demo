package com.example.ldap_demo.service;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;
import java.util.Objects;

@Service
public class LdapService {

    private final LdapTemplate ldapTemplate;

    public LdapService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public String getEmailAddress(String username) {
        try {
            EqualsFilter filter = new EqualsFilter("sAMAccountName", username);

            List<String> emailAddresses = ldapTemplate.search("", filter.encode(),
                    new SearchControls(SearchControls.SUBTREE_SCOPE, 1, 0, new String[]{"mail"}, false, false),
                    (Attributes attrs) -> {
                        Attribute mailAttr = attrs.get("mail");
                        if (mailAttr != null) {
                            return (String) mailAttr.get();
                        }
                        return null;
                    }
            ).stream().filter(Objects::nonNull).toList();
            return emailAddresses.isEmpty() ? null : emailAddresses.get(0);
        } catch (Exception e) {
            return null;
        }

    }
}
