package org.vaadin.viritin.fields;

import static org.junit.Assert.*;
import com.vaadin.event.FieldEvents;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * org.vaadin.viritin.fields.AbstractNumberFieldTest, created on 17/01/2017 15:39 <p>
 *
 * @author Charles
 */
public class AbstractNumberFieldTest {
  public static final FieldEvents.BlurListener BLUR_LISTENER = event -> { /* NO-OP */ };
  public static final FieldEvents.FocusListener FOCUS_LISTENER = event -> { /* NO-OP */ };

  protected ExampleField field;

  @Before
  public void setUp() throws Exception {
    field = new ExampleField();
  }

  @Test
  public void addAndRemoveBlurListener() {
    // Try both add remove a BlurListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FieldEvents.BlurEvent.class).isEmpty());
    field.addBlurListener(BLUR_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FieldEvents.BlurEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(BLUR_LISTENER, listeners.iterator().next());

    field.removeBlurListener(BLUR_LISTENER);
    assertTrue(field.tf.getListeners(FieldEvents.BlurEvent.class).isEmpty());
  }


  @Test
  public void addAndRemoveListenerWithMethodOverload() {
    // Try both add remove a BlurListener via add/removeListener, and make sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FieldEvents.BlurEvent.class).isEmpty());
    field.addListener(BLUR_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FieldEvents.BlurEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(BLUR_LISTENER, listeners.iterator().next());
    field.removeListener(BLUR_LISTENER);
    assertTrue(field.tf.getListeners(FieldEvents.BlurEvent.class).isEmpty());
  }

  @Test
  public void addAndRemoveFocusListener() {
    // Try both add remove a FocusListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FieldEvents.FocusEvent.class).isEmpty());
    field.addFocusListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FieldEvents.FocusEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(FOCUS_LISTENER, listeners.iterator().next());

    field.removeFocusListener(FOCUS_LISTENER);
    assertTrue(field.tf.getListeners(FieldEvents.FocusEvent.class).isEmpty());
  }


  @Test
  public void addAndRemoveFocusListenerWithMethodOverload() {
    // Try both add remove a FocusListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FieldEvents.FocusEvent.class).isEmpty());
    field.addListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FieldEvents.FocusEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(FOCUS_LISTENER, listeners.iterator().next());

    field.removeListener(FOCUS_LISTENER);
    assertTrue(field.tf.getListeners(FieldEvents.FocusEvent.class).isEmpty());
  }


  @Test
  public void withFocusListener() {

    assertTrue(field.tf.getListeners(FieldEvents.FocusEvent.class).isEmpty());

    AbstractNumberField<Integer> fluid = field.withFocusListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FieldEvents.FocusEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(FOCUS_LISTENER, listeners.iterator().next());
    assertSame(field, fluid);
  }

  public class ExampleField extends AbstractNumberField<Integer> {

    protected void userInputToValue(String str) {
      // No-Op
    }

    public Class<? extends Integer> getType() {
      return Integer.class;
    }
  }
}