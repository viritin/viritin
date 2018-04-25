package org.vaadin.viritin.util;

import com.vaadin.data.HasValue;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.Objects;

import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;

/**
 * A helper that provides access to browser cookies.
 *
 * @author Matti Tahvonen
 */
public class BrowserCookie {

    public interface Callback {

        void onValueDetected(String value);
    }

    public static void setCookie(String key, String value) {
        setCookie(key, value, "/");
    }

    public static void setCookie(String key, String value, LocalDateTime expirationTime) {

        String expires = toCookieGMTDate(expirationTime);

        JavaScript.getCurrent().execute(String.format(
            "document.cookie = \"%s=%s; expires=%s\";", key, value, expires
        ));
    }

    private static String toCookieGMTDate(LocalDateTime expirationTime) {
        ZonedDateTime zdt = ZonedDateTime.of(expirationTime, ZoneOffset.UTC);
        String expires = zdt.format(DateTimeFormatter.RFC_1123_DATE_TIME);
        return expires;
    }

    public static void setCookie(String key, String value, String path, LocalDateTime expirationTime) {

        String expires = toCookieGMTDate(expirationTime);

        JavaScript.getCurrent().execute(String.format(
                "document.cookie = \"%s=%s; path=%s\"; Expires=%s\";", key, value, path, expires
        ));
    }

    public static void setCookie(String key, String value, String path) {
        JavaScript.getCurrent().execute(String.format(
                "document.cookie = \"%s=%s; path=%s\";", key, value, path
        ));
    }

    public static void detectCookieValue(String key, final Callback callback) {
        final String callbackid = "viritincookiecb"+UUID.randomUUID().toString().substring(0,8);
        JavaScript.getCurrent().addFunction(callbackid, new JavaScriptFunction() {
            private static final long serialVersionUID = -3426072590182105863L;

            @Override
            public void call(JsonArray arguments) {
                JavaScript.getCurrent().removeFunction(callbackid);
                if(arguments.length() == 0) {
                    callback.onValueDetected(null);
                } else {
                    callback.onValueDetected(arguments.getString(0));
                }
            }
        });

        JavaScript.getCurrent().execute(String.format(
                "var nameEQ = \"%2$s=\";var ca = document.cookie.split(';');for(var i=0;i < ca.length;i++) {var c = ca[i];while (c.charAt(0)==' ') c = c.substring(1,c.length); if (c.indexOf(nameEQ) == 0) {%1$s( c.substring(nameEQ.length,c.length)); return;};} %1$s();",
                callbackid,key
        ));

    }
    
        /**
     *
     * Binds a HasValue&lt;V&gt; to a cookie that lives for a month. The cookies value is updated via a
     * ValueChangeListener.
     *
     * @param <V> The value-type of the HasValue&lt;&gt;
     * @param field The HasValue&lt;V&gt; that gets bound.
     * @param name The name of the cookie
     * @param cb A BrowserCookie.Callback that gets called with the actual value of the cookie. The value is guaranteed to be not null.
     *
     * @throws IllegalArgumentException if field or name are null or if name is empty.
     */
    public static <V> void bindValueToCookie(HasValue<V> field, String name, Callback cb) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (Objects.isNull(field)) {
            throw new IllegalArgumentException("Field must not be null");
        }

        detectCookieValue(name, (v) -> {
                                    if (v != null) {
                                        cb.onValueDetected(v);
                                    }
                                });

        field.addValueChangeListener((event) -> {
            setCookie(name, event.getValue().toString(), LocalDateTime.now().plusMonths(1l));
        });
    }

    /**
     * Binds a HasValue&lt;String&gt; to a cookie that lives for a month. The cookies value is updated via a
     * ValueChangeListener. Its crrent value is copied into the HasValue&lt;String&gt;.
     *
     * @param field The HasValue&lt;String&gt; that gets bound.
     * @param name The name of the cookie
     *
     * @throws IllegalArgumentException if field or name are null or if name is empty.
     */
    public static void bindValueToCookie(HasValue<String> field, String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (Objects.isNull(field)) {
            throw new IllegalArgumentException("Field must not be null");
        }

        detectCookieValue(name, (v) -> {
                                    if (v != null) {
                                        field.setValue(v);
                                    }
                                });

        field.addValueChangeListener((event) -> {
            setCookie(name, event.getValue(), LocalDateTime.now().plusMonths(1l));
        });
    }
}
