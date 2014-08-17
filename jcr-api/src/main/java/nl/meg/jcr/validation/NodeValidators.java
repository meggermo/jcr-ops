package nl.meg.jcr.validation;

import nl.meg.jcr.HippoNode;
import nl.meg.validation.Validator;

public interface NodeValidators {

    Validator<HippoNode> isNotRoot();

    Validator<HippoNode> canAddChild(HippoNode parent);

    Validator<HippoNode> canRenameTo(String name);

    Validator<HippoNode> positionInBounds(int newPosition);

    Validator<HippoNode> supportsOrdering();
}
