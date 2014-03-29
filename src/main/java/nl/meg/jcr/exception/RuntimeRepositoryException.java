package nl.meg.jcr.exception;

import javax.jcr.RepositoryException;

public class RuntimeRepositoryException extends RuntimeException {
    public RuntimeRepositoryException(RepositoryException e) {
        super(e);
    }
}
