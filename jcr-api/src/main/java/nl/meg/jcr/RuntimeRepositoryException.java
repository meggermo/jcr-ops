package nl.meg.jcr;

import javax.jcr.RepositoryException;

/**
 * This exception is used to convert the checked {@link javax.jcr.RepositoryException} into an unchecked exception.
 * In general, recovering from a {@link javax.jcr.RepositoryException} is not possible by e.g. retrying the same method call.
 */
public class RuntimeRepositoryException extends RuntimeException {
    public RuntimeRepositoryException(RepositoryException e) {
        super(e);
    }
}
