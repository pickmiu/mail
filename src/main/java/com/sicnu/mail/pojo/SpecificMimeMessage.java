package com.sicnu.mail.pojo;

import java.util.concurrent.atomic.AtomicLong;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Slf4j
public class SpecificMimeMessage extends MimeMessage {
    @Nullable
    private final String defaultEncoding;

    @Nullable
    private final FileTypeMap defaultFileTypeMap;

    private String appName;

    private static final AtomicLong increasement = new AtomicLong();

    /**
     * Create a new SmartMimeMessage.
     * 
     * @param session
     *            the JavaMail Session to create the message for
     * @param defaultEncoding
     *            the default encoding, or {@code null} if none
     * @param defaultFileTypeMap
     *            the default FileTypeMap, or {@code null} if none
     */
    public SpecificMimeMessage(Session session, @Nullable String defaultEncoding, @Nullable FileTypeMap defaultFileTypeMap,
                               String appName) {
        super(session);
        this.defaultEncoding = defaultEncoding;
        this.defaultFileTypeMap = defaultFileTypeMap;
        this.appName = appName;
    }

    /**
     * Return the default encoding of this message, or {@code null} if none.
     */
    @Nullable
    public final String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    /**
     * Return the default FileTypeMap of this message, or {@code null} if none.
     */
    @Nullable
    public final FileTypeMap getDefaultFileTypeMap() {
        return this.defaultFileTypeMap;
    }

    @Override
    protected void updateMessageID() throws MessagingException {
        setHeader("Message-ID", "<" + generateUniqueId() + ">");
    }

    protected String generateUniqueId() {
        Long currentTime = System.currentTimeMillis();
        increasement.getAndIncrement();
        String id = appName + "." + increasement.get() + "." + currentTime;
        log.info("[op:generateUniqueId] id={}", id);
        return id;
    }
}
