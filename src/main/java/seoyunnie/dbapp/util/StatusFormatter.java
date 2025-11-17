package seoyunnie.dbapp.util;

public final class StatusFormatter {
    private StatusFormatter() {
    }

    public static <T extends Enum<T>> String format(T status) {
        String name = status.name();

        if (!name.contains("_")) {
            return name;
        }

        return name.toLowerCase().replaceAll("_", " ");
    }
}
