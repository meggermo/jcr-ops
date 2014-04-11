package nl.meg.jcr.validation.internal;

import nl.meg.jcr.HippoNode;
import nl.meg.jcr.validation.NodeErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PositionInBoundsValidatorTest {

    private PositionInBoundsValidator validator;

    @Mock
    private HippoNode entity, parent;

    @Before
    public void setUp() {
        this.validator = new PositionInBoundsValidator(0);
    }

    @Test
    public void testGetContextMap() {
        when(entity.getParent()).thenReturn(Optional.of(parent));
        when(parent.getNodes()).thenReturn(Arrays.asList(entity).iterator());
        assertThat(validator.getContextMap(entity).containsKey("min"), is(true));
        assertThat(validator.getContextMap(entity).containsKey("max"), is(true));
    }

    @Test
    public void testGetError() {
        assertThat(validator.getError(), is(NodeErrorCode.POSITION_OUT_OF_RANGE));
    }
}
