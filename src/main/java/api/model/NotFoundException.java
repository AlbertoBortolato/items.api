package api.model;

import java.util.NoSuchElementException;

/**
 * Created by matthew on 01.05.16.
 */
public class NotFoundException extends NoSuchElementException {

    public NotFoundException() {

    }
    public NotFoundException(String s) {
        super(s);

    }
}
