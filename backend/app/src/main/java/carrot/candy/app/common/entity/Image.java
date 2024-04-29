package carrot.candy.app.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    private static final String URL_PREFIX = "https://";

    @Column(nullable = false, length = 4098)
    private String imageUrl;

    public static Image createImage(String imageUrl) {
        validateUrl(imageUrl);
        return new Image(imageUrl);
    }

    private Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private static void validateUrl(String imageUrl) {
        if (!imageUrl.startsWith(URL_PREFIX)) {
            throw new IllegalArgumentException("invalid URL");
        }
    }
}
