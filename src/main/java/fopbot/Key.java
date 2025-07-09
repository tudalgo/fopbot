package fopbot;

import java.awt.event.KeyEvent;

/**
 * Enumeration of supported keyboard keys for use in {@link KeyPressEvent}s.
 *
 * <p>Each key constant corresponds to a specific {@link java.awt.event.KeyEvent} key code.
 *
 * @see InputHandler
 */
public enum Key {

    /**
     * The {@code Key} representing the {@code A} key.
     */
    A(KeyEvent.VK_A),

    /**
     * The {@code Key} representing the {@code B} key.
     */
    B(KeyEvent.VK_B),

    /**
     * The {@code Key} representing the {@code C} key.
     */
    C(KeyEvent.VK_C),

    /**
     * The {@code Key} representing the {@code D} key.
     */
    D(KeyEvent.VK_D),

    /**
     * The {@code Key} representing the {@code E} key.
     */
    E(KeyEvent.VK_E),

    /**
     * The {@code Key} representing the {@code F} key.
     */
    F(KeyEvent.VK_F),

    /**
     * The {@code Key} representing the {@code G} key.
     */
    G(KeyEvent.VK_G),

    /**
     * The {@code Key} representing the {@code H} key.
     */
    H(KeyEvent.VK_H),

    /**
     * The {@code Key} representing the {@code I} key.
     */
    I(KeyEvent.VK_I),

    /**
     * The {@code Key} representing the {@code J} key.
     */
    J(KeyEvent.VK_J),

    /**
     * The {@code Key} representing the {@code K} key.
     */
    K(KeyEvent.VK_K),

    /**
     * The {@code Key} representing the {@code L} key.
     */
    L(KeyEvent.VK_L),

    /**
     * The {@code Key} representing the {@code M} key.
     */
    M(KeyEvent.VK_M),

    /**
     * The {@code Key} representing the {@code N} key.
     */
    N(KeyEvent.VK_N),

    /**
     * The {@code Key} representing the {@code O} key.
     */
    O(KeyEvent.VK_O),

    /**
     * The {@code Key} representing the {@code P} key.
     */
    P(KeyEvent.VK_P),

    /**
     * The {@code Key} representing the {@code Q} key.
     */
    Q(KeyEvent.VK_Q),

    /**
     * The {@code Key} representing the {@code R} key.
     */
    R(KeyEvent.VK_R),

    /**
     * The {@code Key} representing the {@code S} key.
     */
    S(KeyEvent.VK_S),

    /**
     * The {@code Key} representing the {@code T} key.
     */
    T(KeyEvent.VK_T),

    /**
     * The {@code Key} representing the {@code U} key.
     */
    U(KeyEvent.VK_U),

    /**
     * The {@code Key} representing the {@code V} key.
     */
    V(KeyEvent.VK_V),

    /**
     * The {@code Key} representing the {@code W} key.
     */
    W(KeyEvent.VK_W),

    /**
     * The {@code Key} representing the {@code X} key.
     */
    X(KeyEvent.VK_X),

    /**
     * The {@code Key} representing the {@code Y} key.
     */
    Y(KeyEvent.VK_Y),

    /**
     * The {@code Key} representing the {@code Z} key.
     */
    Z(KeyEvent.VK_Z),

    /**
     * The {@code Key} representing the {@code SPACE} key.
     */
    SPACE(KeyEvent.VK_SPACE),

    /**
     * The {@code Key} representing the {@code LEFT ARROW} key.
     */
    LEFT(KeyEvent.VK_LEFT),

    /**
     * The {@code Key} representing the {@code RIGHT ARROW} key.
     */
    RIGHT(KeyEvent.VK_RIGHT),

    /**
     * The {@code Key} representing the {@code UP ARROW} key.
     */
    UP(KeyEvent.VK_UP),

    /**
     * The {@code Key} representing the {@code DOWN ARROW} key.
     */
    DOWN(KeyEvent.VK_DOWN);

    /**
     * The {@link java.awt.event.KeyEvent} key code associated with this {@code Key}.
     */
    private final int keyCode;

    /**
     * Constructs a {@code Key} with the specified key code.
     *
     * @param keyCode the integer key code from {@link java.awt.event.KeyEvent}
     */
    Key(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Returns the integer key code associated with this {@code Key}.
     *
     * @return the {@link java.awt.event.KeyEvent} code for this key
     */
    public int getKeyCode() {
        return keyCode;
    }
}
