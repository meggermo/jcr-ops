package nl.meg.jcr.predicate.internal;

import nl.meg.jcr.RepoFunctionInvoker;
import nl.meg.jcr.predicate.ValuePredicates;

import javax.jcr.Value;
import java.util.Calendar;
import java.util.function.Predicate;

final class ValuePredicatesImpl implements ValuePredicates {

    @Override
    public Predicate<Value> equalTo(String value) {
        return val -> RepoFunctionInvoker.invoke(val, Value::getString).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Long value) {
        return val -> RepoFunctionInvoker.invoke(val, Value::getLong).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Boolean value) {
        return val -> RepoFunctionInvoker.invoke(val, Value::getBoolean).equals(value);
    }

    @Override
    public Predicate<Value> equalTo(Calendar value) {
        return val -> RepoFunctionInvoker.invoke(val, Value::getDate).equals(value);
    }
}
