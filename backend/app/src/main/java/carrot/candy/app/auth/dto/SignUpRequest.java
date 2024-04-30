package carrot.candy.app.auth.dto;

public record SignUpRequest(
        String name,
        String description,
        String imageUrl, // temp
        String email,
        String password
) {
}
