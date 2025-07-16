package fopbot;

import org.jetbrains.annotations.NotNull;

import java.util.EventListener;


/**
 * Listener interface for receiving hover events over fields.
 *
 * <p>Implement this interface if you want to handle {@link FieldHoverEvent}s, which occur when the mouse cursor
 * enters, moves across, or exits a field.
 */
public interface FieldHoverListener extends EventListener {

    /**
     * Invoked when a hover event occurs on a field.
     *
     * @param field the hover event containing current and previous field information
     */
    void onFieldHover(@NotNull FieldHoverEvent field);
}

