package de.hatoka.common.capi.app;

import java.util.List;

import de.hatoka.common.capi.app.model.MessageVO;

public interface FrameRenderer
{
    String renderFame(String content, String... selectedItems);

    String renderFame(String content, List<MessageVO> messages, String... selectedItems);

    String renderNoFame(String content, String... selectedItems);
}
