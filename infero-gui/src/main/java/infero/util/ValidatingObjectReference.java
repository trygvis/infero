package infero.util;

public class ValidatingObjectReference<T> {
    private T object;
    private boolean valid;

    public ValidatingObjectReference(T object) {
        this.object = object;
        valid = true;
    }

    public ValidatingObjectReference() {
    }

    public T getObject() {
        if(valid) {
            return object;
        }

        throw new RuntimeException("Not valid");
    }

    public void setObject(T object) {
        this.object = object;
    }
}
