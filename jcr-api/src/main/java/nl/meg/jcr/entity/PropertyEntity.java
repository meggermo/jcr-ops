package nl.meg.jcr.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.immutables.value.Value;

@XmlRootElement(name = "property")
@Value.Immutable
public interface PropertyEntity<T> {

    @XmlAttribute
    String name();

    @XmlAttribute
    PropertyEntityType type();

    @XmlAttribute
    boolean multiple();

    @XmlElementWrapper
    @XmlElement(name = "value")
    List<T> values();

}
