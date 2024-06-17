package fopbot;

import java.util.EventListener;

/**
 * A {@link FieldHoverListener} is a listener for {@link FieldHoverEvent}s.
 */
public interface FieldHoverListener extends EventListener {
    /**
     * This method is called whenever the hovered field changes.
     *
     * @param field the field that is currently hovered
     */
    void onFieldHover(FieldHoverEvent field);
}
