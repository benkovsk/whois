package net.ripe.db.whois.internal.api.rnd.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConversionTest {

    @Test
    public void toJson() throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        final String json = mapper.writeValueAsString(makeNeoResult());

        assertThat(json, is("{\"columns\":[\"inc\",\"out\"],\"data\":[[{\"data\":{\"type\":\"person\",\"key\":\"adm-test\"}}]]}"));
    }

    private NeoResult makeNeoResult() {
        final NeoResult neoResult = new NeoResult();
        neoResult.setColumns(Lists.newArrayList("inc", "out"));

        final NeoDataContent neoDataContent = new NeoDataContent();
        neoDataContent.setType("person");
        neoDataContent.setKey("adm-test");
        final NeoData neoData = new NeoData();
        neoData.setData(neoDataContent);
        final NeoData[] neoDatas = {neoData};

        neoResult.setData(Lists.<NeoData[]>newArrayList(neoDatas));
        return neoResult;
    }


    @Test
    public void fromJson() throws IOException, JAXBException {
        final String inputData = "" +
                "{\n" +
                "  \"columns\" : [ \"inc\", \"out\" ],\n" +
                "  \"data\" : [ [ {\n" +
                "  }, {\n" +
                "    \"labels\" : \"http://localhost:7474/db/data/node/9/labels\",\n" +
                "    \"outgoing_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/out\",\n" +
                "    \"data\" : {\n" +
                "      \"type\" : \"person\",\n" +
                "      \"key\" : \"adm-test\"\n" +
                "    },\n" +
                "    \"traverse\" : \"http://localhost:7474/db/data/node/9/traverse/{returnType}\",\n" +
                "    \"all_typed_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/all/{-list|&|types}\",\n" +
                "    \"property\" : \"http://localhost:7474/db/data/node/9/properties/{key}\",\n" +
                "    \"self\" : \"http://localhost:7474/db/data/node/9\",\n" +
                "    \"properties\" : \"http://localhost:7474/db/data/node/9/properties\",\n" +
                "    \"outgoing_typed_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/out/{-list|&|types}\",\n" +
                "    \"incoming_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/in\",\n" +
                "    \"extensions\" : {\n" +
                "    },\n" +
                "    \"create_relationship\" : \"http://localhost:7474/db/data/node/9/relationships\",\n" +
                "    \"paged_traverse\" : \"http://localhost:7474/db/data/node/9/paged/traverse/{returnType}{?pageSize,leaseTime}\",\n" +
                "    \"all_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/all\",\n" +
                "    \"incoming_typed_relationships\" : \"http://localhost:7474/db/data/node/9/relationships/in/{-list|&|types}\"\n" +
                "  } ] ] }";
        final ByteArrayInputStream stream = new ByteArrayInputStream(inputData.getBytes());

        final MappingJsonFactory factory = new MappingJsonFactory();
        factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        final JsonParser parser = factory.createParser(stream);
        final Iterator<NeoResult> it = parser.readValuesAs(NeoResult.class);

        while (it.hasNext()) {
            final NeoResult next = it.next();
            System.out.println("columns" + next.getColumns().size());
            System.out.println("data" + next.getData().size());

            final NeoData[] moreData = next.getData().get(0);
            System.out.println("more data:" + moreData.length);

            final NeoData neoData = moreData[0];
            System.out.println(neoData); // the first incoming, bound to be empty in this case

            final NeoData neoData2 = moreData[1];
            System.out.println("neodata2:" + neoData2.getData().getKey() + "," + neoData2.getData().getType());

            assertThat(neoData2.getData().getKey(), is("adm-test"));
            assertThat(neoData2.getData().getType(), is("person"));
            assertThat(neoData2.getSelf(), is("http://localhost:7474/db/data/node/9"));
        }
    }
}
