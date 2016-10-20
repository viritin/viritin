package org.vaadin.viritin.util;

import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;

import java.util.UUID;

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
}
