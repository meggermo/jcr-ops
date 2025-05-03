package nl.meg.jcr.store;

import java.util.concurrent.atomic.AtomicLong;

public interface JcrVersioned {
    AtomicLong version();
}
