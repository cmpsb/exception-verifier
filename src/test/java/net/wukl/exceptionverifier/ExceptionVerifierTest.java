package net.wukl.exceptionverifier;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionVerifierTest {
    @Test
    public void testGoodCitizenWeak() {
        assertDoesNotThrow(() -> ExceptionVerifier.forClass(GoodCitizenException.class)
                .withStrictness(ExceptionVerifier.Strictness.WEAK)
                .verify()
        );
    }

    @Test
    public void testGoodCitizenStrict() {
        assertDoesNotThrow(() -> ExceptionVerifier.forClass(GoodCitizenException.class).verify());
    }

    @Test
    public void testHiddenConstructorsWeak() {
        assertThrows(AssertionError.class, () ->
                ExceptionVerifier.forClass(HiddenConstructorsException.class)
                .withStrictness(ExceptionVerifier.Strictness.WEAK)
                .verify()
        );
    }

    @Test
    public void testHiddenConstructorsStrict() {
        assertThrows(AssertionError.class, () ->
                ExceptionVerifier.forClass(HiddenConstructorsException.class)
                .verify()
        );
    }

    @Test
    public void testHiddenExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(HiddenException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testHiddenExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(HiddenException.class)
                    .verify();
        });
    }

    @Test
    public void testThrowingExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(ThrowingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testThrowingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(ThrowingException.class)
                    .verify();
        });
    }

    @Test
    public void testWrappingExceptionWeak() {
        assertDoesNotThrow(() -> {
            ExceptionVerifier.forClass(WrappingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testWrappingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(WrappingException.class)
                    .verify();
        });
    }

    @Test
    public void testInvocationUnwrappingExceptionWeak() {
        assertDoesNotThrow(() -> {
            ExceptionVerifier.forClass(InvocationUnwrappingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testInvocationUnwrappingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(InvocationUnwrappingException.class)
                    .verify();
        });
    }

    @Test
    public void testSpeakingExceptionWeak() {
        assertDoesNotThrow(() -> {
            ExceptionVerifier.forClass(PuttingTheWordsInMyMouthException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testSpeakingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(PuttingTheWordsInMyMouthException.class)
                    .verify();
        });
    }

    @Test
    public void testMessageReplacingExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(MessageReplacingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testMessageReplacingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(MessageReplacingException.class)
                    .verify();
        });
    }

    @Test
    public void testCauseReplacingExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CauseReplacingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();;
        });
    }

    @Test
    public void testCauseReplacingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CauseReplacingException.class)
                    .verify();
        });
    }

    @Test
    public void testErasingExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(MessageErasingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testErasingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(MessageErasingException.class)
                    .verify();
        });
    }

    @Test
    public void testCauseErasingExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CauseErasingException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testCauseErasingExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CauseErasingException.class)
                    .verify();
        });
    }

    @Test
    public void testCountingMessageExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CountingMessageException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testCountingMessageExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CountingMessageException.class)
                    .verify();
        });
    }

    @Test
    public void testCauseMessageExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CountingCauseException.class)
                    .verify();
        });
    }

    @Test
    public void testCountingCauseExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(CountingCauseException.class)
                    .verify();
        });
    }

    @Test
    public void testBadGetMessageExceptionWeak() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(BadGetMessageException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testBadGetMessageExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(BadGetMessageException.class)
                    .verify();
        });
    }

    @Test
    public void testNullMessageSensitiveExceptionWeak() {
        assertDoesNotThrow(() -> {
            ExceptionVerifier.forClass(NullMessageSensitiveException.class)
                    .withStrictness(ExceptionVerifier.Strictness.WEAK)
                    .verify();
        });
    }

    @Test
    public void testNullMessageSensitiveExceptionStrict() {
        assertThrows(AssertionError.class, () -> {
            ExceptionVerifier.forClass(NullMessageSensitiveException.class)
                    .verify();
        });
    }

    public static final class GoodCitizenException extends RuntimeException {
        /**
         * Creates a new good citizen exception.
         */
        public GoodCitizenException() {
            super();
        }

        /**
         * Creates a new good citizen exception.
         *
         * @param message the message explaining what caused the exception
         */
        public GoodCitizenException(final String message) {
            super(message);
        }

        /**
         * Creates a new good citizen exception.
         *
         * @param cause the exception that caused this exception
         */
        public GoodCitizenException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new good citizen exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public GoodCitizenException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    public static final class HiddenConstructorsException extends RuntimeException {
        /**
         * Creates a new hidden constructors exception.
         */
        private HiddenConstructorsException() {
            super();
        }

        /**
         * Creates a new hidden constructors exception.
         *
         * @param message the message explaining what caused the exception
         */
        public HiddenConstructorsException(final String message) {
            super(message);
        }

        /**
         * Creates a new hidden constructors exception.
         *
         * @param cause the exception that caused this exception
         */
        protected HiddenConstructorsException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new hidden constructors exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public HiddenConstructorsException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    private static final class HiddenException extends RuntimeException {
        /**
         * Creates a new hidden exception.
         */
        protected HiddenException() {
            super();
        }

        /**
         * Creates a new hidden exception.
         *
         * @param message the message explaining what caused the exception
         */
        public HiddenException(final String message) {
            super(message);
        }

        /**
         * Creates a new hidden exception.
         *
         * @param cause the exception that caused this exception
         */
        protected HiddenException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new hidden exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public HiddenException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    public static final class ThrowingException extends RuntimeException {
        /**
         * Creates a new throwing exception.
         */
        public ThrowingException() {
            super();
        }

        /**
         * Creates a new throwing exception.
         *
         * @param message the message explaining what caused the exception
         */
        public ThrowingException(final String message) {
            super(message);
        }

        /**
         * Creates a new throwing exception.
         *
         * @param cause the exception that caused this exception
         */
        public ThrowingException(final Throwable cause) {
            throw new RuntimeException(cause);
        }

        /**
         * Creates a new throwing exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public ThrowingException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    public static final class WrappingException extends RuntimeException {
        /**
         * Creates a new wrapping exception.
         */
        public WrappingException() {
            super();
        }

        /**
         * Creates a new wrapping exception.
         *
         * @param message the message explaining what caused the exception
         */
        public WrappingException(final String message) {
            super("bad! " + message);
        }

        /**
         * Creates a new wrapping exception.
         *
         * @param cause the exception that caused this exception
         */
        public WrappingException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new wrapping exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public WrappingException(final String message, final Throwable cause) {
            super("bad! " + message, cause);
        }
    }

    public static final class InvocationUnwrappingException extends RuntimeException {
        /**
         * Creates a new invocation unwrapping exception.
         */
        public InvocationUnwrappingException() {
            super();
        }

        /**
         * Creates a new invocation unwrapping exception.
         *
         * @param message the message explaining what caused the exception
         */
        public InvocationUnwrappingException(final String message) {
            super(message);
        }

        /**
         * Creates a new invocation unwrapping exception.
         *
         * @param cause the exception that caused this exception
         */
        public InvocationUnwrappingException(final Throwable cause) {
            super(cause instanceof InvocationTargetException ? cause.getCause() : cause);
        }

        /**
         * Creates a new invocation unwrapping exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public InvocationUnwrappingException(final String message, final Throwable cause) {
            super(message, cause instanceof InvocationTargetException ? cause.getCause() : cause);
        }
    }

    public static final class PuttingTheWordsInMyMouthException extends RuntimeException {
        /**
         * Creates a new putting the words in my mouth exception.
         */
        public PuttingTheWordsInMyMouthException() {
            super("haha");
        }

        /**
         * Creates a new putting the words in my mouth exception.
         *
         * @param message the message explaining what caused the exception
         */
        public PuttingTheWordsInMyMouthException(final String message) {
            super(message);
        }

        /**
         * Creates a new putting the words in my mouth exception.
         *
         * @param cause the exception that caused this exception
         */
        public PuttingTheWordsInMyMouthException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new putting the words in my mouth exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public PuttingTheWordsInMyMouthException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    public static final class MessageReplacingException extends RuntimeException {
        /**
         * Creates a new message replacing exception.
         */
        public MessageReplacingException() {
            super();
        }

        /**
         * Creates a new message replacing exception.
         *
         * @param message the message explaining what caused the exception
         */
        public MessageReplacingException(final String message) {
            super("not what you said");
        }

        /**
         * Creates a new message replacing exception.
         *
         * @param cause the exception that caused this exception
         */
        public MessageReplacingException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new message replacing exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public MessageReplacingException(final String message, final Throwable cause) {
            super("!!", cause);
        }
    }

    public static final class CauseReplacingException extends RuntimeException {
        /**
         * Creates a new cause replacing exception.
         */
        public CauseReplacingException() {
            super();
        }

        /**
         * Creates a new cause replacing exception.
         *
         * @param message the message explaining what caused the exception
         */
        public CauseReplacingException(final String message) {
            super(message);
        }

        /**
         * Creates a new cause replacing exception.
         *
         * @param cause the exception that caused this exception
         */
        public CauseReplacingException(final Throwable cause) {
            super(new RuntimeException(cause));
        }

        /**
         * Creates a new cause replacing exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public CauseReplacingException(final String message, final Throwable cause) {
            super(message, new RuntimeException());
        }
    }

    public static final class MessageErasingException extends RuntimeException {

        /**
         * Creates a new erasing exception.
         */
        public MessageErasingException() {
            super();
        }

        /**
         * Creates a new erasing exception.
         *
         * @param message the message explaining what caused the exception
         */
        public MessageErasingException(final String message) {
            super();
        }

        /**
         * Creates a new erasing exception.
         *
         * @param cause the exception that caused this exception
         */
        public MessageErasingException(final Throwable cause) {
            super();
        }

        /**
         * Creates a new erasing exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public MessageErasingException(final String message, final Throwable cause) {
            super();
        }
    }

    public static final class CauseErasingException extends RuntimeException {

        /**
         * Creates a new cause erasing exception.
         */
        public CauseErasingException() {
            super();
        }

        /**
         * Creates a new cause erasing exception.
         *
         * @param message the message explaining what caused the exception
         */
        public CauseErasingException(final String message) {
            super(message);
        }

        /**
         * Creates a new cause erasing exception.
         *
         * @param cause the exception that caused this exception
         */
        public CauseErasingException(final Throwable cause) {
            super();
        }

        /**
         * Creates a new cause erasing exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public CauseErasingException(final String message, final Throwable cause) {
            super(message);
        }
    }

    public static final class CountingMessageException extends RuntimeException {
        private int i = 0;

        /**
         * Creates a new counting message exception.
         */
        public CountingMessageException() {
            super();
        }

        /**
         * Creates a new counting message exception.
         *
         * @param message the message explaining what caused the exception
         */
        public CountingMessageException(final String message) {
            super(message);
        }

        /**
         * Creates a new counting message exception.
         *
         * @param cause the exception that caused this exception
         */
        public CountingMessageException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new counting message exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public CountingMessageException(final String message, final Throwable cause) {
            super(message, cause);
        }

        @NotNull
        @Override
        public final String getMessage() {
            return String.valueOf(this.i++);
        }
    }

    public static final class CountingCauseException extends RuntimeException {
        private int i = 0;

        /**
         * Creates a new counting message exception.
         */
        public CountingCauseException() {
            super();
        }

        /**
         * Creates a new counting message exception.
         *
         * @param message the message explaining what caused the exception
         */
        public CountingCauseException(final String message) {
            super(message);
        }

        /**
         * Creates a new counting message exception.
         *
         * @param cause the exception that caused this exception
         */
        public CountingCauseException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new counting message exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public CountingCauseException(final String message, final Throwable cause) {
            super(message, cause);
        }

        @NotNull
        @Override
        public final Throwable getCause() {
            return new RuntimeException(String.valueOf(this.i++));
        }
    }

    public static final class BadGetMessageException extends RuntimeException {

        /**
         * Creates a new bad get message exception.
         */
        public BadGetMessageException() {
            super();
        }

        /**
         * Creates a new bad get message exception.
         *
         * @param message the message explaining what caused the exception
         */
        public BadGetMessageException(final String message) {
            super(message);
        }

        /**
         * Creates a new bad get message exception.
         *
         * @param cause the exception that caused this exception
         */
        public BadGetMessageException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new bad get message exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public BadGetMessageException(final String message, final Throwable cause) {
            super(message, cause);
        }

        @Override
        public String getMessage() {
            throw new IllegalArgumentException();
        }
    }

    public static final class NullMessageSensitiveException extends RuntimeException {

        /**
         * Creates a new null message sensitive exception.
         */
        public NullMessageSensitiveException() {
            super();
        }

        /**
         * Creates a new null message sensitive exception.
         *
         * @param message the message explaining what caused the exception
         */
        public NullMessageSensitiveException(final String message) {
            super(message);
        }

        /**
         * Creates a new null message sensitive exception.
         *
         * @param cause the exception that caused this exception
         */
        public NullMessageSensitiveException(final Throwable cause) {
            super(cause);
        }

        /**
         * Creates a new null message sensitive exception.
         *
         * @param message the message explaining what caused the exception
         * @param cause   the exception that caused this exception
         */
        public NullMessageSensitiveException(final String message, final Throwable cause) {
            super(message, cause);
            Objects.requireNonNull(message);
        }
    }
}
