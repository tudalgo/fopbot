package fopbot;

import java.awt.event.KeyEvent;

/**
 * A constant of this enumeration represents a key registered by the {@link InputHandler}.
 */
public enum Key {

    /**
     * The <code>Key</code> representing the <code>A</code> key.
     */
    A(KeyEvent.VK_A),

    /**
     * The <code>Key</code> representing the <code>B</code> key.
     */
    B(KeyEvent.VK_B),

    /**
     * The <code>Key</code> representing the <code>C</code> key.
     */

    C(KeyEvent.VK_C),

    /**
     * The <code>Key</code> representing the <code>D</code> key.
     */
    D(KeyEvent.VK_D),

    /**
     * The <code>Key</code> representing the <code>E</code> key.
     */
    E(KeyEvent.VK_E),

    /**
     * The <code>Key</code> representing the <code>F</code> key.
     */

    F(KeyEvent.VK_F),

    /**
     * The <code>Key</code> representing the <code>G</code> key.
     */
    G(KeyEvent.VK_G),

    /**
     * The <code>Key</code> representing the <code>H</code> key.
     */
    H(KeyEvent.VK_H),

    /**
     * The <code>Key</code> representing the <code>I</code> key.
     */
    I(KeyEvent.VK_I),

    /**
     * The <code>Key</code> representing the <code>J</code> key.
     */
    J(KeyEvent.VK_J),

    /**
     * The <code>Key</code> representing the <code>K</code> key.
     */
    K(KeyEvent.VK_K),

    /**
     * The <code>Key</code> representing the <code>L</code> key.
     */
    L(KeyEvent.VK_L),

    /**
     * The <code>Key</code> representing the <code>M</code> key.
     */
    M(KeyEvent.VK_M),

    /**
     * The <code>Key</code> representing the <code>N</code> key.
     */
    N(KeyEvent.VK_N),

    /**
     * The <code>Key</code> representing the <code>O</code> key.
     */
    O(KeyEvent.VK_O),

    /**
     * The <code>Key</code> representing the <code>P</code> key.
     */
    P(KeyEvent.VK_P),

    /**
     * The <code>Key</code> representing the <code>Q</code> key.
     */
    Q(KeyEvent.VK_Q),

    /**
     * The <code>Key</code> representing the <code>R</code> key.
     */
    R(KeyEvent.VK_R),

    /**
     * The <code>Key</code> representing the <code>S</code> key.
     */
    S(KeyEvent.VK_S),

    /**
     * The <code>Key</code> representing the <code>T</code> key.
     */
    T(KeyEvent.VK_T),

    /**
     * The <code>Key</code> representing the <code>U</code> key.
     */
    U(KeyEvent.VK_U),

    /**
     * The <code>Key</code> representing the <code>V</code> key.
     */
    V(KeyEvent.VK_V),

    /**
     * The <code>Key</code> representing the <code>W</code> key.
     */
    W(KeyEvent.VK_W),

    /**
     * The <code>Key</code> representing the <code>X</code> key.
     */
    X(KeyEvent.VK_X),

    /**
     * The <code>Key</code> representing the <code>Y</code> key.
     */
    Y(KeyEvent.VK_Y),

    /**
     * The <code>Key</code> representing the <code>Z</code> key.
     */
    Z(KeyEvent.VK_Z),

    /**
     * The <code>Key</code> representing the <code>SPACE</code> key.
     */
    SPACE(KeyEvent.VK_SPACE),

    /**
     * The <code>Key</code> representing the <code>LEFT</code> key.
     */
    LEFT(KeyEvent.VK_LEFT),

    /**
     * The <code>Key</code> representing the <code>RIGHT</code> key.
     */
    RIGHT(KeyEvent.VK_RIGHT),

    /**
     * The <code>Key</code> representing the <code>UP</code> key.
     */
    UP(KeyEvent.VK_UP),

    /**
     * The <code>Key</code> representing the <code>DOWN</code> key.
     */
    DOWN(KeyEvent.VK_DOWN);

    private final int keyCode;

    Key(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Returns the key code as used in {@link KeyEvent}.
     *
     * @return the key code
     */
    public int getKeyCode() {
        return keyCode;
    }
}
