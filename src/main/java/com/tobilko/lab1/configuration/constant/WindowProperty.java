package com.tobilko.lab1.configuration.constant;

/**
 * Created by Andrew Tobilko on 9/10/17.
 */
public final class WindowProperty {

    public static class ApplicationWindow {

        public static final String PATH_TO_SCHEMA = "src/main/resources/application.fxml";
        public static final String TITLE = "Andrew Tobilko [lab #1]";
        public static final int WINDOW_WIDTH = 500;
        public static final int WINDOW_HEIGHT = 500;

        private ApplicationWindow() {}

    }

    public static class AuthorisationWindow {

        public static final String PATH_TO_SCHEMA = "src/main/resources/authorisation-window.fxml";
        public static final String TITLE = "Login";
        public static final int WINDOW_WIDTH = 400;
        public static final int WINDOW_HEIGHT = 200;

        private AuthorisationWindow() {}

    }

    private WindowProperty() {}

}
