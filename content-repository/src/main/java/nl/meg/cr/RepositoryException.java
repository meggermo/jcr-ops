package nl.meg.cr;

public class RepositoryException extends RuntimeException {
    public RepositoryException(javax.jcr.RepositoryException e) {
        super(e);
    }
}
