package nl.meg.jcr.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.immutables.value.Value;

@XmlRootElement(name = "node")
@Value.Immutable
public interface NodeEntity {

    @XmlAttribute
    String id();

    @XmlAttribute
    String path();

    @XmlAttribute
    String name();

    @XmlElementWrapper
    @XmlElement(name = "type")
    List<String> types();

    @XmlElementWrapper
    @XmlElement(name = "node")
    List<NodeEntity> nodes();

    @XmlElementWrapper
    @XmlElement(name = "property")
    List<PropertyEntity> properties();

}
