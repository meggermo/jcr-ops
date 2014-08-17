package nl.meg.validation.impl;

import nl.meg.validation.ValidationContext;
import nl.meg.validation.ValidationException;
import nl.meg.validation.Validator;
import nl.meg.validation.ValidatorBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class ValidatorBuilderImpl<S> implements ValidatorBuilder<S> {
	
	private final List<Validator<S>> validators;

	public ValidatorBuilderImpl(List<Validator<S>> validators) {
		this.validators = validators;
	}

	public ValidatorBuilderImpl() {
		this(Collections.<Validator<S>>emptyList());
	}

	public ValidatorBuilderImpl<S> add(Validator<S> validator) {
		final List<Validator<S>> newValidators = new ArrayList<>(validators);
		newValidators.add(validator);
		return new ValidatorBuilderImpl<>(newValidators);
	}

    @Override
    public Validator<S> build(Predicate<ValidationContext> terminationPredicate) {
        return new CompositeValidator<>(validators, terminationPredicate);
    }

	static class CompositeValidator<S> implements Validator<S> {
		
		private final List<Validator<S>> validators;
		private final Predicate<ValidationContext> shouldTerminate;
		
		CompositeValidator(List<Validator<S>> validators, Predicate<ValidationContext> shouldTerminate) {
			this.validators = validators;
			this.shouldTerminate = shouldTerminate;
		}
		
		@Override
		public ValidationContext validate(S subject, ValidationContext context) throws ValidationException {
			if (shouldTerminate.test(context)) {
				return context;
			}
			for (Validator<S> validator : validators) {
				context = validator.validate(subject, context);
				if (shouldTerminate.test(context)) {
					return context;
				}
			}
			return context;
		}
	}
}
