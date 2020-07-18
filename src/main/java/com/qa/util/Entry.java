package com.qa.util;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * Creaed by fj on 2018/10/12
 */
@XmlRootElement(name="Entry")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Entry.class})
public class Entry implements Serializable {

    private String key;

    private Integer value;
    @XmlTransient
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    @XmlTransient
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
