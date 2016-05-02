package api.model;

/**
 * Created by matthew on 01.05.16.
 */
public class IdNotSpecifiedException extends RuntimeException {
    public IdNotSpecifiedException(){

    }
    public IdNotSpecifiedException(String s) {
        super(s);
    }
}
