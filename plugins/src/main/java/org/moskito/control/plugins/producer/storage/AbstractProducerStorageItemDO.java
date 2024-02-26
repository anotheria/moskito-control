package org.moskito.control.plugins.producer.storage;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractProducerStorageItemDO {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "producer_id")
    private String producerId;

    @Column(name = "tt")
    private String tt;

    @Column(name = "last")
    private String last;

    @Column(name = "min")
    private String min;

    @Column(name = "max")
    private String max;

    @Column(name = "avg")
    private String avg;

    @Column(name = "err")
    private String err;

    @Column(name = "mcr")
    private String mcr;

    @Column(name = "tr")
    private String tr;

    @Column(name = "cr")
    private String cr;

    @Column(name = "timestamp")
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMcr() {
        return mcr;
    }

    public void setMcr(String mcr) {
        this.mcr = mcr;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
