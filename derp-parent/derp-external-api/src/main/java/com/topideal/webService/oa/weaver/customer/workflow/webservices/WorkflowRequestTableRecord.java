/**
 * WorkflowRequestTableRecord.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.topideal.webService.oa.weaver.customer.workflow.webservices;

public class WorkflowRequestTableRecord  implements java.io.Serializable {
    private Integer recordOrder;

    private WorkflowRequestTableField[] workflowRequestTableFields;

    public WorkflowRequestTableRecord() {
    }

    public WorkflowRequestTableRecord(
           Integer recordOrder,
           WorkflowRequestTableField[] workflowRequestTableFields) {
           this.recordOrder = recordOrder;
           this.workflowRequestTableFields = workflowRequestTableFields;
    }


    /**
     * Gets the recordOrder value for this WorkflowRequestTableRecord.
     * 
     * @return recordOrder
     */
    public Integer getRecordOrder() {
        return recordOrder;
    }


    /**
     * Sets the recordOrder value for this WorkflowRequestTableRecord.
     * 
     * @param recordOrder
     */
    public void setRecordOrder(Integer recordOrder) {
        this.recordOrder = recordOrder;
    }


    /**
     * Gets the workflowRequestTableFields value for this WorkflowRequestTableRecord.
     * 
     * @return workflowRequestTableFields
     */
    public WorkflowRequestTableField[] getWorkflowRequestTableFields() {
        return workflowRequestTableFields;
    }


    /**
     * Sets the workflowRequestTableFields value for this WorkflowRequestTableRecord.
     * 
     * @param workflowRequestTableFields
     */
    public void setWorkflowRequestTableFields(WorkflowRequestTableField[] workflowRequestTableFields) {
        this.workflowRequestTableFields = workflowRequestTableFields;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof WorkflowRequestTableRecord)) return false;
        WorkflowRequestTableRecord other = (WorkflowRequestTableRecord) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.recordOrder==null && other.getRecordOrder()==null) || 
             (this.recordOrder!=null &&
              this.recordOrder.equals(other.getRecordOrder()))) &&
            ((this.workflowRequestTableFields==null && other.getWorkflowRequestTableFields()==null) || 
             (this.workflowRequestTableFields!=null &&
              java.util.Arrays.equals(this.workflowRequestTableFields, other.getWorkflowRequestTableFields())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRecordOrder() != null) {
            _hashCode += getRecordOrder().hashCode();
        }
        if (getWorkflowRequestTableFields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWorkflowRequestTableFields());
                 i++) {
                Object obj = java.lang.reflect.Array.get(getWorkflowRequestTableFields(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkflowRequestTableRecord.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableRecord"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "recordOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workflowRequestTableFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "workflowRequestTableFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableField"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://webservices.workflow.weaver", "WorkflowRequestTableField"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
