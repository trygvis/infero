package infero.util;

/**
 * A memoizing product of arity 1.
 * <p/>
 * This is the basis for lazy structures which in short will ensure that calculate() is called only once for
 * each instance of the product.
 */
public abstract class P1<T> {

    private T _1;
    private boolean called;

    public final T _1() {
        if (called) {
            return _1;
        }

        _1 = calculate();
        called = true;

        return _1;
    }

    public abstract T calculate();
}
