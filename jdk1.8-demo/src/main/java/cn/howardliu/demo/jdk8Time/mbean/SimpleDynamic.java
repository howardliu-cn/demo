package cn.howardliu.demo.jdk8Time.mbean;

import javax.management.*;
import java.lang.reflect.Constructor;

/**
 * <br>created at 17-5-9
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class SimpleDynamic implements DynamicMBean {
    private String dClassName = this.getClass().getName();
    private String dDescription = "Simple implementation of a dynamic MBean.";

    private MBeanAttributeInfo[] dAttributes = new MBeanAttributeInfo[2];
    private MBeanConstructorInfo[] dConstructors = new MBeanConstructorInfo[1];
    private MBeanOperationInfo[] dOperations = new MBeanOperationInfo[1];
    private MBeanInfo dMBeanInfo = null;
    private String state = "initial state";
    private int nbChanges = 0;
    private int nbResets = 0;

    public SimpleDynamic() {
        buildDynamicMBeanInfo();
    }

    private void buildDynamicMBeanInfo() {
        dAttributes[0] = new MBeanAttributeInfo(
                "State",
                "java.lang.String",
                "State: state string.",
                true,
                true,
                false
        );
        dAttributes[1] = new MBeanAttributeInfo(
                "NbChanges",
                "java.lang.Integer",
                "NbChanges: number of times the State string has been changed.",
                true,
                false,
                false
        );

        Constructor[] constructors = this.getClass().getConstructors();
        dConstructors[0] = new MBeanConstructorInfo(
                "SimpleDynamic(): No-parameter constructor",
                constructors[0]
        );

        dOperations[0] = new MBeanOperationInfo(
                "reset",
                "Resets State and Nbchanges attributes to their initial values",
                null,
                "void",
                MBeanOperationInfo.ACTION
        );

        dMBeanInfo = new MBeanInfo(
                dClassName,
                dDescription,
                dAttributes,
                dConstructors,
                dOperations,
                new MBeanNotificationInfo[0]
        );
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return dMBeanInfo;
    }

    @Override
    public Object getAttribute(String attribute)
            throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Attribute name cannot be null"),
                    "Cannot invoke a getter of " + dClassName + " with null attribute name"
            );
        }

        switch (attribute) {
            case "State": {
                return getState();
            }
            case "NbChanges": {
                return getNbChanges();
            }
        }
        throw new AttributeNotFoundException("Cannot find " + attribute + " attribute in " + dClassName);
    }

    @Override
    public void setAttribute(Attribute attribute)
            throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        if (attribute == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Attribute cannot be null"),
                    "Cannot invoke a setter of " + dClassName + " with null attribute"
            );
        }

        String name = attribute.getName();
        Object value = attribute.getValue();

        switch (name) {
            case "State": {
                if (value == null) {
                    try {
                        setState(null);
                    } catch (Exception e) {
                        throw new InvalidAttributeValueException("Cannot set attribute " + name + " to null");
                    }
                } else {
                    try {
                        if ((Class.forName("java.lang.String")).isAssignableFrom(value.getClass())) {
                            setState((String) value);
                        } else {
                            throw new InvalidAttributeValueException(
                                    "Cannot set attribute " + name + " to a " + value.getClass()
                                            .getName() + " object, String expected");
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "NbChanges": {
                throw new AttributeNotFoundException("Cannot set a attribute " + name + " because it is read-only");
            }
            default: {
                throw new AttributeNotFoundException("Attribute " + name + " not found in " + dClassName);
            }
        }
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("attributes[] cannot be null"),
                    "Cannot invoke a getter of " + dClassName
            );
        }
        AttributeList resultList = new AttributeList();
        if (attributes.length == 0) {
            return resultList;
        }
        for (String attribute : attributes) {
            try {
                Object value = getAttribute(attribute);
                resultList.add(new Attribute(attribute, value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        if (attributes == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("AttributeList attributes cannot be null"),
                    "Cannot invoke a setter of " + dClassName
            );
        }

        AttributeList resultList = new AttributeList();
        if (attributes.isEmpty()) {
            return resultList;
        }
        for (Object attribute : attributes) {
            Attribute attr = (Attribute) attribute;
            try {
                setAttribute(attr);
                String name = attr.getName();
                Object value = getAttribute(name);
                resultList.add(new Attribute(name, value));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
            throws MBeanException, ReflectionException {
        if (actionName == null) {
            throw new RuntimeOperationsException(
                    new IllegalArgumentException("Operation name cannot be null"),
                    "Cannot invoke a null operation in " + dClassName
            );
        }
        switch (actionName) {
            case "reset": {
                reset();
                return null;
            }
            default: {
                throw new ReflectionException(
                        new NoSuchMethodException(actionName),
                        "Cannot find the operation " + actionName + " in " + dClassName
                );
            }
        }
    }

    public void reset() {
        this.state = "initial state";
        nbChanges = 0;
        this.nbResets++;
    }

    public String getState() {
        return state;
    }

    public SimpleDynamic setState(String state) {
        this.state = state;
        nbChanges++;
        return this;
    }

    public int getNbChanges() {
        return nbChanges;
    }

    public int getNbResets() {
        return nbResets;
    }
}
