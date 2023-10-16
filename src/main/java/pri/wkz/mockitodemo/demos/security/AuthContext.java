package pri.wkz.mockitodemo.demos.security;

/**
 * @author kuiZhi Wang
 */
public class AuthContext {

    private final static ThreadLocal<Long> context = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void setUserId(Long tenantId) {
        context.set(tenantId);
    }

    public static Long currentUserId() {
        return context.get();
    }

    public static void clean() {
        context.remove();
    }
}
