public final class Util {

    public static String convertToString(Object obj) {
        if (obj == null) return "none";
        if (obj instanceof Double) {
            String s = obj.toString(); 

            if (!s.endsWith(".0")) return s;

            return s.substring(0, s.length() - 2);
        }  

        return obj.toString();
    }

    public static boolean isTruthy(Object value) {
        if (value == null) return false;

        if (value instanceof String) {
            if (convertToString(value) == "") {
                return false;
            }
        }

        if (value instanceof Double) {
            if ((double)value == 0.0) {
                return false;
            }
        }

        if (value instanceof Boolean) return (boolean)value;
        return true;
    }
}
