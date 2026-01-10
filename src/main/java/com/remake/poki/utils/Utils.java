package com.remake.poki.utils;

import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Utils {

    public static String getMessage(String translationKey) {
        return getMessage(translationKey, (Object) null);
    }

    public static String getMessage(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            String pattern = ResourceBundle.getBundle(Constants.BUNDLE_NAME, locale).getString(key);
            return (args == null || args.length == 0) ? pattern : MessageFormat.format(pattern, args);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}

