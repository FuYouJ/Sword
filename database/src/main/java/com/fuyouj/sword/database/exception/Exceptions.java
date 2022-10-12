package com.fuyouj.sword.database.exception;

public class Exceptions {
    private static final ColumnNotExist COLUMN_NOT_EXIST = new ColumnNotExist();
    private static final NotSupportedYet NOT_SUPPORTED_YET = new NotSupportedYet("");
    private static final OnlyOneDataKeyAllowed ONLY_ONE_DATA_KEY_ALLOWED = new OnlyOneDataKeyAllowed();
    private static final NoDefaultConstructor NO_DEFAULT_CONSTRUCTOR = new NoDefaultConstructor();
    private static final PropNotAccessible PROP_NOT_ACCESSIBLE = new PropNotAccessible();

    private static final ItemDoesNotExist NOT_EXIST = new ItemDoesNotExist();
    //    private static final ItemDuplicated ITEM_DUPLICATED = new ItemDuplicated();
    private static final ItemIdMustBeStringOrLong ITEM_ID_MUST_BE_STRING = new ItemIdMustBeStringOrLong();
    private static final ItemNotAssignableFromObjectUpdater ITEM_NOT_ASSIGNABLE_FROM_OBJECT_UPDATER =
            new ItemNotAssignableFromObjectUpdater();

    public static PropNotAccessible canNotAccessProp() {
        return PROP_NOT_ACCESSIBLE;
    }

    public static ColumnNotExist columnNotExist() {
        return COLUMN_NOT_EXIST;
    }

    public static ItemDuplicated duplicated(final String message, final Object conflictKey, final Object duplicatedValue) {
        return new ItemDuplicated(message, conflictKey, duplicatedValue);
    }

    public static ItemIdMustBeStringOrLong idMustBeStringOrLong() {
        return ITEM_ID_MUST_BE_STRING;
    }

    public static InvalidQueryOption invalidQueryOption(final String message) {
        return new InvalidQueryOption(message);
    }

    public static NoDefaultConstructor noDefaultConstructor() {
        return NO_DEFAULT_CONSTRUCTOR;
    }

    public static ItemDoesNotExist notExist() {
        return NOT_EXIST;
    }

    public static NotSupportedYet notSupported() {
        return NOT_SUPPORTED_YET;
    }

    public static ItemNotAssignableFromObjectUpdater objectMustAssignableToBeAbleToUpdate() {
        return ITEM_NOT_ASSIGNABLE_FROM_OBJECT_UPDATER;
    }

    public static OnlyOneDataKeyAllowed onlyOneDataKeyAllowed() {
        return ONLY_ONE_DATA_KEY_ALLOWED;
    }
}
