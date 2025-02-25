package com.example.ldap_demo.config;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.OperationType;
import com.unboundid.ldap.sdk.schema.AttributeTypeDefinition;
import com.unboundid.ldap.sdk.schema.ObjectClassDefinition;
import com.unboundid.ldap.sdk.schema.Schema;
import com.unboundid.ldif.LDIFReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.EnumSet;

import static com.unboundid.ldap.sdk.schema.AttributeTypeDefinition.*;

@Configuration
@Profile("test")
public class EmbeddedLdapConfig {

    private InMemoryDirectoryServer directoryServer;

    @Bean
    public InMemoryDirectoryServer inMemoryDirectoryServer() throws LDAPException {
        // Load the schema from the schema.ldif file
        Schema schema;
        try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("ldap-schema.ldif")) {
//        try (InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("schema.ldif")) {
            if (schemaStream == null) {
                throw new RuntimeException("schema.ldif not found in the classpath");
            }
            System.out.println("schema.ldif loaded successfully");
            schema = Schema.getSchema(schemaStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load schema from schema.ldif", e);
        }

        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=example,dc=com");
        config.setListenerConfigs(InMemoryListenerConfig.createLDAPConfig("default", 10389));
        config.setSchema(null); // Set the schema

        // Reject anonymous access
        config.setAuthenticationRequiredOperationTypes(EnumSet.allOf(OperationType.class));

        // Enable SASL authentication
        config.addAdditionalBindCredentials("cn=John Doe,ou=Users,dc=example,dc=com", "password");
        config.addAdditionalBindCredentials("cn=Jane Smith,ou=Users,dc=example,dc=com", "password");
        directoryServer = new InMemoryDirectoryServer(config);
        directoryServer.importFromLDIF(true, new LDIFReader(getClass().getClassLoader().getResourceAsStream("sample.ldif")));
        //directoryServer.importFromLDIF(true, new LDIFReader(getClass().getClassLoader().getResourceAsStream("sample-group.ldif")));
        directoryServer.startListening();
        return directoryServer;
    }

    @PreDestroy
    public void shutdown() {
        if (directoryServer != null) {
            directoryServer.shutDown(true);
        }
    }
}