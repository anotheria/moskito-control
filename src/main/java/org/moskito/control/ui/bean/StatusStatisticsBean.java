package org.moskito.control.ui.bean;

/**
 * Represents a single status statistics in the statistics widget.
 *
 * @author strel
 */
public class StatusStatisticsBean {

    /**
     * Name / color of the status.
     */
    private String name;

    /**
     * Number of components with given status.
     */
    private int componentCount;

    /**
     * If true this status filter is actually selected.
     */
    private boolean selected;


    public StatusStatisticsBean(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getComponentCount() {
        return componentCount;
    }

    public void setComponentCount(int componentCount) {
        this.componentCount = componentCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void addComponent() {
        this.componentCount++;
    }

    public void removeComponent() {
        this.componentCount--;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusStatisticsBean)) return false;

        StatusStatisticsBean that = (StatusStatisticsBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
