package net.ripe.db.whois.internal.api.rnd.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@XmlRootElement
@JsonInclude(NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeoData {
    @XmlElement
    private NeoDataContent data;

    @XmlElement
    private String self;

    public NeoDataContent getData() {
        return data;
    }

    public void setData(final NeoDataContent data) {
        this.data = data;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(final String self) {
        this.self = self;
    }

    @Override
    public String toString() {
        final String oper = data == null ? "#" : data.toString();
        return "NeoData{" +
                "data=" + oper +
                '}';
    }
}
