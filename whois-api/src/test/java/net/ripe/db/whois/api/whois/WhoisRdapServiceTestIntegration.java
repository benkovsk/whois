package net.ripe.db.whois.api.whois;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import net.ripe.db.whois.api.AbstractRestClientTest;
import net.ripe.db.whois.api.httpserver.Audience;
import net.ripe.db.whois.api.whois.rdap.domain.Domain;
import net.ripe.db.whois.api.whois.rdap.domain.Entity;
import net.ripe.db.whois.common.IntegrationTest;
import net.ripe.db.whois.common.rpsl.RpslObject;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

@Category(IntegrationTest.class)
public class WhoisRdapServiceTestIntegration extends AbstractRestClientTest {
    private static final Logger LOGGER = 
        LoggerFactory.getLogger(WhoisRdapServiceTestIntegration.class);
    private static final Audience AUDIENCE = Audience.RDAP;
    private static final String VERSION_DATE_PATTERN = 
        "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";
    private static final RpslObject PAULETH_PALTHEN = RpslObject.parse("" +
            "person:  Pauleth Palthen\n" +
            "address: Singel 258\n" +
            "phone:   +31-1234567890\n" +
            "e-mail:  noreply@ripe.net\n" +
            "mnt-by:  OWNER-MNT\n" +
            "nic-hdl: PP1-TEST\n" +
            "changed: noreply@ripe.net 20120101\n" +
            "changed: noreply@ripe.net 20120102\n" +
            "changed: noreply@ripe.net 20120103\n" +
            "remarks: remark\n" +
            "source:  TEST\n");
    private static final RpslObject OWNER_MNT = RpslObject.parse("" +
            "mntner:      OWNER-MNT\n" +
            "descr:       Owner Maintainer\n" +
            "admin-c:     TP1-TEST\n" +
            "upd-to:      noreply@ripe.net\n" +
            "auth:        MD5-PW $1$d9fKeTr2$Si7YudNf4rUGmR71n/cqk/ #test\n" +
            "mnt-by:      OWNER-MNT\n" +
            "referral-by: OWNER-MNT\n" +
            "changed:     dbtest@ripe.net 20120101\n" +
            "source:      TEST");
    private static final RpslObject TEST_PERSON = RpslObject.parse("" +
            "person:  Test Person\n" +
            "address: Singel 258\n" +
            "phone:   +31 6 12345678\n" +
            "nic-hdl: TP1-TEST\n" +
            "mnt-by:  OWNER-MNT\n" +
            "changed: dbtest@ripe.net 20120101\n" +
            "source:  TEST\n");
    private static final RpslObject TEST_DOMAIN = RpslObject.parse("" +
            "domain:  31.12.202.in-addr.arpa\n" +
            "descr:   Test domain\n" +
            "admin-c: TP1-TEST\n" +
            "tech-c:  TP1-TEST\n" +
            "zone-c:  TP1-TEST\n" +
            "notify:  notify@test.net.au\n" +
            "nserver: ns1.test.com.au\n" +
            "nserver: ns2.test.com.au\n" +
            "changed: test@test.net.au 20010816\n" +
            "changed: test@test.net.au 20121121\n" +
            "mnt-by:  OWNER-MNT\n" +
            "source:  TEST\n");
    private static final RpslObject TEST_ORG = RpslObject.parse("" +
            "organisation:  ORG-TEST1-TEST\n" +
            "org-name:      Test organisation\n" +
            "org-type:      OTHER\n" +
            "descr:         Drugs and gambling\n" +
            "remarks:       Nice to deal with generally\n" +
            "address:       1 Fake St. Fauxville\n" +
            "phone:         +01-000-000-000\n" +
            "fax-no:        +01-000-000-000\n" +
            "e-mail:        org@test.com\n" +
            "mnt-by:        OWNER-MNT\n" +
            "changed:       test@test.net.au 20121121\n" +
            "source:        TEST\n");

//    private static final RpslObject TEST_DOMAIN = RpslObject.parse("" +
//            "domain:         31.12.202.in-addr.arpa\n" +
//            "descr:          zone for 202.12.31.0/24\n" +
//            "country:        AU\n" +
//            "admin-c:        NO4-AP\n" +
//            "tech-c:         AIC1-AP\n" +
//            "zone-c:         NO4-AP\n" +
//            "nserver:        sec3.apnic.net\n" +
//            "nserver:        cumin.apnic.net\n" +
//            "nserver:        tinnie.apnic.net\n" +
//            "ds-rdata:       38744 5 1 ( 478d5e87d198a6f50808675fbeaa4c5883df2ba4 )\n" +
//            "ds-rdata:       38744 5 2 ( ffd10dc264d800e70143d43cf35eb1d109a059b166ba76d5541972b6b670a2b8 )\n" +
//            "mnt-by:         MAINT-APNIC-IS-AP\n" +
//            "changed:        hm-changed@apnic.net 20120504\n" +
//            "changed:        hm-changed@apnic.net 20120508\n" +
//            "source:         APNIC\n" +
//            "\n" +
//            "role:           APNIC Infrastructure Contact\n" +
//            "address:        6 Cordelia Street\n" +
//            "address:        South Brisbane\n" +
//            "address:        QLD 4101\n" +
//            "country:        AU\n" +
//            "phone:          +61 7 3858 3100\n" +
//            "fax-no:         +61 7 3858 3199\n" +
//            "e-mail:         helpdesk@apnic.net\n" +
//            "admin-c:        DNS3-AP\n" +
//            "tech-c:         NO4-AP\n" +
//            "nic-hdl:        AIC1-AP\n" +
//            "remarks:        Infrastructure Contact for APNICs own-use network blocks\n" +
//            "notify:         dbmon@apnic.net\n" +
//            "mnt-by:         MAINT-APNIC-IS-AP\n" +
//            "changed:        hm-changed@apnic.net 20020211\n" +
//            "changed:        hm-changed@apnic.net 20101217\n" +
//            "changed:        hm-changed@apnic.net 20110704\n" +
//            "source:         APNIC\n" +
//            "\n" +
//            "person:         APNIC Network Operations\n" +
//            "address:        6 Cordelia Street\n" +
//            "address:        South Brisbane\n" +
//            "address:        QLD 4101\n" +
//            "country:        AU\n" +
//            "phone:          +61 7 3858 3100\n" +
//            "fax-no:         +61 7 3858 3199\n" +
//            "e-mail:         netops@apnic.net\n" +
//            "nic-hdl:        NO4-AP\n" +
//            "remarks:        Administrator for APNIC Network Operations\n" +
//            "notify:         netops@apnic.net\n" +
//            "mnt-by:         MAINT-APNIC-AP\n" +
//            "changed:        netops@apnic.net 19981111\n" +
//            "changed:        hostmaster@apnic.net 20020211\n" +
//            "changed:        hm-changed@apnic.net 20081205\n" +
//            "changed:        hm-changed@apnic.net 20101217\n" +
//            "source:         APNIC\n");

