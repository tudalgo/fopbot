package fopbot;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Listener interface for handling clicks on fields in the virtual world.
 *
 * <p>Implement this interface to respond to field click events via the {@link #onFieldClick(FieldClickEvent)} method.
 */
public interface FieldClickListener extends EventListener {

    /**
     * Invoked when a field in the virtual world is clicked.
     *
     * @param event the event containing information about the clicked field
     */
    void onFieldClick(@NotNull FieldClickEvent event);
}
