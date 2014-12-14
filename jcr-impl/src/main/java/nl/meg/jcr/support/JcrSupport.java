package nl.meg.jcr.support;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.HippoProperty;
import nl.meg.jcr.HippoValue;

import java.util.Calendar;
import java.util.Optional;

public class JcrSupport {

    public static Optional<HippoValue> getValue(HippoNode node, String propertyName) {
        return node.getProperty(propertyName)
                .map(HippoProperty::getValue)
                .orElse(Optional.<HippoValue>empty());
    }

    public static Optional<String> getString(HippoNode node, String propertyName) {
        return getValue(node, propertyName).map(HippoValue::getString);
    }

    public static Optional<Calendar> getDate(HippoNode node, String propertyName) {
        return getValue(node, propertyName).map(HippoValue::getDate);
    }

}