    @Before
    public void setup() throws Exception {
        databaseHelper.addObject("person: Test Person\nnic-hdl: TP1-TEST");
        databaseHelper.addObject(OWNER_MNT);
        databaseHelper.updateObject(TEST_PERSON);
        databaseHelper.addObject(TEST_DOMAIN);
        ipTreeUpdater.rebuild();
    }

    @Before
    @Override
    public void setUpClient() throws Exception {
        ClientConfig cc = new DefaultClientConfig();
        cc.getSingletons().add(new JacksonJaxbJsonProvider());
        client = Client.create(cc);
    }

    @Test
    public void lookup_inet6num_with_prefix_length() throws Exception {
        databaseHelper.addObject(
                "inet6num:       2001:2002:2003::/48\n" +
                "netname:        RIPE-NCC\n" +
                "descr:          Private Network\n" +
                "country:        NL\n" +
                "tech-c:         TP1-TEST\n" +
                "status:         ASSIGNED PA\n" +
                "mnt-by:         OWNER-MNT\n" +
                "mnt-lower:      OWNER-MNT\n" +
                "source:         TEST");
        ipTreeUpdater.rebuild();

        createResource(AUDIENCE, "inet6num/2001:2002:2003::/48");
        //final WhoisResources whoisResources = createResource(AUDIENCE, "inet6num/2001:2002:2003::/48").get(WhoisResources.class);
        /*assertThat(whoisResources.getWhoisObjects(), hasSize(2));
        final RpslObject inet6num = WhoisObjectMapper.map(whoisResources.getWhoisObjects().get(0));
        assertThat(inet6num.getKey(), is(ciString("2001:2002:2003::/48")));
        final RpslObject person = WhoisObjectMapper.map(whoisResources.getWhoisObjects().get(1));
        assertThat(person.getKey(), is(ciString("TP1-TEST")));   */

        //Thread.sleep(1500000);
    }

