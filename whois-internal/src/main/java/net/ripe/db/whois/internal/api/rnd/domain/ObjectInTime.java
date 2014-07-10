package net.ripe.db.whois.internal.api.rnd.domain;

import net.ripe.db.whois.common.domain.CIString;
import net.ripe.db.whois.common.rpsl.ObjectType;

public class ObjectInTime {

    private final ObjectType type;
    private final CIString pkey;
    private String self;

    public ObjectInTime(final ObjectType type, final CIString pkey) {
        this.type = type;
        this.pkey = pkey;
    }

    public ObjectType getType() {
        return type;
    }

    public CIString getPkey() {
        return pkey;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectInTime that = (ObjectInTime) o;

        if (!pkey.equals(that.pkey)) return false;
        if (self != null ? !self.equals(that.self) : that.self != null) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + pkey.hashCode();
        result = 31 * result + (self != null ? self.hashCode() : 0);
        return result;
    }
}
