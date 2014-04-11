package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.RepoFunctionInvoker;
import nl.meg.jcr.predicate.ValuePredicates;

import javax.jcr.Value;
import java.util.Calendar;
import java.util.function.Predicate;

final class ValuePredicatesImpl implements ValuePredicates {

    @Override
    public Predicate<Value> equalTo(String value) {
        return val -> RepoFunctionInvoker.invoke(Value::getString, val).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Long value) {
        return val -> RepoFunctionInvoker.invoke(Value::getLong, val).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Boolean value) {
        return val -> RepoFunctionInvoker.invoke(Value::getBoolean, val).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Calendar value) {
        return val -> RepoFunctionInvoker.invoke(Value::getDate, val).equals(value);
    }
}
