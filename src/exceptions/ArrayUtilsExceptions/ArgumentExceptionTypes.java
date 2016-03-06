package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 06/03/16.
 */
public enum ArgumentExceptionTypes {
    FILTER {
        @Override
        public String toString() {
            return "filter";
        }
    },
    JOIN {
        @Override
        public String toString() {
            return "join";
        }
    },
    TAKE {
        @Override
        public String toString() {
            return "take";
        }
    },
    REVERSE {
        @Override
        public String toString() {
            return "reverse";
        }
    },
    UNION {
        @Override
        public String toString() {
            return "union";
        }
    }
}
