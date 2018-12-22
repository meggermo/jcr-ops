package nl.meg.entity.reader;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.jcr.NamespaceRegistry;

public class JcrPrefixFilter implements Predicate<String> {

    private static final Set<String> JCR_NAMESPACE_PREFIXES = Stream.of(
            NamespaceRegistry.PREFIX_JCR,
            NamespaceRegistry.PREFIX_MIX,
            NamespaceRegistry.PREFIX_NT,
            NamespaceRegistry.PREFIX_XML
    ).collect(Collectors.toSet());

    private Set<String> namespacePrefixes;

    public JcrPrefixFilter(final Set<String> namespacePrefixes) {
        Objects.requireNonNull(namespacePrefixes);
        this.namespacePrefixes = namespacePrefixes;
    }

    public JcrPrefixFilter() {
        this(getJcrNamespacePrefixes());
    }

    public static Set<String> getJcrNamespacePrefixes() {
        return new HashSet<>(JCR_NAMESPACE_PREFIXES);
    }

    @Override
    public boolean test(final String value) {
        final int separatorIndex = value.indexOf(':');
        return separatorIndex < 0 || !namespacePrefixes.contains(value.substring(0, separatorIndex));
    }
}
