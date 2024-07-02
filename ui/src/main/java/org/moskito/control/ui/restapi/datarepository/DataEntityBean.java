package org.moskito.control.ui.restapi.datarepository;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a single data entity in the data repository. It can either be calculated (formulas) or retrieved from external source.

 */
public class DataEntityBean {
    /**
     * Name of the entity
     */
    private String name;
    /**
     * Current value.
     */
    private String value;
    /**
     * Formulas that define the calculation or retrieval source of the value.
     */
    private List<String> formulas = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getFormulas() {
        return formulas;
    }

    public void setFormulas(List<String> formulas) {
        this.formulas = formulas;
    }

    public void addFormula(String formula){
        formulas.add(formula);
    }
}
