package carrot.candy.app.chat.dto.response;

public record MessageResponse(
        Long id,
        String content,
        Long memberId
) {

    public static MessageResponse of(Long id, String content, Long memberId) {
        return new MessageResponse(id, content, memberId);
    }
}
