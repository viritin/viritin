package org.vaadin.viritin.v7.fields;

import org.vaadin.viritin.v7.fields.AbstractNumberField;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import static org.junit.Assert.*;
import com.vaadin.v7.event.FieldEvents;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

/**
 * org.vaadin.viritin.fields.AbstractNumberFieldTest, created on 17/01/2017 15:39 <p>
 *
 * @author Charles
 */
public class AbstractNumberFieldTest {
  public static final BlurListener BLUR_LISTENER = event -> { /* NO-OP */ };
  public static final FocusListener FOCUS_LISTENER = event -> { /* NO-OP */ };

  protected ExampleField field;

  @Before
  public void setUp() throws Exception {
    field = new ExampleField();
  }

  @Test
  public void addAndRemoveBlurListener() {
    // Try both add remove a BlurListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(BlurEvent.class).isEmpty());
    field.addBlurListener(BLUR_LISTENER);
    Collection<?> listeners = field.tf.getListeners(BlurEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(BLUR_LISTENER, listeners.iterator().next());

    field.removeBlurListener(BLUR_LISTENER);
    assertTrue(field.tf.getListeners(BlurEvent.class).isEmpty());
  }


  @Test
  public void addAndRemoveListenerWithMethodOverload() {
    // Try both add remove a BlurListener via add/removeListener, and make sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(BlurEvent.class).isEmpty());
    field.addBlurListener(BLUR_LISTENER);
    Collection<?> listeners = field.tf.getListeners(BlurEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(BLUR_LISTENER, listeners.iterator().next());
    field.removeBlurListener(BLUR_LISTENER);
    assertTrue(field.tf.getListeners(BlurEvent.class).isEmpty());
  }

  @Test
  public void addAndRemoveFocusListener() {
    // Try both add remove a FocusListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FocusEvent.class).isEmpty());
    field.addFocusListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FocusEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(FOCUS_LISTENER, listeners.iterator().next());

    field.removeFocusListener(FOCUS_LISTENER);
    assertTrue(field.tf.getListeners(FocusEvent.class).isEmpty());
  }


  @Test
  public void addAndRemoveFocusListenerWithMethodOverload() {
    // Try both add remove a FocusListener, and ,ake sure they get delegated to the textfield
    assertTrue(field.tf.getListeners(FocusEvent.class).isEmpty());
    field.addFocusListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FocusEvent.class);
    assertEquals(1, listeners.size());
    assertEquals(FOCUS_LISTENER, listeners.iterator().next());

    field.removeFocusListener(FOCUS_LISTENER);
    assertTrue(field.tf.getListeners(FocusEvent.class).isEmpty());
  }


  @Test
  public void withFocusListener() {

    assertTrue(field.tf.getListeners(FocusEvent.class).isEmpty());

    AbstractNumberField<Integer> fluid = field.withFocusListener(FOCUS_LISTENER);
    Collection<?> listeners = field.tf.getListeners(FocusEvent.class);
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