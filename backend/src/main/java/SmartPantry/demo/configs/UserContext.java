package SmartPantry.demo.configs;

public class UserContext {
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    public static void setCurrentUserId(Long id) {
        currentUserId.set(id);
    }

    public static Long getCurrentUserId() {
        return currentUserId.get();
    }

    public static void setCurrentUsername(String username) {
        currentUsername.set(username);
    }

    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    public static void clear() {
        currentUserId.remove();
        currentUsername.remove();
    }
}