    @Test
    public void lookup_person_object() throws Exception {
        databaseHelper.addObject(PAULETH_PALTHEN);

        ClientResponse response = 
            createResource(AUDIENCE, "entity/PP1-TEST")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        final Entity en = response.getEntity(Entity.class);

        assertThat(en.getHandle(), equalTo("PP1-TEST"));
        assertThat(en.getRdapConformance().get(0), equalTo("rdap_level_0"));
        /* todo: the remaining tests: vcards, remarks, events. As per
         * the next commented section. */

        /*
        "  \"vcardArray\" : [ \"vcard\", [ [ \"version\", {\n" +
        "  }, \"text\", \"4.0\" ], [ \"fn\", {\n" +
        "  }, \"text\", \"Pauleth Palthen\" ], [ \"adr\", {\n" +
        "    \"label\" : \"Singel 258\"\n" +
        "  }, \"text\", [ \"\", \"\", \"\", \"\", \"\", \"\", \"\" ] ], [ \"tel\", {\n" +
        "  }, \"uri\", \"+31-1234567890\" ], [ \"email\", {\n" +
        "  }, \"text\", \"noreply@ripe.net\" ] ] ],\n" +
        "  \"remarks\" : [ {\n" +
        "    \"description\" : [ \"remark\" ]\n" +
        "  } ],\n" +
        "  \"events\" : [ {\n" +
        "    \"eventAction\" : \"last changed\",\n" +
        "    \"eventDate\" : \"2012-01-02T14:00:00Z\",\n" +
        "    \"eventActor\" : \"noreply@ripe.net\"\n" +
        "  } ]\n" +
        "}"
        */
    }

    @Test
    public void lookup_domain_object() throws Exception {
        databaseHelper.addObject(PAULETH_PALTHEN);

        ClientResponse response =
            createResource(AUDIENCE, "domain/31.12.202.in-addr.arpa")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        final Domain dn = response.getEntity(Domain.class); 

        assertThat(dn.getHandle(), equalTo("31.12.202.in-addr.arpa"));
        assertThat(dn.getLdhName(), equalTo("31.12.202.in-addr.arpa"));
        assertThat(dn.getRdapConformance().get(0), equalTo("rdap_level_0"));
        /* todo: nameserver tests, as per the below. */

        /*
        "  \"nameservers\" : [ {\n" +
        "    \"ldhName\" : \"ns1.test.com.au\"\n" +
        "  }, {\n" +
        "    \"ldhName\" : \"ns2.test.com.au\"\n" +
        "  } ]\n" +
        "}"
        */
    }

    @Test
    public void lookup_org_object() throws Exception {
        databaseHelper.addObject(TEST_ORG);

        ClientResponse response = 
            createResource(AUDIENCE, "entity/ORG-TEST1-TEST")
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        assertEquals(200, response.getStatus());
        final Entity en = response.getEntity(Entity.class); 

        assertThat(en.getHandle(), equalTo("ORG-TEST1-TEST"));
        /* todo: additional tests, as per the below. */

        /*
        "{\n" +
        "  \"handle\" : \"ORG-TEST1-TEST\",\n" +
        "  \"vcardArray\" : [ \"vcard\", [ [ \"version\", {\n" +
        "  }, \"text\", \"4.0\" ], [ \"adr\", {\n" +
        "    \"label\" : \"1 Fake St. Fauxville\"\n" +
        "  }, \"text\", [ \"\", \"\", \"\", \"\", \"\", \"\", \"\" ] ], [ \"tel\", {\n" +
        "  }, \"uri\", \"+01-000-000-000\" ], [ \"email\", {\n" +
        "  }, \"text\", \"org@test.com\" ] ] ],\n" +
        "  \"remarks\" : [ {\n" +
        "    \"description\" : [ \"Nice to deal with generally\", \"Drugs and gambling\" ]\n" +
        "  } ],\n" +
        "  \"events\" : [ {\n" +
        "    \"eventAction\" : \"last changed\",\n" +
        "    \"eventDate\" : \"2012-11-20T14:00:00Z\",\n" +
        "    \"eventActor\" : \"test@test.net.au\"\n" +
        "  } ]\n" +
        "}"
        */
    }

    @Override
    protected WebResource createResource(final Audience audience, final String path) {
        return client.resource(String.format("http://localhost:%s/%s", getPort(audience), path));
    }
}