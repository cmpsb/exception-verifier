package net.wukl.exceptionverifier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * A verifier for Rule of Four compliant exception classes.
 */
public class ExceptionVerifier {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionVerifier.class);

    private static final String TEST_MESSAGE = "ExceptionVerifier test message";
    private static final Exception TEST_EXCEPTION = new RuntimeException(TEST_MESSAGE);
    private static final Exception TEST_IT_EXCEPTION = new InvocationTargetException(TEST_EXCEPTION);
    private static final String DEFAULT_PARAM_FORM = "(default; no-arg)";
    private static final String STRING_PARAM_FORM = "(String message)";
    private static final String NULL_STRING_PARAM_FORM = "(String message = null)";
    private static final Object[] NULL_ARG = new Object[] { null };

    /**
     * Creates a new verifier.
     *
     * @param type the class to verify
     *
     * @return the verifier builder
     */
    @Contract(value = "_ -> new", pure = true)
    @Nonnull
    public static ExceptionVerifierBuilder forClass(final Class<? extends Throwable> type) {
        return new ExceptionVerifierBuilder(type);
    }

    private final Class<? extends Throwable> exception;
    private final Class<? extends Throwable> causeType;
    private final Strictness strictness;

    @Contract(pure = true)
    private ExceptionVerifier(
            final Class<? extends Throwable> exception,
            final Class<? extends Throwable> causeType,
            final Strictness strictness
    ) {
        this.exception = exception;
        this.causeType = causeType;
        this.strictness = strictness;
    }

    private void verify() {
        this.verifyDefault();
        this.verifyMessageOnly();
        this.verifyNullableMessageOnly();
        this.verifyCauseOnly();
        this.verifyNestedInvocationTargetExceptionOnly();
        this.verifyNullableCauseOnly();
        this.verifyFull();
        this.verifyFullWithInvocationTargetExceptionCause();
        this.verifyFullWithNullMessage();
        this.verifyFullWithNullCause();
        this.verifyFullWithNullEverything();
    }

    private void verifyDefault() {
        try {
            final var ctor = this.exception.getConstructor();
            final var instance = ctor.newInstance();

            this.verifyEmptyMessage(instance.getMessage(), DEFAULT_PARAM_FORM);
            this.verifyEmptyCause(instance.getCause(), DEFAULT_PARAM_FORM);

            this.verifyRepeatability(instance, DEFAULT_PARAM_FORM);
        } catch (final Exception ex) {
            this.trapException(ex, DEFAULT_PARAM_FORM);
        }
    }

    private void verifyMessageOnly() {
        try {
            final var ctor = this.exception.getConstructor(String.class);
            final var instance = ctor.newInstance(TEST_MESSAGE);

            this.verifyNonEmptyMessage(instance.getMessage(), TEST_MESSAGE, STRING_PARAM_FORM);
            this.verifyEmptyCause(instance.getCause(), STRING_PARAM_FORM);

            this.verifyRepeatability(instance, STRING_PARAM_FORM);
        } catch (final Exception ex) {
            this.trapException(ex, STRING_PARAM_FORM);
        }
    }

    private void verifyNullableMessageOnly() {
        if (this.strictness != Strictness.STRICT) {
            return;
        }

        try {
            final var ctor = this.exception.getConstructor(String.class);
            final var instance = ctor.newInstance(NULL_ARG);

            this.verifyEmptyMessage(instance.getMessage(), NULL_STRING_PARAM_FORM);
            this.verifyEmptyCause(instance.getCause(), NULL_STRING_PARAM_FORM);

            this.verifyRepeatability(instance, NULL_STRING_PARAM_FORM);
        } catch (final Exception ex) {
            this.trapException(ex, NULL_STRING_PARAM_FORM);
        }
    }

    private void verifyCauseOnly(final Exception except, final String paramForm) {
        try {
            final var ctor = this.exception.getConstructor(this.causeType);
            final var instance = ctor.newInstance(except);

            this.verifyNonEmptyCause(instance.getCause(), except, paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyCauseOnly() {
        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(" + causeName + " cause)";

        this.verifyCauseOnly(TEST_EXCEPTION, paramForm);
    }

    private void verifyNestedInvocationTargetExceptionOnly() {
        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(" + causeName + " cause = InvocationTargetException)";

        this.verifyCauseOnly(TEST_IT_EXCEPTION, paramForm);
    }

    private void verifyNullableCauseOnly() {
        if (this.strictness != Strictness.STRICT) {
            return;
        }

        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(" + causeName + " cause = null)";

        try {
            final var ctor = this.exception.getConstructor(this.causeType);
            final var instance = ctor.newInstance(NULL_ARG);

            this.verifyEmptyCause(instance.getCause(), paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyFull(final Exception except, final String paramForm) {
        try {
            final var ctor = this.exception.getConstructor(String.class, this.causeType);
            final var instance = ctor.newInstance(TEST_MESSAGE, except);

            this.verifyNonEmptyMessage(instance.getMessage(), TEST_MESSAGE, paramForm);
            this.verifyNonEmptyCause(instance.getCause(), except, paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyFull() {
        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(String message, " + causeName + " cause)";

        this.verifyFull(TEST_EXCEPTION, paramForm);
    }

    private void verifyFullWithInvocationTargetExceptionCause() {
        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(String message, " + causeName
                                      + " cause = InvocationTargetException)";

        this.verifyFull(TEST_IT_EXCEPTION, paramForm);
    }

    private void verifyFullWithNullMessage() {
        if (this.strictness != Strictness.STRICT) {
            return;
        }

        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(String message = null, " + causeName + " cause)";

        try {
            final var ctor = this.exception.getConstructor(String.class, this.causeType);
            final var instance = ctor.newInstance(null, TEST_EXCEPTION);

            this.verifyNonEmptyCause(instance.getCause(), TEST_EXCEPTION, paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyFullWithNullCause() {
        if (this.strictness != Strictness.STRICT) {
            return;
        }

        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(String message, " + causeName + " cause = null)";

        try {
            final var ctor = this.exception.getConstructor(String.class, this.causeType);
            final var instance = ctor.newInstance(TEST_MESSAGE, null);

            this.verifyNonEmptyMessage(instance.getMessage(), TEST_MESSAGE, paramForm);
            this.verifyEmptyCause(instance.getCause(), paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyFullWithNullEverything() {
        if (this.strictness != Strictness.STRICT) {
            return;
        }

        final var causeName = this.causeType.getSimpleName();
        final var paramForm = "(String message, " + causeName + " cause = null)";

        try {
            final var ctor = this.exception.getConstructor(String.class, this.causeType);
            final var instance = ctor.newInstance(null, null);

            this.verifyEmptyMessage(instance.getMessage(), paramForm);
            this.verifyEmptyCause(instance.getCause(), paramForm);

            this.verifyRepeatability(instance, paramForm);
        } catch (final Exception ex) {
            this.trapException(ex, paramForm);
        }
    }

    private void verifyEmptyMessage(final @Nullable String message, final String paramForm) {
        if (this.strictness == Strictness.WEAK && message != null) {
            logger.warn(
                    "Non-null message ({}) when using the {} constructor. "
                            + "While this is not wrong, it is counterintuitive",
                    message, paramForm
            );
        }

        assert this.strictness != Strictness.STRICT || message == null
                : "Non-null message when using the " + paramForm + " constructor";
    }

    @Contract("null, _, _ -> fail")
    private void verifyNonEmptyMessage(
            final @Nullable String message,
            final @Nullable String expected,
            final String paramForm
    ) {
        assert expected == null || message != null : paramForm + " constructor set message to null";

        assert strictness != Strictness.WEAK || expected == null
                       || message.toLowerCase().contains(expected.toLowerCase())
                : paramForm + " constructor ignored or mangled message (expecting \""
                        + expected + "\", got " + message + ")";

        assert strictness != Strictness.STRICT || expected == null || message.equals(expected)
                : paramForm + " constructor ignored or changed message (expecting \""
                        + expected + "\", got " + message + ")";
    }

    private void verifyRepeatableMessage(
            final @Nullable String message,
            final @Nullable String message2,
            final String paramForm
    ) {
        assert Objects.equals(message, message2)
                : "Message changed after a second getMessage call when using the "
                          + paramForm + " constructor (first value was \"" + message
                          + "\", second was \"" + message2 + "\")";
    }

    @Contract("!null, _ -> fail")
    private void verifyEmptyCause(final @Nullable Throwable cause, final String paramForm) {
        assert cause == null : "Passing a null cause to " + paramForm
                                       + " did not produce a null cause";
    }

    @Contract("!null, null, _ -> fail")
    private void verifyNonEmptyCause(
            final @Nullable Throwable cause,
            final @Nullable Throwable expected,
            final String paramForm
    ) {
        assert expected != null || cause == null
                : "Passing null cause to " + paramForm + " did not produce a null cause";
        if (expected instanceof InvocationTargetException && this.strictness == Strictness.WEAK) {
            assert cause == expected || cause == expected.getCause()
                    : paramForm + " constructor ignored or mangled cause "
                    + "(expecting " + expected + ", got " + cause + ")";
        } else {
            assert cause == expected
                    : paramForm + " constructor ignored or changed cause "
                              + "(expecting " + expected + ", got " + cause + ")";

        }
    }

    private void verifyRepeatableCause(
            final @Nullable Throwable cause,
            final @Nullable Throwable cause2,
            final String paramForm
    ) {
        assert Objects.equals(cause, cause2)
                : "Cause changed after a second getCause call when using the "
                + paramForm + " constructor (first value was " + cause + ", second was"
                + cause2 + ")";
    }

    private void verifyRepeatability(final Throwable t, final String paramForm) {
        this.verifyRepeatableMessage(t.getMessage(), t.getMessage(), paramForm);
        this.verifyRepeatableCause(t.getCause(), t.getCause(), paramForm);
    }

    private void trapException(final Exception e, final String paramForm) {
        try {
            throw e;
        } catch (final NoSuchMethodException ex) {
            throw new AssertionError("Missing " + paramForm + " constructor");
        } catch (final IllegalAccessException ex) {
            throw new AssertionError(paramForm + " constructor is not accessible");
        } catch (final InstantiationException ex) {
            throw new AssertionError(paramForm + " constructor is not instantiable", ex);
        } catch (final InvocationTargetException ex) {
            throw new AssertionError(paramForm + " constructor failed with an exception", ex);
        } catch (final Exception ex) {
            throw new AssertionError(paramForm + " verification failed", ex);
        }
    }

    public static final class ExceptionVerifierBuilder {
        private final Class<? extends Throwable> exception;
        private Class<? extends Throwable> causeType = Throwable.class;
        private Strictness strictness = Strictness.STRICT;

        @Contract(pure = true)
        private ExceptionVerifierBuilder(final Class<? extends Throwable> exception) {
            this.exception = exception;
        }

        /**
         * Sets the cause exception type.
         *
         * Commonly used to change {@link Throwable} to {@link Exception} for style reasons.
         *
         * @param causeType the cause type
         *
         * @return the builder
         */
        @Contract("_ -> this")
        public ExceptionVerifierBuilder withCauseType(final Class<? extends Throwable> causeType) {
            this.causeType = causeType;
            return this;
        }

        /**
         * Sets the strictness level for the verifier.
         *
         * See {@link Strictness#WEAK} for details on the differences between the levels.
         *
         * @param strictness the strictness
         *
         * @return the builder
         */
        @Contract("_ -> this")
        public ExceptionVerifierBuilder withStrictness(final Strictness strictness) {
            this.strictness = strictness;
            return this;
        }

        /**
         * Runs verification.
         */
        public void verify() {
            new ExceptionVerifier(this.exception, this.causeType, strictness).verify();
        }
    }

    /**
     * The strictness of the verification.
     */
    public enum Strictness {
        /**
         * In weak mode, exceptions may mutate data that is given within reasonable limits.
         *
         * More concretely, the following cases that are forbidden in strict mode are allowed
         * in weak mode:
         * <ul>
         *     <li>Message-less constructors returning non-null messages</li>
         *     <li>Message case changes</li>
         *     <li>Extra text surrounding the message</li>
         *     <li>Unpacking InvocationTargetExceptions</li>
         *     <li>Non-nullable parameters</li>
         * </ul>
         */
        WEAK,

        /**
         * In strict mode, exceptions are not allowed to modify their values and must pass these
         * directly to the underlying exception class.
         */
        STRICT
    }
}
