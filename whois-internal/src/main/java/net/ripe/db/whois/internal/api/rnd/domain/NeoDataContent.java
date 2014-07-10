package net.ripe.db.whois.internal.api.rnd.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(NON_EMPTY)
public class NeoDataContent {
    @XmlElement
    private String type;

    @XmlElement
    private String key;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
