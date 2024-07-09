package ar.com.util.exceptions;

import java.text.MessageFormat;
import java.util.List;

public class ExceptionUtil {

    public static final String MIN_VALUE_ERR_MSG = "El argumento {0} no puede tener un valor inferior a <{1}> y tiene <{2}>.";
    public static final String NULL_ERR_MSG = "El argumento {0} no puede ser nulo.";
    public static final String LENGTH_ERR_MSG = "El argumento {0} no puede ser nulo y debe tener una longitud de {1}.";
    public static final String SIZE_ERR_MSG = "El argumento {0} no puede ser nulo y debe tener {1} elementos.";

    private ExceptionUtil(){}

    public static void checkMinValueArgument(int o, int minValue, String name){
        if(o < minValue){
            throw new IllegalArgumentException(MessageFormat.format(MIN_VALUE_ERR_MSG, name, minValue, o));
        }
    }

    public static void checkMinValueArgument(long o, long minValue, String name){
        if (o < minValue){
            throw new IllegalArgumentException(MessageFormat.format(MIN_VALUE_ERR_MSG, name, minValue, o));
        }
    }

    public static void checkNullArgument(Object o, String name) {
        if (o == null) {
            throw new IllegalArgumentException(MessageFormat.format(NULL_ERR_MSG, name));
        }
    }

    public static void checkListSizeArgument(List a, String name, int length) {
        if (a == null || a.size() != length) {
            throw new IllegalArgumentException(MessageFormat.format(SIZE_ERR_MSG, name, length));
        }
    }

    public static <T> void checkArrayLengthArgument(T[] a, String name, int length) {
        if (a == null || a.length != length) {
            throw new IllegalArgumentException(MessageFormat.format(LENGTH_ERR_MSG, name, length));
        }
    }

    public static void checkArgument(boolean throwException, String message, Object... args) {
        if (throwException) {
            throw new IllegalArgumentException(MessageFormat.format(message, args));
        }
    }

}
