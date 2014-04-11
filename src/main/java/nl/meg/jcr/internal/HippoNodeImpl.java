package nl.meg.jcr.internal;

import nl.meg.jcr.HippoNode;

import javax.jcr.Node;

final class HippoNodeImpl extends AbstractHippoItem<Node> implements HippoNode {

    HippoNodeImpl(Node node) {
        super(node, HippoNodeImpl::new);
    }

}
