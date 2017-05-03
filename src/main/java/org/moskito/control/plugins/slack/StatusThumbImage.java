package org.moskito.control.plugins.slack;

import org.moskito.control.core.HealthColor;

/**
 * Enum contains thumb images urls
 * corresponding to health color statuses.
 * Elements has method getImageUrl(), that return
 * url string routes to bulb image with corresponding to status color
 */
public enum StatusThumbImage {

    /**
     * Green.
     */
    GREEN {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/green.png";
        }
    },
    /**
     * Yellow.
     */
    YELLOW {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/yellow.png";
        }
    },
    /**
     * Orange.
     */
    ORANGE {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/orange.png";
        }
    },
    /**
     * Red.
     */
    RED {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/red.png";
        }
    },
    /**
     * Purple.
     */
    PURPLE {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/purple.png";
        }
    },
    /**
     * None yet.
     */
    NONE {
        @Override
        public String getImageUrl() {
            return "http://www.moskito.org/applications/control/none.png";
        }
    };

    /**
     * Returns thumb image corresponding to health color
     * @param color health color to search image
     * @return thumb image
     */
    public static StatusThumbImage getImageByColor(HealthColor color){
        try {
            return StatusThumbImage.valueOf(color.name());
        }
        catch (IllegalArgumentException e){
            return null;
        }
    }

    /**
     * Returns link to bulb image
     * @return url of bulb image
     */
    public abstract String getImageUrl();

}
