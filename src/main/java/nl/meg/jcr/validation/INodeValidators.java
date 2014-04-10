package nl.meg.jcr.validation;

import nl.meg.jcr.INode;
import nl.meg.validation.Validator;

public interface INodeValidators {

    Validator<NodeErrorCode, INode> isNotRoot();

    Validator<NodeErrorCode, INode> canAddChild(INode parent);

    Validator<NodeErrorCode, INode> canRenameTo(String name);

    Validator<NodeErrorCode,INode> positionInBounds(int newPosition);

    Validator<NodeErrorCode,INode> supportsOrdering();
}
