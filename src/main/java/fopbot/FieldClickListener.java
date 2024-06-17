package fopbot;

import java.util.EventListener;

/**
 * A {@link FieldClickListener} is a listener for {@link FieldClickEvent}s.
 */
public interface FieldClickListener extends EventListener {


    /**
     * Notifies this field click listener about the given field click event.
     *
     * @param event the field click event
     */
    void onFieldClick(FieldClickEvent event);
}
