package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public final class DbTimer {

    @NotNull
    private final Method method;
    protected volatile long invocationCount;
    protected volatile long totalTimeInNanos;

    public DbTimer(@NotNull Method method) {
        this.method = method;
    }

    @NotNull
    public Method getMethod() {
        return method;
    }

    public long getInvocationCount() {
        return invocationCount;
    }

    public long getTotalTimeInNanos() {
        return totalTimeInNanos;
    }

    public void onInvoke(long nanos) {
        invocationCount++;
        totalTimeInNanos += nanos;
    }
}
