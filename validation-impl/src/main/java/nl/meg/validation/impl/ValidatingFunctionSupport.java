package nl.meg.validation.impl;

import nl.meg.validation.ValidationContext;
import nl.meg.validation.Validator;

import java.util.function.Function;

public final class ValidatingFunctionSupport {

	private ValidatingFunctionSupport() {
	}

	public static <S,T> Function<S,T> preValidate(Function<S,T> function, Validator<S> validator) {
		return new PreValidatingFunction<>(validator,new ValidationContextImpl(),function);
	}
	
	public static <S,T> Function<S,T> postValidate(Function<S,T> function, Validator<T> validator) {
		return new PostValidatingFunction<>(validator,new ValidationContextImpl(),function);
	}
	
	private static class PreValidatingFunction<S, T> implements Function<S, T> {

		private final Validator<S> preValidator;
		private final ValidationContextImpl context;
		private final Function<S,T> function;

		private PreValidatingFunction(Validator<S> preValidator,ValidationContextImpl context, Function<S,T> function) {
			this.preValidator = preValidator;
			this.context = context;
			this.function = function;
		}

		@Override
		public T apply(S subject) {
			final ValidationContext newContext = preValidator.validate(subject, context);
			if (newContext.hasErrors()) {
				throw new PreValidationException(newContext.getErrorMap());
			}
			return function.apply(subject);
		}

	}

	private static class PostValidatingFunction<S, T> implements Function<S, T> {

		private final Validator<T> postValidator;
		private final ValidationContextImpl context;
		private final Function<S,T> function;

		private PostValidatingFunction(Validator<T> postValidator,ValidationContextImpl context, Function<S,T> function) {
			this.postValidator = postValidator;
			this.context = context;
			this.function = function;
		}

		@Override
		public T apply(S subject) {
			final T result = function.apply(subject);
			final ValidationContext newContext = postValidator.validate(result, context);
			if (newContext.hasErrors()) {
				throw new PostValidationException(newContext.getErrorMap());
			}
			return result;
		}

	}
}
