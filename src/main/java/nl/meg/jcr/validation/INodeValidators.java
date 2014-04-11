package nl.meg.jcr.validation;

import nl.meg.jcr.HippoNode;
import nl.meg.validation.Validator;

public interface INodeValidators {

    Validator<NodeErrorCode, HippoNode> isNotRoot();

    Validator<NodeErrorCode, HippoNode> canAddChild(HippoNode parent);

    Validator<NodeErrorCode, HippoNode> canRenameTo(String name);

    Validator<NodeErrorCode,HippoNode> positionInBounds(int newPosition);

    Validator<NodeErrorCode,HippoNode> supportsOrdering();
}
