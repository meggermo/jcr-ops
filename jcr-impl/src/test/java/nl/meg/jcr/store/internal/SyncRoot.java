package nl.meg.jcr.store.internal;

import java.util.concurrent.atomic.AtomicLong;

import nl.meg.jcr.store.JcrVersioned;

public record SyncRoot(AtomicLong version, String relPath, String syncRootPath) implements JcrVersioned {
}
