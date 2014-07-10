package net.ripe.db.whois.internal.api.rnd.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Joiner;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@XmlRootElement
@JsonInclude(NON_EMPTY)
@XmlAccessorType(XmlAccessType.FIELD)
public class NeoResult {

    @XmlElement(name = "columns")
    private List<String> columns;

    @XmlElement(name = "data")
    private List<NeoData[]> data;


    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<NeoData[]> getData() {
        return data;
    }

    public void setData(List<NeoData[]> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final Joiner on = Joiner.on(',');
        return "NeoResult{" +
                "columns=" + on.join(columns) +
                ", data=" + on.join(data) +
                '}';
    }
}
