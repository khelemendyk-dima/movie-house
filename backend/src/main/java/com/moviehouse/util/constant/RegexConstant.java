package com.moviehouse.util.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RegexConstant {
    public static final String NAME_REGEX = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)*$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$";
}
