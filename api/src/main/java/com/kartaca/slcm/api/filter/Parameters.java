package com.kartaca.slcm.api.filter;

public class Parameters {
    private Object value;
    private Object operator;
    private Object value2;
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getOperator() {
        return operator;
    }

    public void setOperator(Object operator) {
        this.operator = operator;
    }

    public Object getValue2() {
        return value2;
    }

    public void setValue2(Object value2) {
        this.value2 = value2;
    }
    public Parameters(){
        
    }
    public Parameters(Object value, Object operator, Object value2) {
        this.value = value;
        this.operator = operator;
        this.value2 = value2;
    }

    public Parameters(Object value, Object operator) {
        this.value = value;
        this.operator = operator;
    }
    
}
