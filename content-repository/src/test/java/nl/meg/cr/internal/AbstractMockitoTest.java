package nl.meg.cr.internal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import nl.meg.cr.RepositoryException;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMockitoTest {

    protected final <E extends Throwable> void shouldHaveThrown(Class<E> exceptionClass) {
        fail(String.format("Should have thrown a %s", exceptionClass.getSimpleName()));
    }

    protected NodeIterator getNodeIterator(final Node... nodes) {

        return new NodeIterator() {
            final AtomicInteger i = new AtomicInteger();
            final List<Node> nodeList = Arrays.asList(nodes);

            @Override
            public Node nextNode() {
                return nodeList.get(i.getAndIncrement());
            }

            @Override
            public void skip(long skipNum) {
                i.addAndGet((int) skipNum);
            }

            @Override
            public long getSize() {
                return nodeList.size();
            }

            @Override
            public long getPosition() {
                return i.get();
            }

            @Override
            public boolean hasNext() {
                return i.get() < getSize();
            }

            @Override
            public Object next() {
                return nextNode();
            }
        };
    }

    protected PropertyIterator getPropertyIterator(Property... properties) {
        return new PropertyIterator() {
            private final AtomicInteger i = new AtomicInteger();
            private final List<Property> propertyList = Arrays.asList(properties);

            @Override
            public Property nextProperty() {
                return propertyList.get(i.getAndIncrement());
            }

            @Override
            public void skip(long skipNum) {
                i.addAndGet((int) skipNum);
            }

            @Override
            public long getSize() {
                return propertyList.size();
            }

            @Override
            public long getPosition() {
                return i.get();
            }

            @Override
            public boolean hasNext() {
                return i.get() < propertyList.size();
            }

            @Override
            public Object next() {
                return nextProperty();
            }
        };
    }

    protected VersionIterator getVersionIterator(Version... versions) {
        return new VersionIterator() {
            private final AtomicInteger i = new AtomicInteger();
            private final List<Version> versionList = Arrays.asList(versions);

            @Override
            public Version nextVersion() {
                return versionList.get(i.getAndIncrement());
            }

            @Override
            public void skip(long skipNum) {
                i.addAndGet((int) skipNum);
            }

            @Override
            public long getSize() {
                return versionList.size();
            }

            @Override
            public long getPosition() {
                return i.get();
            }

            @Override
            public boolean hasNext() {
                return i.get() < versionList.size();
            }

            @Override
            public Object next() {
                return nextVersion();
            }
        };
    }

    protected void shouldHaveThrown() {
        shouldHaveThrown(RepositoryException.class);
    }
}
